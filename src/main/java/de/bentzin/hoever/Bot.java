package de.bentzin.hoever;

import com.google.common.base.Preconditions;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

/**
 * @author Ture Bentzin
 * @since 07-10-2023
 */
public class Bot {

    /**
     * Entrypoint of application
     * @param args
     * 0 = token
     */
    public static void main(String[] args) {
        Preconditions.checkNotNull(args[0], "Please provide a token");
        JDABuilder builder = JDABuilder.createDefault(args[0]);

        // Disable parts of the cache
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);
        // Set activity (like "playing Something")
        builder.setActivity(Activity.watching("Polynomdivision"));

        builder.build();
    }

}
