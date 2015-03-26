package tk.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotChannel;
import tk.hintss.botss.BotUser;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;

/**
 * Created by Henry on 12/30/2014.
 */
public class PingCommand extends Command {
    @Override
    public void execute(Botss bot, String target, BotUser user, BotChannel channel, String... args) {
        if (args.length == 0) {
            bot.sendFormattedMessage(user, target, "PONG!");
        } else {
            StringBuilder sb = new StringBuilder("PONG ");

            for (String arg : args) {
                sb.append(arg);
                sb.append(" ");
            }

            bot.sendFormattedMessage(user, target, sb.toString());
        }
    }

    @Override
    public String getCommand() {
        return "ping";
    }

    @Override
    public String getHelpText() {
        return "ping [token]" + Colors.BOLD + " - PONG!";
    }
}
