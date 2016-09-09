package tk.hintss.botss;

import java.util.ArrayList;

public class MessageQueue {
    private int size = 50;
    private ArrayList<BotMessage> list = new ArrayList<>(size);

    public MessageQueue() {
    }

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
