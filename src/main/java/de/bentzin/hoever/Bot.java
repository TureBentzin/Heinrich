package de.bentzin.hoever;

import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 07-10-2023
 */
public class Bot {

    private static JDA jda;

    /**
     * Entrypoint of application
     *
     * @param args 0 = token
     *             1 = bot_config [file]
     *            [2] = -d (debug)
     */
    public static void main(String[] args) {

    }

    @NotNull
    public static JDA getJda() {
        return jda;
    }

}
