package tk.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotChannel;
import tk.hintss.botss.BotUser;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;
import tk.hintss.botss.util.NumberUtil;

/**
 * Created by Henry on 1/2/2015.
 */
public class LuhnSumCommand extends Command {
    @Override
    public void execute(Botss bot, String target, BotUser user, BotChannel channel, String... args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(bot, user, target, getCommand());
            return;
        }

        long input;

        try {
            input = Long.parseLong(args[0]);
        } catch (NumberFormatException ex) {
            HelpCommand.sendHelp(bot, user, target, getCommand());
            return;
        }

        bot.sendFormattedMessage(user, target, "Luhn sum for " + args[0] + " = " + NumberUtil.luhnSum(input));
    }

    @Override
    public String getCommand() {
        return "luhnsum";
    }

    @Override
    public String getHelpText() {
        return "luhnsum <number>" + Colors.BOLD + " - Calculates the luhn sum of a number.";
    }
}
