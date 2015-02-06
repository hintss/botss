package tk.hintss.botss.util;

import org.jibble.pircbot.Colors;

/**
 * Created by hintss on 1/28/15.
 */
public class FormattingUtil {
    public static boolean hasFormatting(String message) {
        return message.equals(Colors.removeFormatting(message));
    }

    public static boolean equalsIgnoreFormatting(String message1, String message2) {
        return Colors.removeFormatting(message1).equals(Colors.removeFormatting(message2));
    }

    public static boolean containsIgnoreFormatting(String message, String contains) {
        return Colors.removeFormatting(message).contains(Colors.removeFormatting(contains));
    }
}
