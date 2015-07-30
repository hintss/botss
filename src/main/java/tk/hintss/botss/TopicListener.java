package tk.hintss.botss;

/**
 * Created by hintss on 7/29/2015.
 */
public interface TopicListener {
    public abstract void onTopic(Botss bot, BotUser setter, String oldTopic, String newTopic);
}
