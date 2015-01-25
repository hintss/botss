package tk.hintss.botss;

/**
 * Created by Henry on 1/25/2015.
 */
public class BotMessage {
    private final long time;

    private final String message;
    private final BotUser sender;
    private final BotChannel channel;

    public BotMessage(String message, BotUser user, BotChannel target) {
        time = System.currentTimeMillis();

        this.message = message;
        this.sender = user;
        this.channel = target;
    }

    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public BotUser getSender() {
        return sender;
    }

    public BotChannel getChannel() {
        return channel;
    }

    public String getIrcForm() {
        return "<" + getSender() + "> " + getMessage();
    }
}
