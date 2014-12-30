package tk.hintss.botss;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Henry on 12/30/2014.
 */
public class BotUser {
    private String nick;

    private String user = "unknownuser";
    private String host = "unknownhost";

    private final Set<BotChannel> channels = new HashSet<>();

    public BotUser(String nick) {
        this.nick = nick;
    }

    public BotUser(String nick, String user, String host) {
        this.nick = nick;

        this.user = user;
        this.host = host;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHostmask() {
        return (getNick() + "!" + getUser() + "@" + getHost());
    }

    public Set<BotChannel> getChannels() {
        return channels;
    }
}
