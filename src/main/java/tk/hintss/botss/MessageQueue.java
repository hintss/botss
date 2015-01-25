package tk.hintss.botss;

import java.util.ArrayList;

/**
 * Created by Henry on 1/25/2015.
 */
public class MessageQueue {
    int size = 0;
    ArrayList<BotMessage> list = new ArrayList<>();

    public MessageQueue(int size) {
        this.size = size;
    }

    public void addMessage(BotMessage message) {
        list.add(0, message);

        while (list.size() > size) {
            list.remove(size);
        }
    }

    public ArrayList<BotMessage> getMessages() {
        return new ArrayList<>(list);
    }
}