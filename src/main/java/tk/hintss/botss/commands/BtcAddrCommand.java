package tk.hintss.botss.commands;

import info.blockchain.api.APIException;
import info.blockchain.api.blockexplorer.Address;
import info.blockchain.api.blockexplorer.BlockExplorer;
import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotMessage;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by Henry on 12/30/2014.
 */
public class BtcAddrCommand extends Command {
    @Override
    public void execute(Botss bot, BotMessage bm, String... args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(bot, bm.getSender(), bm.getReplyTarget(), getCommand());
            return;
        }

        String addr = args[0];

        new Thread(() -> {
            try {
                BlockExplorer blockExplorer = new BlockExplorer();

                Address address = blockExplorer.getAddress(addr);
                bot.reply(bm, "BTC: " + Colors.BOLD + address.getAddress() + Colors.BOLD
                        + " Final Balance: " + Colors.BOLD + new DecimalFormat("#").format(address.getFinalBalance() / 100000000D) + Colors.BOLD
                        + " Total Received: " + Colors.BOLD + new DecimalFormat("#").format(address.getTotalReceived() / 100000000D) + Colors.BOLD
                        + " Total Sent: " + Colors.BOLD + address.getTotalSent() + Colors.BOLD
                        + " Blockchain.info: " + Colors.BOLD + "https://blockchain.info/address/" + address.getAddress());
            } catch (APIException e) {
                bot.reply(bm, Colors.RED + "APIException: " + e.getMessage());
            } catch (IOException e) {
                bot.reply(bm, Colors.RED + "IOException: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public String getCommand() {
        return "btcaddr";
    }

    @Override
    public String getHelpText() {
        return "btcaddr <bitcoin address>" + Colors.BOLD + " - gets bitcoin address info";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"btc", "bitcoin"};
    }
}
