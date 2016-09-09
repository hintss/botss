package pw.hintss.botss;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class BotMain {
    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration.Builder()
                .setAutoNickChange(true)
                .setAutoReconnect(true)
                .setLogin("botss")
                .setName("botss")
                .setOnJoinWhoEnabled(true)
                .setRealName("botss, by hintss")
                .setVersion("botss by hintss, using pircbotx 2.1, https://github.com/hintss/botss")
                .addServer("irc.subluminal.net")
                .addAutoJoinChannel("#programming")
                .buildConfiguration();

        PircBotX bot = new PircBotX(config);
        bot.startBot();
    }
}
