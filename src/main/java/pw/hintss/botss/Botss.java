package pw.hintss.botss;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.*;

public class Botss extends PircBot {
    public static final String commandPrefix = "]";

    public BotUser me;

    public Map<String, Command> commands = new HashMap<>();
    public Map<String, String> aliases = new HashMap<>();

    public List<MessageListener> messageListeners = new ArrayList<>();
    public List<TopicListener> topicListeners = new ArrayList<>();

    public Map<String, BotChannel> channels = new HashMap<>();
    public Map<String, BotUser> users = new HashMap<>();

    public Random rand = new Random();

    public Botss() {
        setName("botss");
        setAutoNickChange(true);
        setFinger("hintss");
        setLogin("hintss");
        setVersion("botss by hintss, using PircBot 1.5.0.");

        Reflections reflections = new Reflections("tk.hintss.botss.commands");

        Set<Class<? extends Command>> commandClasses = reflections.getSubTypesOf(Command.class);

        for (Class commandClass : commandClasses) {
            try {
                Command command = (Command) commandClass.newInstance();

                commands.put(command.getCommand().toLowerCase(), command);

                for (String alias : command.getAliases()) {
                    aliases.put(alias.toLowerCase(), command.getCommand().toLowerCase());
                }

                System.out.println("loaded " + command.getCommand().toLowerCase());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        reflections = new Reflections("tk.hintss.botss.messagelisteners");

        Set<Class<? extends MessageListener>> messageListeners = reflections.getSubTypesOf(MessageListener.class);

        for (Class messageListener : messageListeners) {
            try {
                MessageListener listener = (MessageListener) messageListener.newInstance();

                this.messageListeners.add(listener);

                System.out.println("loaded " + messageListener.getSimpleName());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        reflections = new Reflections("tk.hintss.botss.topiclisteners");

        Set<Class<? extends MessageListener>> topicListeners = reflections.getSubTypesOf(MessageListener.class);

        for (Class topicListener : topicListeners) {
            try {
                TopicListener listener = (TopicListener) topicListener.newInstance();

                this.topicListeners.add(listener);

                System.out.println("loaded " + topicListener.getSimpleName());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onMessage(String channel, String nick, String user, String host, String message) {
        processMessage(nick, user, host, channel, message);
    }

    @Override
    protected void onPrivateMessage(String nick, String user, String host, String message) {
        processMessage(nick, user, host, nick, message);
    }

    public void processMessage(String nick, String user, String host, String target, String message) {
        BotUser botUser = users.get(nick);

        if (botUser == null) {
            botUser = new BotUser(nick, user, host);
        }

        Messageable replyTarget = null;

        if (target.startsWith("#")) {
            replyTarget = channels.get(target);
        } else {
            replyTarget = botUser;
        }

        // if we picked up this user in the initial channel burst, we won't have their hostmask
        botUser.setUser(user);
        botUser.setHost(host);

        BotMessage bm = new BotMessage(message, botUser, replyTarget);
        botUser.said(bm);


        if (replyTarget == null) {
            botUser.sent(bm);
        } else {
            replyTarget.sent(bm);
        }

        if (message.startsWith(commandPrefix)) {
            String[] splitWithCommand = message.substring(1).split(" ");

            if (aliases.containsKey(splitWithCommand[0].toLowerCase())) {
                splitWithCommand[0] = aliases.get(splitWithCommand[0].toLowerCase());
            }

            if (commands.containsKey(splitWithCommand[0].toLowerCase())) {
                String[] args = new String[splitWithCommand.length - 1];

                System.arraycopy(splitWithCommand, 1, args, 0, args.length);

                commands.get(splitWithCommand[0].toLowerCase()).execute(this, bm, args);
            }
        }

        for (MessageListener listener : messageListeners) {
            listener.onMessage(this, bm);
        }
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        if (sender.equals(getNick())) {
            channels.put(channel, new BotChannel(channel));
        }

        addUser(sender, login, hostname, channel);

        me = users.get(getNick());
    }

    @Override
    protected void onPart(String channel, String nick, String user, String host) {
        removeUser(nick, channel);
    }

    @Override
    protected void onKick(String channel, String kickerNick, String kickerUser, String kickerHost, String recipient, String reason) {
        removeUser(recipient, channel);
    }

    @Override
    protected void onQuit(String nick, String user, String host, String reason) {
        removeUser(nick);
    }

    @Override
    protected void onUserList(String channel, User[] users) {
        for (User user : users) {
            addUser(user.getNick(), channel);
        }
    }

    @Override
    protected void onNickChange(String oldNick, String user, String host, String newNick) {
        BotUser botUser = users.get(oldNick);

        botUser.setNick(newNick);

        users.put(newNick, botUser);
        users.remove(oldNick);
    }

    @Override
    protected void onTopic(String channel, String topic, String setBy, long date, boolean changed) {
        for (TopicListener listener : topicListeners) {
            listener.onTopic(this, users.get(setBy), channels.get(channel).getTopic(), topic);
        }

        channels.get(channel).setTopic(topic, date, setBy);
    }

    @Override
    protected void onDisconnect() {
        Set<String> channelsList = channels.keySet();

        StringBuilder sb = new StringBuilder();

        for (String channel : channelsList) {
            sb.append(channel);
            sb.append(",");
        }

        channels.clear();
        users.clear();

        boolean connected = false;

        while (!connected) {
            try {
                Thread.sleep(500);
                reconnect();
                connected = true;
            } catch (InterruptedException | IOException | IrcException e) {
                e.printStackTrace();
            }
        }

        joinChannel(sb.toString());
    }

    /**
     * Adds a channel to that user if we already know of them, otherwise, add them to the hashmap
     * @param nick the nick of the user
     * @param channel the channel
     */
    public void addUser(String nick, String channel) {
        BotUser botUser = users.get(nick);
        BotChannel botChannel = channels.get(channel);

        if (botUser == null) {
            botUser = new BotUser(nick);
            users.put(nick, botUser);
        }

        HashSet<BotUser> channelUsers = botChannel.getUsers();
        channelUsers.add(botUser);
        botChannel.setUsers(channelUsers);
        HashSet<BotChannel> userChannels = botUser.getChannels();
        userChannels.add(botChannel);
        botUser.setChannels(userChannels);
    }

    /**
     * Adds a channel to that user if we already know of them, otherwise, add them to the hashmap
     * @param nick the nick of the user
     * @param user the user's username
     * @param host the user's hostname
     * @param channel the channel
     */
    public void addUser(String nick, String user, String host, String channel) {
        BotUser botUser = users.get(nick);
        BotChannel botChannel = channels.get(channel);

        if (botUser == null) {
            botUser = new BotUser(nick, user, host);
            users.put(nick, botUser);
        }

        HashSet<BotUser> channelUsers = botChannel.getUsers();
        channelUsers.add(botUser);
        botChannel.setUsers(channelUsers);
        HashSet<BotChannel> userChannels = botUser.getChannels();
        userChannels.add(botChannel);
        botUser.setChannels(userChannels);
    }

    /**
     * Removes a user from a channel
     * @param nick the user
     * @param channel the channel
     */
    public void removeUser(String nick, String channel) {
        BotUser botUser = users.get(nick);
        BotChannel botChannel = channels.get(channel);

        HashSet<BotUser> channelUsers = botChannel.getUsers();
        channelUsers.remove(botUser);
        botChannel.setUsers(channelUsers);
        HashSet<BotChannel> userChannels = botUser.getChannels();
        userChannels.remove(botChannel);
        botUser.setChannels(userChannels);

        if (botUser.getChannels().size() == 0) {
            removeUser(nick);
        }
    }

    /**
     * Removes a user
     * @param nick the user
     */
    public void removeUser(String nick) {
        BotUser user = users.get(nick);

        for (BotChannel channel : user.getChannels()) {
            HashSet<BotUser> channelUsers = channel.getUsers();
            channelUsers.remove(user);
            channel.setUsers(channelUsers);
        }

        users.remove(nick);
    }

    public void sendFormattedMessage(BotUser user, Messageable target, String message) {
        sendFormattedMessage(target, message);
    }

    public void sendFormattedMessage(Messageable target, String message) {
        message = message.replace("\7", "␇"); // replace bels with the bel representative
        sendMessage(target.getTargetName(), message);

        BotMessage bm = new BotMessage(message, me, target);
        me.said(bm);
        target.sent(bm);
    }

    public void reply(BotMessage bm, String message) {
        sendFormattedMessage(bm.getSender(), bm.getReplyTarget(), bm.getSender().getNick() + ": " + message);
    }
}
