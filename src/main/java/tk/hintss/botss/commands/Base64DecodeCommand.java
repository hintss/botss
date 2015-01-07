package tk.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotChannel;
import tk.hintss.botss.BotUser;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;

import java.util.Base64;

/**
 * Created by Henry on 1/6/2015.
 */
public class Base64DecodeCommand extends Command {
    @Override
    public void execute(Botss bot, String target, BotUser user, BotChannel channel, String... args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(bot, user, target, getCommand());
            return;
        }

        String decoded = new String(Base64.getDecoder().decode(args[0].getBytes()));

        bot.sendFormattedMessage(user, target, Colors.BOLD + "Decodes to: " + Colors.BOLD + decoded);
    }

    @Override
    public String getCommand() {
        return "base64decode";
    }

    @Override
    public String getHelpText() {
        return "base64decode <text>" + Colors.BOLD + " - decodes a string from base64";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"b64d"};
    }
}
