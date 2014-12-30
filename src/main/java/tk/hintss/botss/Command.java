package tk.hintss.botss;

/**
 * Created by Henry on 12/30/2014.
 */
public interface Command {
    public abstract void execute(Botss bot, String target, BotUser user, BotChannel channel, String... args);

    /**
     * return the command name (the part after the prefix and before the first space)
     * @return the command name
     */
    public abstract String getCommand();

    /**
     * gets the help text for the command
     * @return the help text
     */
    public abstract String getHelpText();
}
