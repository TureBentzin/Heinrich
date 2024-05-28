package de.bentzin.hoever.web;

import de.bentzin.hoever.Bot;
import de.bentzin.hoever.DBChannel;
import de.bentzin.hoever.SendOrder;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.internal.entities.EntityBuilder;
import net.dv8tion.jda.internal.utils.Helpers;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ture Bentzin
 * @since 26-03-2024
 */
public class DataManager {
    @NotNull
    public static final Logger logger = LoggerFactory.getLogger(DataManager.class);

    @NotNull
    public static final Set<String> allowedHosts = Set.of(
            "https://www.hoever-downloads.fh-aachen.de",
            "https://www.hm-kompakt.de/",
            "https://video.fh-aachen.de/",
            "https://youtube.com/"
    );

    public DataManager() {

    }

    /**
     * @return number of messages sent
     */
    public int update() {
        //Eventlink, File
        Map<String, String> index = new HashMap<>();
        for (URL event : Bot.getDatabaseManager().getEvents()) {
            logger.info("Downloading event: {}", event);
            try {
                String event_file = downloadHTML(event);
                index.put(event.toString(), event_file.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        logger.info("Downloaded {} events", index.size());
        logger.info("Parsing events to individual items");
        //Eventlink, Items
        Map<String, Set<Item>> items = new HashMap<>();
        int itemsParsed = 0;
        for (Map.Entry<String, String> entry : index.entrySet()) {
            items.put(entry.getKey(), parse(entry.getValue()));
            itemsParsed += items.get(entry.getKey()).size();
        }
        logger.info("Parsed {} items", itemsParsed);
        logger.info("Updating database");
        Bot.getDatabaseManager().updateFileItems(items);
        logger.info("Database updated");
        logger.info("Retrieving channels from database");
        Set<DBChannel> channels = Bot.getDatabaseManager().getChannels();
        for(DBChannel channel : channels) {
            try {
                TextChannel textChannel = Bot.getJda().getTextChannelById(channel.channelID());
                textChannel.sendMessage("If you can see this message, the updater works and just updated this feature into your bot instance!").queue();
            }catch (Exception ignored) {

            }
        }
        logger.info("Retrieved {} channels", channels.size());
        logger.info("Determining sendorders from history and guilds");

        //Wir benötigen: Channel, URL, Event
        Set<SendOrder> sendOrder = Bot.getDatabaseManager().getSendOrders();
        logger.info("Retrieved {} sendorders", sendOrder.size());
        logger.info("Sending messages");
        int messagesSent = 0;
        Collection<Long> skippedChannels = new ArrayList<>();
        for (SendOrder order : sendOrder) {
            try {
                if (DBChannel.collectionContains(channels, order.channel(), order.event())) {
                    TextChannel channel = Bot.getJda().getTextChannelById(order.channel());
                    if (channel == null) {
                        if (!skippedChannels.contains(order.channel())) {
                            logger.warn("Channel {} cant be found! Was it deleted?", order.channel());
                            skippedChannels.add(order.channel());
                        }
                        continue; //dont spam log
                    }
                    MessageEmbed messageEmbed = buildMessage(order.url(), order.name(), order.topic());
                    MessageCreateAction createAction = channel.sendMessageEmbeds(messageEmbed);
                    //Message ++
                    {

                    }

                    createAction.onSuccess(message -> {
                        Bot.getDatabaseManager().reportMessage(order.channel(), order.url(), order.event(), message.getIdLong());
                        logger.info("Sent message to channel {} for file ({}) {} ", order.channel(), order.name(), order.url());
                    }).queue();
                    messagesSent++;
                } else {
                    logger.warn("Potential corrupted data! Skipping order with channel {} for event {}", order.channel(), order.event());
                    continue;
                }
            } catch (Exception e) {
                logger.error("Failed to send message to channel {} for file ({}) {} ", order.channel(), order.name(), order.url());
                logger.error("Exception: ", e);
            }
        }

        return messagesSent;
    }

    @NotNull
    public Set<Item> parse(@NotNull String file) {
        final List<HTMLUtils.DataBlock> dataBlocks = HTMLUtils.extractDataBlocks(file);
        final Set<Item> items = new HashSet<>();
        for (HTMLUtils.DataBlock dataBlock : dataBlocks) {
            for (Pair<String, String> entry : dataBlock.getContent())
                items.add(new Item(dataBlock.getTopic(), entry.getFirst(), entry.getSecond()));
        }
        return items;
    }


    @NotNull
    protected MessageEmbed buildMessage(@NotNull String url, @NotNull String name, @NotNull String topic) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(name);
        embedBuilder.setUrl(url);
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        embedBuilder.setFooter(fileName);
        if (url.startsWith("https://video.fh-aachen.de/")) {
            fileName = "FH-Video: " + fileName;
            //download html from url
            URL target = null;
            try {
                target = new URL(url);

                String html = downloadHTML(target);
                //extract better name
                Pattern pattern = Pattern.compile("<meta name=\"description\" content=\".*?:(.*?)\".*?>", Pattern.DOTALL);
                Matcher matcher = pattern.matcher(html);
                if (matcher.find()) {
                    String betterTitle = matcher.group(1);
                    embedBuilder.setFooter("FH-Video: " + betterTitle);
                }
            } catch (IOException e) {
                logger.warn("Failed to download video info from {}", url);
            }
        }

        if (url.startsWith("https://www.hm-kompakt.de/")) {
            embedBuilder.setFooter("HM-Kompakt: " + fileName);
        }

        embedBuilder.setDescription(topic);
        {
            int color;
            if (url.endsWith(".pdf")) {
                color = 0xFFD700;
            } else if (url.endsWith(".docx")) {
                color = 0x0000FF;
            } else if (url.endsWith(".xlsx")) {
                color = 0x008000;
            } else if (url.endsWith(".pptx")) {
                color = 0x800080;
            } else if (url.endsWith(".ggb")) {
                color = 0x008000;
            } else {
                color = 0xFF0000;
            }
            embedBuilder.setColor(color);
        }
        //TODO: Add Videoinfo

        MessageEmbed build = embedBuilder.build();

        // build = experimentalVideoBuild("This is a video", url, name, 0x0000FF, fileName, new MessageEmbed.VideoInfo("https://video.fh-aachen.de/getMedium/dd36714825eee58cedfdb53b5c64b1e7.m4v", null, 640, 360));

        return build;
    }

    private String downloadHTML(URL target) throws IOException {
        InputStream downstream = target.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(downstream));
        String line;
        StringBuilder html = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            html.append(line).append("\n");
        }
        return html.toString();
    }


    private MessageEmbed experimentalVideoBuild(String description, @NotNull String url, @NotNull String title, int color, @NotNull String footer, @NotNull MessageEmbed.VideoInfo video) {

        if (description.length() > MessageEmbed.DESCRIPTION_MAX_LENGTH)
            throw new IllegalStateException(Helpers.format("Description is longer than %d! Please limit your input!", MessageEmbed.DESCRIPTION_MAX_LENGTH));
        return EntityBuilder.createMessageEmbed(url, title, description, EmbedType.RICH, null,
                color, null, null, null, video, new MessageEmbed.Footer(footer, null, null), null, new LinkedList<>());
    }
}
