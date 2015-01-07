package tk.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotChannel;
import tk.hintss.botss.BotUser;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;

import java.util.Base64;

/**
 * Created by Henry on 1/5/2015.
 */
public class Base64EncodeCommand extends Command {
    @Override
    public void execute(Botss bot, String target, BotUser user, BotChannel channel, String... args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(bot, user, target, getCommand());
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (String arg : args) {
            sb.append(" ");
            sb.append(arg);
        }

        String output = new String(Base64.getEncoder().encode(sb.substring(1).getBytes()));

        bot.sendFormattedMessage(user, target, Colors.BOLD + "Encodes to: " + Colors.BOLD + output);
    }

    @Override
    public String getCommand() {
        return "base64encode";
    }

    @Override
    public String getHelpText() {
        return "base64encode <text>" + Colors.BOLD + " - encodes a string in base64";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"b64e"};
    }
}
