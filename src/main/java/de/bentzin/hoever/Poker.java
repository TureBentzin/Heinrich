package de.bentzin.hoever;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.logging.Logger;

/**
 * @author Ture Bentzin
 * @since 09-10-2023
 */
public class Poker implements Runnable {

    @NotNull
    public static final ThreadGroup THREAD_GROUP = new ThreadGroup("hoever.poker");

    private boolean exit = false;
    private Thread thread;

    @Override
    public void run() {
        {
            final Logger logger = Logger.getLogger("Thread-PREP");
            logger.info("Thread was started!");
        }

        while (!exit) {
            try {
                final Logger logger = Logger.getLogger("Thread");
                int i = Bot.runCheck();
                if (i != 0) {
                    logger.info("Found " + i + " new files");
                }
                Thread.sleep(Duration.ofMinutes(10).toMillis());
            } catch (Exception e) {
                final Logger logger = Logger.getLogger("Thread-ERR");
                logger.severe("Failed to execute scheduled check: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    public void exit() {
        exit = true;
    }

    @NotNull
    public Thread start() {
        if(thread != null) {
            throw new IllegalStateException("Thread already running!");
        }
        thread = new Thread(THREAD_GROUP, this, "hoever-schedule");
        thread.start();
        return thread;
    }

    @Nullable
    public Thread getThread() {
        return thread;
    }
}
