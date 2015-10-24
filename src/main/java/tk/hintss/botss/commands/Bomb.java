package tk.hintss.botss.commands;

import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotMessage;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by hints on 10/17/2015.
 */
public class Bomb extends Command {
    private static List<String> PASSWORDS = Arrays.asList("about", "after", "again", "below", "could",
                                                          "every", "first", "found", "great", "house",
                                                          "large", "learn", "never", "other", "place",
                                                          "plant", "point", "right", "small", "sound",
                                                          "spell", "still", "study", "their", "there",
                                                          "these", "thing", "think", "three", "water",
                                                          "where", "which", "world", "would", "write");

    @Override
    public void execute(Botss bot, BotMessage bm, String... args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(bot, bm.getSender(), bm.getReplyTarget(), getCommand());
            return;
        }

        if (args[0].equalsIgnoreCase("password") || args[0].equalsIgnoreCase("pw")) {
            if (args.length != 6) {
                HelpCommand.sendHelp(bot, bm.getSender(), bm.getReplyTarget(), getCommand());
                return;
            }

            List<String> possibilities = new LinkedList<>(PASSWORDS);

            for (int i = 1; i <= 5; i++) {
                String col = args[i];
                final int j = i;

                Stream<String> toRemove = possibilities.stream().filter(possibility -> !col.contains(possibility.substring(j - 1, j)));

                possibilities.removeAll(toRemove.collect(Collectors.toList()));

                if (possibilities.size() <= 1) {
                    break;
                }
            }

            if (possibilities.size() == 0) {
                bot.reply(bm, "no matches!");
            } else if (possibilities.size() == 1) {
                bot.reply(bm, "Password is \"" + possibilities.get(0) + "\"");
            } else if (possibilities.size() > 1) {
                StringBuilder sb = new StringBuilder();

                for (String possibility : possibilities) {
                    sb.append(", ");
                    sb.append(possibility);
                }

                bot.reply(bm, "Multiple matches! " + sb.substring(2));
            }

            return;
        }
    }

    @Override
    public String getCommand() {
        return "keeptalkingandnobodyexplodes";
    }

    @Override
    public String getHelpText() {
        return "keeptalkingandnobodyexplodes <module>" + Colors.BOLD + " - decodes a string from base64";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"bomb", "b"};
    }
}
