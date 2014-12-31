package tk.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotChannel;
import tk.hintss.botss.BotUser;
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
public class MumbleCommand implements Command {
    @Override
    public void execute(Botss bot, String target, BotUser user, BotChannel channel, String... args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(bot, user, target, getCommand());
            return;
        }

        String ip = args[0];
        int port = 64738;

        if (args.length == 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                HelpCommand.sendHelp(bot, user, target, getCommand());
                return;
            }
        }

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

            bot.sendFormattedMessage(user, target, "'" + Colors.BOLD + ip + Colors.BOLD + "' currently has " + Colors.BOLD + currentUsers + "/" + maxUsers + Colors.BOLD + " users online.");
        } catch (UnknownHostException e) {
            bot.sendFormattedMessage(user, target, Colors.RED + "Couldn't resolve '" + Colors.BOLD + args[0] + Colors.BOLD + "'");
        } catch (IOException e) {
            bot.sendFormattedMessage(user, target, Colors.RED + "Couldn't connect to '" + Colors.BOLD + args[0] + Colors.BOLD + "' on " + Colors.BOLD + port);
        }
    }

    @Override
    public String getCommand() {
        return "mumble";
    }

    @Override
    public String getHelpText() {
        return "mumble <server> [port]" + Colors.BOLD + " - pings a mumble server";
    }
}
