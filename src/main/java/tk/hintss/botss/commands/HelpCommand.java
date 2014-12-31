package tk.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotChannel;
import tk.hintss.botss.BotUser;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;

/**
 * Created by Henry on 12/30/2014.
 */
public class HelpCommand implements Command {
    @Override
    public void execute(Botss bot, String target, BotUser user, BotChannel channel, String... args) {
        if (args.length == 0) {
            StringBuilder sb = new StringBuilder("Available commands: ");

            for (String cmd : bot.commands.keySet()) {
                sb.append(cmd);
                sb.append(", ");
            }

            bot.sendFormattedMessage(user, target, sb.substring(0, sb.length() - 2));
        } else {
            if (bot.commands.containsKey(args[0].toLowerCase())) {
                bot.sendFormattedMessage(user, target, Colors.BOLD + Botss.commandPrefix + bot.commands.get(args[0].toLowerCase()).getHelpText());
            } else {
                bot.sendFormattedMessage(user, target, Colors.RED + "Command '" + Colors.BOLD + args[0] + Colors.BOLD + "' not found!");
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

    public static void sendHelp(Botss bot, BotUser user, String target, String command) {
        bot.sendFormattedMessage(user, target, Colors.BOLD + Botss.commandPrefix + bot.commands.get(command.toLowerCase()).getHelpText());
    }
}
