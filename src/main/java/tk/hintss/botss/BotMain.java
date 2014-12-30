package tk.hintss.botss;

/**
 * Created by Henry on 12/30/2014.
 */
public class BotMain {
    public static void main(String[] args) throws Exception {
        Botss bot = new Botss();
        bot.setVerbose(true);
        bot.connect("irc.subluminal.net");
        bot.joinChannel("#programming");
    }
}
