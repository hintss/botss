package tk.hintss.botss;

public interface TopicListener {
    void onTopic(Botss bot, BotUser setter, String oldTopic, String newTopic);
}
