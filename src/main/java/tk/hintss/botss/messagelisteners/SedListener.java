package tk.hintss.botss.messagelisteners;

import tk.hintss.botss.BotMessage;
import tk.hintss.botss.Botss;
import tk.hintss.botss.MessageListener;

import java.util.ArrayList;

/**
 * Created by Henry on 1/25/2015.
 */
public class SedListener extends MessageListener {
    @Override
    public void onMessage(Botss bot, String target, BotMessage bm) {
        String message = bm.getMessage();
        // s/x// (shortest possible) == 4 chars
        if (message.length() > 4) {
            if (message.startsWith("s/")) {
                if (message.endsWith("/")) {
                    // the index of the / in the middle
                    int split = 2;

                    for (int i = 3; i < message.length(); i++) {
                        if (message.charAt(i) == '/') {
                            split = i;
                            break;
                        } else if (message.charAt(i) == '\\') {
                            // skip the / if there's a \ in front
                            i++;
                        }
                    }

                    if (split < message.length() - 1) {
                        String from = message.substring(2, split);
                        String to = message.substring(split + 1, message.length() - 1);

                        ArrayList<BotMessage> messages;

                        if (bm.getChannel() == null) {
                            messages = bm.getSender().getLastMessages();
                        } else {
                            messages = bm.getChannel().getLastMessages();
                        }

                        BotMessage replace = null;

                        for (BotMessage candidate : messages) {
                            if (candidate.getMessage().contains(from)) {
                                if (replace == null) {
                                    replace = candidate;
                                } else {
                                    replace = candidate;
                                    break;
                                }
                            }
                        }

                        if (replace != null) {
                            bot.sendFormattedMessage(bm.getSender(), target, replace.getIrcForm().replace(from, to));
                        }
                    }
                }
            }
        }
    }
}
