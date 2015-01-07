package tk.hintss.botss.commands;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import org.jibble.pircbot.Colors;
import tk.hintss.botss.BotChannel;
import tk.hintss.botss.BotUser;
import tk.hintss.botss.Botss;
import tk.hintss.botss.Command;

/**
 * Created by Henry on 12/30/2014.
 */
public class QrCommand extends Command {
    @Override
    public void execute(Botss bot, String target, BotUser user, BotChannel channel, String... args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(bot, user, target, getCommand());
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (String s : args) {
            sb.append(" ");
            sb.append(s);
        }

        String content = sb.substring(1);

        try {
            QRCode qr = Encoder.encode(content, ErrorCorrectionLevel.M);
            ByteMatrix matrix = qr.getMatrix();

            for (int i = 0; i < matrix.getHeight() / 2 + 1; i++) {
                StringBuilder line = new StringBuilder(" ");

                for (int j = 0; j < matrix.getWidth() / 2 + 1; j++) {
                    line.append(charAt(matrix, j, i));
                }

                bot.sendMessage(user.getNick(), line.toString());
            }
        } catch (WriterException e) {
            bot.sendFormattedMessage(user, target, e.getMessage());
        }
    }

    private char charAt(ByteMatrix matrix, int x, int y) {
        int index = getPixelAt(matrix, 2 * x + 1, 2 * y + 1) | getPixelAt(matrix, 2 * x + 1, 2 * y) << 1 | getPixelAt(matrix, 2 * x, 2 * y + 1) << 2 |  getPixelAt(matrix, 2 * x, 2 * y) << 3;
        char[] codes = {' ', '▗', '▝', '▐', '▖', '▄', '▞', '▟', '▘', '▚', '▀', '▜', '▌', '▙', '▛', '█'};

        return codes[index];
    }

    private int getPixelAt(ByteMatrix matrix, int x, int y) {
        if (x < 0) {
            return 0;
        } else if (y < 0) {
            return 0;
        } else if (x >= matrix.getWidth()) {
            return 0;
        } else if (y >= matrix.getHeight()) {
            return 0;
        }

        if (matrix.get(x, y) == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String getCommand() {
        return "qr";
    }

    @Override
    public String getHelpText() {
        return "qr <text>" + Colors.BOLD + " - encodes the text into a qr code";
    }
}
