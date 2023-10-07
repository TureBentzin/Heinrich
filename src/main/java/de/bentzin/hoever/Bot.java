package de.bentzin.hoever;

import com.google.common.base.Preconditions;
import jdk.management.jfr.ConfigurationInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static JDA jda;

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

        jda = builder.build();

        jda.updateCommands().addCommands(
                Commands.slash("raw", "Download the current page"),
                Commands.slash("update", "Trigger update").setGuildOnly(true)
        ).queue();
    }

    public static int runCheck() {
        //read last data
        StorageManager storageManager = new StorageManager();
        Map<String, String> read = storageManager.read();
        //name url

        String[] scrape = webHandler.scrape();
        final ArrayList<String> news = new ArrayList<>();
        for (String s : scrape) {
            if(!read.containsKey(s)) {
                news.add(s);
            }
        }

        //news

        news.forEach(Bot::announceNewFile);

        Map<String, String> map = new HashMap<>();
        for (String s : scrape) {
            map.put(s, "void");
        }
        storageManager.parse(map);
        try {
            storageManager.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return news.size();
    }

    private static void announceNewFile(@NotNull String url){


        Guild hcGuild = jda.getGuildById(1151171778438254757L); //Hardcode will be changed later maybe.
        TextChannel channel = hcGuild.getChannelById(TextChannel.class, 1151171778928975877L);


        /*
                Guild hcGuild = jda.getGuildById(874649328701022239L); //Hardcode will be changed later maybe. Testserver
        TextChannel channel = hcGuild.getChannelById(TextChannel.class, 874649330366160995L);

         */

        MessageEmbed embed;
        {
            URL fileURl = null;
            try {
                fileURl = new URL(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            String filename = fileURl.getFile();
            String[] pathSegments = filename.split("/");
            String extractedFilename = pathSegments[pathSegments.length - 1];

            embed = new EmbedBuilder()
                    .setTitle(extractedFilename)
                    .setColor(Color.YELLOW)
                    .setAuthor("1Hoever - Beta")
                    .build();
        }

        channel.sendMessageEmbeds(embed).addContent(url).queue();
    }

}
