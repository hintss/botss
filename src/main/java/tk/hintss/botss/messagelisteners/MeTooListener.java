package tk.hintss.botss.messagelisteners;

import tk.hintss.botss.BotChannel;
import tk.hintss.botss.BotMessage;
import tk.hintss.botss.Botss;
import tk.hintss.botss.MessageListener;

/**
 * Created by Henry on 3/26/2015.
 */
public class MeTooListener implements MessageListener {

    @Override
    public void onMessage(Botss bot, BotMessage bm) {
        BotChannel channel = bm.getChannel();

        if (channel != null) {
            int count = 0;

            for (BotMessage msg : channel.getLastMessages()) {
                if (msg.getMessage().equalsIgnoreCase(bm.getMessage())) {
                    count++;
                } else {
                    break;
                }
            }

            if (count >= 2 + bot.rand.nextInt(3)) {
                if (bm.getMessage().equals("^")) {
                    bot.sendFormattedMessage(channel, "^");
                } else if (bm.getMessage().equalsIgnoreCase("lol")) {
                    bot.sendFormattedMessage(channel, "lol");
                } else if (bm.getMessage().equalsIgnoreCase("wat")) {
                    bot.sendFormattedMessage(channel, "wat");
                }
            }
        }
    }
}
