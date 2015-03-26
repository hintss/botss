package tk.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotMessage;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Henry on 12/30/2014.
 */
public class MumblePingCommand extends Command {
    @Override
    public void execute(Botss bot, BotMessage bm, String... args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(bot, bm.getSender(), bm.getTarget(), getCommand());
            return;
        }

        String ip = args[0];
        final int port;

        if (args.length == 2) {
            try {
                port = Integer.parseInt(args[1]);

                if (port < 1 || port > 65535) {
                    bot.reply(bm, Colors.RED + "Invalid port number '" + port + "'!");

                    return;
                }
            } catch (NumberFormatException ex) {
                bot.reply(bm, Colors.RED + "Invalid port number '" + args[1] + "'!");

                return;
            }
        } else {
            port = 64738;
        }

        new Thread(() -> {
            try {
                DatagramSocket clientsocket = new DatagramSocket();
                clientsocket.setSoTimeout(2000);

                InetAddress ipaddress = InetAddress.getByName(ip);

                byte[] sendData = "\000\000\000\000\000\000\000\000\000\000\000\000".getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipaddress, port);
                clientsocket.send(sendPacket);

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientsocket.receive(receivePacket);
                clientsocket.close();

                ByteArrayInputStream packets = new ByteArrayInputStream(receivePacket.getData());
                DataInputStream data = new DataInputStream(packets);

                data.readInt();
                data.readLong();

                int currentUsers = data.readInt();
                int maxUsers = data.readInt();

                bot.reply(bm, "'" + Colors.BOLD + ip + Colors.BOLD + "' currently has " + Colors.BOLD + currentUsers + "/" + maxUsers + Colors.BOLD + " users online.");
            } catch (UnknownHostException e) {
                bot.reply(bm, Colors.RED + "Couldn't resolve '" + Colors.BOLD + args[0] + Colors.BOLD + "'");
            } catch (IOException e) {
                bot.reply(bm, Colors.RED + "Couldn't connect to '" + Colors.BOLD + args[0] + Colors.BOLD + "' on " + Colors.BOLD + port);
            }
        }).start();
    }

    @Override
    public String getCommand() {
        return "mumbleping";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"mumble"};
    }

    @Override
    public String getHelpText() {
        return "mumbleping <server> [port]" + Colors.BOLD + " - pings a mumble server";
    }
}
