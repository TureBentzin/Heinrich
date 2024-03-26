package de.bentzin.hoever;

import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ture Bentzin
 * @since 07-10-2023
 */
public class Bot {

    /* Exit codes */
    public static final int EXIT_CODE_UNKOWN = -1;
    public static final int NORMAL_EXIT = 0; // Everything is fine no restart
    public static final int RESTART_NO_UPDATE = 1; // Restart without updating
    public static final int RESTART_UPDATE = 2; // Restart with updating
    public static final int RESTART_ERROR = 3; // Restart because of error (try to update)
    public static final int UNRECOVERABLE_ERROR = 4; // Unrecoverable error (will not fix itself)
    /* Exit codes */


    @NotNull
    public static final Logger logger = LoggerFactory.getLogger(Bot.class);
    public static final Logger logger_hoever = LoggerFactory.getLogger("Prof. Dr. rer. nat. Dr.-Ing. Georg Hoever ");

    private static JDA jda;

    /**
     * Entrypoint of application
     *
     * @param args 0 = token
     *             1 = bot_config [file]
     *             [2] = -d (debug)
     */
    public static void main(String[] args) {
        //https://www.slf4j.org/api/org/slf4j/simple/SimpleLogger.html
        logger_hoever.info("Willkommen zur Hoeheren Mathematik!");
        if (args.length < 1 || args[0].isEmpty()) {
            logger.error("Please provide token at args[0]!");
            System.exit(UNRECOVERABLE_ERROR);
        }
    }

    @NotNull
    public static JDA getJda() {
        return jda;
    }

}