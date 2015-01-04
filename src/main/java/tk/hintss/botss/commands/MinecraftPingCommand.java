package tk.hintss.botss.commands;

import ch.jamiete.mcping.MinecraftPing;
import ch.jamiete.mcping.MinecraftPingOptions;
import ch.jamiete.mcping.MinecraftPingReply;
import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotChannel;
import tk.hintss.botss.BotUser;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;

import java.io.IOException;

/**
 * Created by Henry on 1/4/2015.
 */
public class MinecraftPingCommand extends Command {
    @Override
    public void execute(Botss bot, String target, BotUser user, BotChannel channel, String... args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(bot, user, target, getCommand());
            return;
        }

        String ip = args[0];
        final int port;

        if (args.length == 2) {
            try {
                port = Integer.parseInt(args[1]);

                if (port < 1 || port > 65535) {
                    bot.sendFormattedMessage(user, target, Colors.RED + "Invalid port number '" + port + "'!");

                    return;
                }
            } catch (NumberFormatException ex) {
                bot.sendFormattedMessage(user, target, Colors.RED + "Invalid port number '" + args[1] + "'!");

                return;
            }
        } else {
            port = 25565;
        }

        new Thread(() -> {
            try {
                MinecraftPingReply data = new MinecraftPing().getPing(new MinecraftPingOptions().setHostname(ip).setPort(port));
                bot.sendFormattedMessage(user, target, "'" + Colors.BOLD + ip + Colors.BOLD + "' - " + data.getVersion().getName() + " - " + Colors.BOLD + data.getPlayers().getOnline() + "/" + data.getPlayers().getMax() + Colors.BOLD + " - " + data.getDescription());
            } catch (IOException e) {
                bot.sendFormattedMessage(user, target, Colors.RED + "Couldn't connect to '" + Colors.BOLD + args[0] + Colors.BOLD + "' on " + Colors.BOLD + port);
            }
        }).start();
    }

    @Override
    public String getCommand() {
        return "minecraft";
    }

    @Override
    public String getHelpText() {
        return "minecraftping <server> [port]" + Colors.BOLD + " - pings a minecraft server";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"mcping", "mc", "minecraft"};
    }
}