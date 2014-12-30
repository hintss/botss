package tk.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotChannel;
import tk.hintss.botss.BotUser;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;

/**
 * Created by Henry on 12/30/2014.
 */
public class PingCommand implements Command {
    @Override
    public void execute(Botss bot, String target, BotUser user, BotChannel channel, String... args) {
        bot.sendFormattedMessage(user, target, "PONG!");
    }

    @Override
    public String getCommand() {
        return "ping";
    }

    @Override
    public String getHelpText() {
        return "ping" + Colors.BOLD + " - PONG!";
    }
}
