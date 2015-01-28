package tk.hintss.botss.messagelisteners;

import info.blockchain.api.APIException;
import info.blockchain.api.blockexplorer.Address;
import info.blockchain.api.blockexplorer.BlockExplorer;
import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotMessage;
import tk.hintss.botss.Botss;
import tk.hintss.botss.MessageListener;
import tk.hintss.botss.util.FormattingUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Henry on 1/25/2015.
 */
public class SedListener extends MessageListener {
    @Override
    public void onMessage(Botss bot, BotMessage bm) {
        String message = bm.getMessage();
        // s/x// (shortest possible) == 4 chars
        if (message.length() > 4 && message.startsWith("s/") && message.endsWith("/")) {
            // the index of the / in the middle
            int split = 2;

            for (int i = 3; i < message.length(); i++) {
                if (message.charAt(i) == '/') {
                    split = i;
                    break;
                } else if (message.charAt(i) == '\\') {
                    // skip the character after a \
                    i++;
                }
            }

            if (split < message.length() - 1) {
                String from = message.substring(2, split).replace("\\\\", "\\").replace("\\/", "/");
                String to = message.substring(split + 1, message.length() - 1).replace("\\\\", "\\").replace("\\/", "/");

                ArrayList<BotMessage> messages;

                if (bm.getChannel() == null) {
                    messages = bm.getSender().getLastPrivateMessages();
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
                    final BotMessage candidate = replace;

                    new Thread(() -> {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        ArrayList<BotMessage> newMessages;

                        if (bm.getChannel() == null) {
                            newMessages = bm.getSender().getLastMessages();
                        } else {
                            newMessages = bm.getChannel().getLastMessages();
                        }

                        boolean send = true;

                        for (BotMessage newMessage : newMessages) {
                            if (newMessage.getTime() < candidate.getTime()) {
                                break;
                            }

                            if (FormattingUtil.containsIgnoreFormatting(newMessage.getMessage(), candidate.getMessage().replace(from, to))) {
                                send = false;
                                break;
                            }
                        }

                        if (send) {
                            String result = "<" + candidate.getSender().getNick() + "> ";

                            if (FormattingUtil.hasFormatting(candidate.getMessage())) {
                                result += candidate.getMessage().replace(from, to);
                            } else {
                                result += candidate.getMessage().replace(from, Colors.BOLD + to + Colors.BOLD);
                            }
                            bot.reply(bm, result);
                        }
                    }).start();
                }
            }
        }
    }
}
