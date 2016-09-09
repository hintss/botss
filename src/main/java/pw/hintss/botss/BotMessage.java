package pw.hintss.botss;

public class BotMessage {
    private final long time;

    private final String message;
    private final BotUser sender;
    private final Messageable replyTarget; // either the sender (for a pm) or the channel (for a channel message)

    public BotMessage(String message, BotUser user, Messageable replyTarget) {
        time = System.currentTimeMillis();

        this.message = message;
        this.sender = user;
        this.replyTarget = replyTarget;
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

    public Messageable getReplyTarget() {
        return replyTarget;
    }

    public String getIrcForm() {
        return "<" + getSender().getNick() + "> " + getMessage();
    }
}
