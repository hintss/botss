package tk.hintss.botss;

import java.util.List;

public interface Messageable {
    String getTargetName();

    List<BotMessage> getLastMessages();
    void sent(BotMessage bm);
}
