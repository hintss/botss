package tk.hintss.botss.commands;

import tk.hintss.botss.BotChannel;
import tk.hintss.botss.BotUser;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;

/**
 * Created by Henry on 1/5/2015.
 */
public class Base64EncodeCommand extends Command {
    @Override
    public void execute(Botss bot, String target, BotUser user, BotChannel channel, String... args) {
        if (args.length > 0) {
            StringBuilder sb = new StringBuilder();


        }
    }

    @Override
    public String getCommand() {
        return "base64encode";
    }

    @Override
    public String getHelpText() {
        return super.getHelpText();
    }

    @Override
    public String[] getAliases() {
        return new String[] {"b64e"};
    }
}
