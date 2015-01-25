package tk.hintss.botss;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Henry on 12/30/2014.
 */
public class BotChannel {
    private final String name;

    private String topic;

    private String topicSetter;
    private long topicChangeTime;
    private String modes;

    private final Set<BotUser> users = new HashSet<>();

    private final MessageQueue lastMessages = new MessageQueue(20);

    public BotChannel(String name) {
        this.name = name;
    }

    public void setTopic(String topic, long topicChangeTime, String topicSetter) {
        this.topic = topic;

        this.topicChangeTime = topicChangeTime;
        this.topicSetter = topicSetter;
    }

    public String getTopic() {
        return topic;
    }

    public String getTopicSetter() {
        return topicSetter;
    }

    public String getName() {
        return name;
    }

    public long getTopicChangeTime() {
        return topicChangeTime;
    }

    public Set<BotUser> getUsers() {
        return users;
    }

    public void said(BotMessage message) {
        lastMessages.addMessage(message);
    }

    public ArrayList<BotMessage> getLastMessages() {
        return lastMessages.getMessages();
    }
}
