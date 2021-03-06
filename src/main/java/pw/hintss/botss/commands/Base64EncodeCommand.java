package pw.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import pw.hintss.botss.BotMessage;
import pw.hintss.botss.Botss;
import pw.hintss.botss.Command;

import java.util.Base64;

public class Base64EncodeCommand extends Command {
    @Override
    public void execute(Botss bot, BotMessage bm, String... args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(bot, bm.getSender(), bm.getReplyTarget(), getCommand());
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (String arg : args) {
            sb.append(" ");
            sb.append(arg);
        }

        String output = new String(Base64.getEncoder().encode(sb.substring(1).getBytes()));

        bot.reply(bm, Colors.BOLD + "Encodes to: " + Colors.BOLD + output);
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
