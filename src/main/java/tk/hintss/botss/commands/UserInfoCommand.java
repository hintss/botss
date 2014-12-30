package tk.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotChannel;
import tk.hintss.botss.BotUser;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;

/**
 * Created by Henry on 12/30/2014.
 */
public class UserInfoCommand implements Command {
    @Override
    public void execute(Botss bot, String target, BotUser user, BotChannel channel, String... args) {
        String nick;

        if (args.length == 0) {
            nick = user.getNick();
        } else {
            nick = args[0];
        }

        BotUser botUser = bot.users.get(nick);

        if (botUser != null) {
            bot.sendFormattedMessage(user, target, "nick: " + botUser.getNick() + ", user: " + botUser.getUser() + ", host: " + botUser.getHost());
        } else {
            bot.sendFormattedMessage(user, target, "User not found!");
        }
    }

    @Override
    public String getCommand() {
        return "userinfo";
    }

    @Override
    public String getHelpText() {
        return "userinfo [nick]" + Colors.BOLD + " - gets what this bot knows about that user (or you if you don't specify)";
    }
}
