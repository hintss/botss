package tk.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotMessage;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;

import java.util.Base64;

public class Base64DecodeCommand extends Command {
    @Override
    public void execute(Botss bot, BotMessage bm, String... args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(bot, bm.getSender(), bm.getReplyTarget(), getCommand());
            return;
        }

        String decoded = new String(Base64.getDecoder().decode(args[0].getBytes()));

        bot.reply(bm, Colors.BOLD + "Decodes to: " + Colors.BOLD + decoded);
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
