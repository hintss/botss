package pw.hintss.botss;

public class BotMain {
    public static void main(String[] args) throws Exception {
        Botss bot = new Botss();
        bot.setVerbose(true);
        bot.connect("irc.subluminal.net");
        bot.setMode(bot.getNick(), "+B");
        bot.joinChannel("#programming");
    }
}
