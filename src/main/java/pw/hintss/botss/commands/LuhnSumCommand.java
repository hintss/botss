package pw.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import pw.hintss.botss.BotMessage;
import pw.hintss.botss.Botss;
import pw.hintss.botss.Command;
import pw.hintss.botss.util.NumberUtil;

public class LuhnSumCommand extends Command {
    @Override
    public void execute(Botss bot, BotMessage bm, String... args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(bot, bm.getSender(), bm.getReplyTarget(), getCommand());
            return;
        }

        long input;

        try {
            input = Long.parseLong(args[0]);
        } catch (NumberFormatException ex) {
            HelpCommand.sendHelp(bot, bm.getSender(), bm.getReplyTarget(), getCommand());
            return;
        }

        bot.reply(bm, "Luhn sum for " + args[0] + " = " + NumberUtil.luhnSum(input));
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
