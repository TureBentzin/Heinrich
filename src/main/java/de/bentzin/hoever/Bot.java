package de.bentzin.hoever;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import okio.Source;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
    public static WebHandler webHandler;
    @NotNull
    private static Configuration configuration;
    private static JDA jda;

    private static Poker poker;


    /**
     * Entrypoint of application
     *
     * @param args 0 = token
     */
    public static void main(String[] args) {
        Preconditions.checkNotNull(args[0], "Please provide a token");
        JDABuilder builder = JDABuilder.createDefault(args[0]);

        //APP
        try {
            configuration = Configuration.configuration("https://www.fh-aachen.de/menschen/hoever/lehrveranstaltungen/hoehere-mathematik-1/wochenplaene-2023/24-hoehere-mathematik-1",
                    Pattern.compile("https://www\\.hoever-downloads\\.fh-aachen\\.de/[^\\s]+"),
                    351973117872046080L,
                    OperationMode.TESTING);
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
                Commands.slash("update", "Trigger update").setGuildOnly(true),
                Commands.slash("exit", "SUDO: shutdown the bot"),
                Commands.slash("fake", "Simulate a response").addOption(OptionType.STRING,"url", "URL to be used", true)
        ).queue();

        {
            if (!configuration.operationMode().isProduction()) {
                jda.getPresence().setActivity(Activity.of(Activity.ActivityType.COMPETING, "Debugmode..."));
                jda.getPresence().setStatus(OnlineStatus.IDLE);
            }
        }

        poker = new Poker();
        poker.start();
    }

    public static int runCheck() {
        //read last data
        StorageManager storageManager = new StorageManager();
        Map<String, String> read = storageManager.read();
        //name url

        String[] scrape = webHandler.scrape();
        final ArrayList<String> news = new ArrayList<>();
        for (String s : scrape) {
            if (!read.containsKey(s)) {
                news.add(s);
            }
        }

        //news

        news.forEach(Bot::announceNewFile);

        Map<String, String> map = new HashMap<>(read);
        for (String s : scrape) {

            map.putIfAbsent(s, "void");
        }
        storageManager.parse(map);
        try {
            storageManager.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return news.size();
    }

    protected static void announceNewFile(@NotNull String url) {


        Guild hcGuild; //Hardcode will be changed later maybe.

        TextChannel channel;
        if (configuration.operationMode().isProduction()) {
            hcGuild = jda.getGuildById(1151171778438254757L);
            channel = hcGuild.getChannelById(TextChannel.class, 1160325127674810501L);
        } else {
            hcGuild = jda.getGuildById(874649328701022239L);
            channel = hcGuild.getChannelById(TextChannel.class, 874649330366160995L);

        }

        EmbedBuilder embed;
        {


        }

        channel.sendMessageEmbeds(EmbedUtil.process(url, Color.YELLOW))
                .queue();
    }


    @NotNull
    public static Configuration getConfiguration() {
        return configuration;
    }

    @NotNull
    public static JDA getJda() {
        return jda;
    }

    @NotNull
    public static WebHandler getWebHandler() {
        return webHandler;
    }
}
