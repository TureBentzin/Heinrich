package de.bentzin.hoever;

import com.google.common.base.Preconditions;
import jdk.management.jfr.ConfigurationInfo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Ture Bentzin
 * @since 07-10-2023
 */
public class Bot {

    @NotNull
    private static Configuration configuration;
    @NotNull
    public static WebHandler webHandler;

    /**
     * Entrypoint of application
     * @param args
     * 0 = token
     */
    public static void main(String[] args) {
        Preconditions.checkNotNull(args[0], "Please provide a token");
        JDABuilder builder = JDABuilder.createDefault(args[0]);

        //APP
        try {
            configuration = Configuration.configuration("https://www.fh-aachen.de/menschen/hoever/lehrveranstaltungen/hoehere-mathematik-1/wochenplaene-2023/24-hoehere-mathematik-1",
                    Pattern.compile("https://www\\.hoever-downloads\\.fh-aachen\\.de/[^\\s]+"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        webHandler = new WebHandler(configuration.homepage(), configuration.hrefPattern());

        // Disable parts of the cache
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);
        // Set activity (like "playing Something")
        builder.setActivity(Activity.watching("Polynomdivision"));

        builder.addEventListeners(new CommandListener());

        JDA jda = builder.build();

        jda.updateCommands().addCommands(
                Commands.slash("raw", "Download the current page")
        ).queue();
    }

    public void runCheck() {
        //read last data
        Map<String, String> read = new StorageManager().read();
        //name url

        String[] scrape = webHandler.scrape();
        ArrayList<String> news = new ArrayList<>();
        for (String s : scrape) {
            if(!read.containsValue(s)) {
                news.add(s);
            }
        }

        //news
        

    }

}
