package pw.hintss.botss;

public class Command {
    public void execute(Botss bot, BotMessage bm, String... args) {
    }

    /**
     * return the command name (the part after the prefix and before the first space)
     * @return the command name
     */
    public String getCommand() {
        return "";
    }

    /**
     * gets the help text for the command
     * @return the help text
     */
    public String getHelpText() {
        return "";
    }

    /**
     * gets an array of the aliases of the command
     * @return array of aliases
     */
    public String[] getAliases() {
        return new String[0];
    }
}
