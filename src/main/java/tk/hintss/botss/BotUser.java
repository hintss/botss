package tk.hintss.botss;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Henry on 12/30/2014.
 */
public class BotUser implements Messageable {
    private String nick;

    private String user;
    private String host;

    private Set<BotChannel> channels = new HashSet<>();

    private final MessageQueue lastMessages = new MessageQueue();
    private final MessageQueue lastPrivateMessages = new MessageQueue();

    protected BotUser(String nick) {
        this.nick = nick;
    }

    protected BotUser(String nick, String user, String host) {
        this.nick = nick;

        this.user = user;
        this.host = host;
    }

    public String getNick() {
        return nick;
    }

    protected void setNick(String nick) {
        this.nick = nick;
    }

    public String getUser() {
        return user;
    }

    protected void setUser(String user) {
        this.user = user;
    }

    public String getHost() {
        return host;
    }

    protected void setHost(String host) {
        this.host = host;
    }

    public String getHostmask() {
        return (getNick() + "!" + getUser() + "@" + getHost());
    }

    public HashSet<BotChannel> getChannels() {
        return new HashSet<>(channels);
    }

    protected void setChannels(HashSet<BotChannel> newChannels) {
        this.channels = newChannels;
    }

    protected void said(BotMessage message) {
        lastMessages.addMessage(message);
    }

    protected void pmed(BotMessage message) {
        lastPrivateMessages.addMessage(message);
    }

    public ArrayList<BotMessage> getLastMessages() {
        return lastMessages.getMessages();
    }

    public ArrayList<BotMessage> getLastPrivateMessages() {
        return lastPrivateMessages.getMessages();
    }

    @Override
    public String getTargetName() {
        return nick;
    }
}
