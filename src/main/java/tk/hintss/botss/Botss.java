package tk.hintss.botss;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Henry on 12/30/2014.
 */
public class Botss extends PircBot {
    public static final String commandPrefix = "]";

    public HashMap<String, Command> commands = new HashMap<>();
    public HashMap<String, String> aliases = new HashMap<>();

    public ArrayList<MessageListener> listeners = new ArrayList<>();

    public HashMap<String, BotChannel> channels = new HashMap<>();
    public HashMap<String, BotUser> users = new HashMap<>();

    public Botss() {
        this.setName("botss");
        this.setAutoNickChange(true);
        this.setFinger("hintss");
        this.setLogin("hintss");
        this.setVersion("botss by hintss, using PircBot 1.5.0.");

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

                listeners.add(listener);

                System.out.println("loaded " + messageListener.getSimpleName());
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

        BotChannel botChannel = null;

        if (target.startsWith("#")) {
            botChannel = channels.get(target);
        }

        if (botUser == null) {
            botUser = new BotUser(nick, user, host);
        }

        // if we picked up this user in the initial channel burst, we won't have their hostmask
        botUser.setUser(user);
        botUser.setHost(host);

        BotMessage bm = new BotMessage(message, botUser, botChannel);
        botUser.said(bm);


        if (botChannel == null) {
            botUser.pmed(bm);
        } else {
            botChannel.said(bm);
        }

        if (message.startsWith(commandPrefix)) {
            String[] splitWithCommand = message.substring(1).split(" ");

            if (aliases.containsKey(splitWithCommand[0].toLowerCase())) {
                splitWithCommand[0] = aliases.get(splitWithCommand[0].toLowerCase());
            }

            if (commands.containsKey(splitWithCommand[0].toLowerCase())) {
                String[] args = new String[splitWithCommand.length - 1];

                System.arraycopy(splitWithCommand, 1, args, 0, args.length);

                commands.get(splitWithCommand[0].toLowerCase()).execute(this, target, botUser, botChannel, args);
            }
        }

        for (MessageListener listener : listeners) {
            listener.onMessage(this, bm);
        }
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        if (sender.equals(getNick())) {
            channels.put(channel, new BotChannel(channel));
        }

        addUser(sender, login, hostname, channel);
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
                reconnect();
                connected = true;
            } catch (IOException | IrcException e) {
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
    }

    public void reply(BotMessage bm, String message) {
        Messageable target;

        if (bm.getChannel() == null) {
            target = bm.getSender();
        } else {
            target = bm.getChannel();
        }

        sendFormattedMessage(bm.getSender(), target, message);
    }
}
