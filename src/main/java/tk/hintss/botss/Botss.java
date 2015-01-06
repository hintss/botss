package tk.hintss.botss;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Henry on 12/30/2014.
 */
public class Botss extends PircBot {
    public static final String commandPrefix = "]";

    public HashMap<String, Command> commands = new HashMap<>();
    public HashMap<String, String> aliases = new HashMap<>();

    public HashMap<String, BotChannel> channels = new HashMap<>();
    public HashMap<String, BotUser> users = new HashMap<>();

    public Botss() {
        this.setName("botss");
        this.setAutoNickChange(true);
        this.setFinger("hintss");
        this.setLogin("hintss");
        this.setVersion("botss by hintss, using PircBot 1.5.0.");
        this.setMessageDelay(500);

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
    }

    @Override
    protected void onMessage(String channel, String nick, String user, String host, String message) {
        BotUser botUser = users.get(nick);
        BotChannel botChannel = channels.get(channel);

        if (botUser == null) {
            botUser = new BotUser(nick, user, host);
        }

        botUser.setUser(user);
        botUser.setHost(host);

        if (message.startsWith(commandPrefix)) {
            String[] splitWithCommand = message.substring(1).split(" ");

            if (aliases.containsKey(splitWithCommand[0].toLowerCase())) {
                splitWithCommand[0] = aliases.get(splitWithCommand[0].toLowerCase());
            }

            if (commands.containsKey(splitWithCommand[0].toLowerCase())) {
                String[] args = new String[splitWithCommand.length - 1];

                System.arraycopy(splitWithCommand, 1, args, 0, args.length);

                commands.get(splitWithCommand[0].toLowerCase()).execute(this, channel, botUser, botChannel, args);
            }
        }
    }

    @Override
    protected void onPrivateMessage(String nick, String user, String host, String message) {
        BotUser botUser = users.get(nick);

        if (botUser == null) {
            botUser = new BotUser(nick, user, host);
        }

        botUser.setUser(user);
        botUser.setHost(host);

        if (message.startsWith(commandPrefix)) {
            String[] splitWithCommand = message.split(" ");

            if (aliases.containsKey(splitWithCommand[0].toLowerCase())) {
                splitWithCommand[0] = aliases.get(splitWithCommand[0].toLowerCase());
            }

            if (commands.containsKey(splitWithCommand[0].toLowerCase().substring(1))) {
                String[] args = new String[splitWithCommand.length - 1];

                System.arraycopy(splitWithCommand, 1, args, 0, args.length);

                commands.get(splitWithCommand[0].substring(1).toLowerCase()).execute(this, nick, botUser, null, args);
            }
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


        StringBuilder sb = new StringBuilder();

        for (String channel : channelsList) {
            sb.append(channel);
            sb.append(",");
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

        botChannel.getUsers().add(botUser);
        botUser.getChannels().add(botChannel);
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

        botChannel.getUsers().add(botUser);
        botUser.getChannels().add(botChannel);
    }

    /**
     * Removes a user from a channel
     * @param nick the user
     * @param channel the channel
     */
    public void removeUser(String nick, String channel) {
        BotUser botUser = users.get(nick);
        BotChannel botChannel = channels.get(channel);

        botUser.getChannels().remove(botChannel);
        botChannel.getUsers().remove(botUser);

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
            channel.getUsers().remove(user);
        }

        users.remove(nick);
    }

    public void sendFormattedMessage(BotUser user, String target, String message) {
        sendMessage(target, message);
    }
}
