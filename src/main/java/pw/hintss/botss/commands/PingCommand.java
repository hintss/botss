package pw.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import pw.hintss.botss.BotMessage;
import pw.hintss.botss.Botss;
import pw.hintss.botss.Command;

public class PingCommand extends Command {
    @Override
    public void execute(Botss bot, BotMessage bm, String... args) {
        if (args.length == 0) {
            bot.reply(bm, "PONG!");
        } else {
            StringBuilder sb = new StringBuilder("PONG ");

            for (String arg : args) {
                sb.append(arg);
                sb.append(' ');
            }

            bot.reply(bm, sb.toString());
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
