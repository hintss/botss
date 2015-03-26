package tk.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotMessage;
import tk.hintss.botss.BotUser;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;
import tk.hintss.botss.Messageable;

/**
 * Created by Henry on 12/30/2014.
 */
public class HelpCommand extends Command {
    @Override
    public void execute(Botss bot, BotMessage bm, String... args) {
        if (args.length == 0) {
            StringBuilder sb = new StringBuilder("Available commands: ");

            for (String cmd : bot.commands.keySet()) {
                sb.append(cmd);
                sb.append(", ");
            }

            bot.reply(bm, sb.substring(0, sb.length() - 2));
        } else {
            if (bot.commands.containsKey(args[0].toLowerCase())) {
                bot.reply(bm, Colors.BOLD + Botss.commandPrefix + bot.commands.get(args[0].toLowerCase()).getHelpText());
            } else {
                bot.reply(bm, Colors.RED + "Command '" + Colors.BOLD + args[0] + Colors.BOLD + "' not found!");
            }
        }
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getHelpText() {
        return "help [command]" + Colors.BOLD + " - Retrieves either the list of commands, or the help text for the specified command.";
    }

    public static void sendHelp(Botss bot, BotUser user, Messageable target, String command) {
        bot.sendFormattedMessage(user, target, Colors.BOLD + Botss.commandPrefix + bot.commands.get(command.toLowerCase()).getHelpText());
    }
}
