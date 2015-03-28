package tk.hintss.botss;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Henry on 12/30/2014.
 */
public class BotChannel implements Messageable {
    private final String name;

    private String topic;

    private String topicSetter;
    private long topicChangeTime;
    private String modes;

    private Set<BotUser> users = new HashSet<>();

    private final MessageQueue lastMessages = new MessageQueue();

    protected BotChannel(String name) {
        this.name = name;
    }

    protected void setTopic(String topic, long topicChangeTime, String topicSetter) {
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

    public HashSet<BotUser> getUsers() {
        return new HashSet<>(users);
    }

    protected void setUsers(HashSet<BotUser> newUsers) {
        this.users = newUsers;
    }

    public void sent(BotMessage message) {
        lastMessages.addMessage(message);
    }

    public ArrayList<BotMessage> getLastMessages() {
        return lastMessages.getMessages();
    }

    @Override
    public String getTargetName() {
        return name;
    }
}
