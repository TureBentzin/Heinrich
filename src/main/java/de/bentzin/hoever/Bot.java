package de.bentzin.hoever;

import de.bentzin.hoever.command.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.TimeFormat;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

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
    /* Managers (populated here or before bot start) */
    @NotNull
    private static final GsonManager gsonManager = new GsonManager();
    private static final long session_start = System.currentTimeMillis();
    /* DEBUG */
    public static boolean debug = false;
    @Nullable
    private static DatabaseManager databaseManager;
    /* Objects populated on main */
    private static JDA jda;
    private static ConfigObject configObject;

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
        String token = null;
        File config;
        if (args.length > 2 && args[2].equals("-d")) {
            debug = true;
            logger.info("Debug mode enabled!");
        }
        if (args.length < 1 || args[0].isEmpty()) {
            logger.error("Please provide token at args[0]!");
            System.exit(UNRECOVERABLE_ERROR);
        } else {
            token = args[0];
            logger.info("Token was loaded from argument!");
        }
        if (args.length < 2 || args[1].isEmpty()) {
            logger.error("Please provide bot_config at args[1]!");
            System.exit(UNRECOVERABLE_ERROR);
        } else {
            config = new File(args[1]);
            if (!config.exists()) {
                logger.error("{} does not exist!", config.getAbsolutePath());
                createConfig(config);
                logger.info("Please fill out the config file at {}. The application will shut down now!", config.getAbsolutePath());
                System.exit(UNRECOVERABLE_ERROR);
            } else {
                //config exists
                configObject = getGsonManager().fromJson(config, ConfigObject.class);
                if (configObject == null) {
                    logger.error("Could not load config file!");
                    System.exit(UNRECOVERABLE_ERROR);
                } else {
                    logger.info("Config file was loaded successfully!");
                }
            }
        }

        // continue bootstrap
        databaseManager = new DatabaseManager(configObject.getSqlitePath());
        logger.info("DatabaseManager was created successfully!");
        {
            //initial setup of the database
            databaseManager.createTables();
        }

        /* Commands */
        GCommandListener gCommandListener = new GCommandListener();

        SayCommand sayCommand = new SayCommand();
        gCommandListener.register(sayCommand);
        gCommandListener.register(new ExitCommand());
        if (debug) gCommandListener.register(new ConnectTestCommand());
        gCommandListener.register(new SetChannelCommand());


        //JDA Startup
        {
            JDABuilder jdaBuilder = JDABuilder.createDefault(token);
            jdaBuilder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
            jdaBuilder.setBulkDeleteSplittingEnabled(false).setActivity(Activity.competing("Höhere Mathematik 1.5"));
            jdaBuilder.addEventListeners(gCommandListener);
            jda = jdaBuilder.build();
            gCommandListener.updateJDA(jda);
        }
    }

    @NotNull
    public static JDA getJda() {
        return jda;
    }

    public static void createConfig(@NotNull File file) {
        //noinspection ResultOfMethodCallIgnored
        file.delete();
        try {
            boolean newFile = file.createNewFile();
            if (newFile) {
                logger.info("Created new config file at {}", file.getAbsolutePath());
                //populate config
                ConfigObject configObject = ConfigObject.defaultConfig();
                getGsonManager().toJson(file, configObject);
                logger.info("Populated new config file with default values!");
            } else {
                logger.error("Could not create new config file at {}", file.getAbsolutePath());
                System.exit(UNRECOVERABLE_ERROR);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @NotNull
    public static ConfigObject getConfig() {
        return configObject;
    }

    @NotNull
    public static GsonManager getGsonManager() {
        return gsonManager;
    }

    public static boolean isAdmin(long id) {
        return configObject.getAdminIds().contains(id);
    }

    public static long getSessionDuration() {
        return System.currentTimeMillis() - session_start;
    }

    @NotNull
    public static String getSessionDurationString() {
        return Utils.formatDuration(Duration.ofMillis(getSessionDuration()));
    }

    @NotNull
    public static String getSessionStartTimeFormat() {
        return TimeFormat.RELATIVE.format(session_start);
    }

    public static void shutdown() {
        shutdown(NORMAL_EXIT);
    }

    public static void shutdown(int code) {
        logger.info("Shutting down with exit code {} after session with length of {}", code, getSessionDurationString());
        jda.shutdown();
        System.exit(code);
    }
}