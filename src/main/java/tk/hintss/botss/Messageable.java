package tk.hintss.botss;

import java.util.List;

/**
 * Created by Henry on 3/26/2015.
 */
public interface Messageable {
    public abstract String getTargetName();

    public abstract List<BotMessage> getLastMessages();
    public abstract void sent(BotMessage bm);
}
