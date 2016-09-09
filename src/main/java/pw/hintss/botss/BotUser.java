package pw.hintss.botss;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BotUser implements Messageable {
    private String nick;

    private String user;
    private String host;

    private Set<BotChannel> channels = new HashSet<>();

    private final MessageQueue lastSentMessages = new MessageQueue();
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
        return getNick() + '!' + getUser() + '@' + getHost();
    }

    public HashSet<BotChannel> getChannels() {
        return new HashSet<>(channels);
    }

    protected void setChannels(Set<BotChannel> newChannels) {
        channels = newChannels;
    }

    protected void said(BotMessage message) {
        lastSentMessages.addMessage(message);
    }

    @Override
    public void sent(BotMessage message) {
        lastPrivateMessages.addMessage(message);
    }

    public List<BotMessage> getLastSentMessages() {
        return lastSentMessages.getMessages();
    }

    @Override
    public List<BotMessage> getLastMessages() {
        return lastPrivateMessages.getMessages();
    }

    @Override
    public String getTargetName() {
        return nick;
    }
}
