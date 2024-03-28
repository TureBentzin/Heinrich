package de.bentzin.hoever.web;

import de.bentzin.hoever.Bot;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

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
            "https://www.hm-kompakt.de/"
    );

    public DataManager() {

    }

    public void update() {
        //Eventlink, File
        Map<String, String> index = new HashMap<>();
        for (URL event : Bot.getDatabaseManager().getEvents()) {
            logger.info("Downloading event: {}", event);
            try {
                InputStream downstream = event.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(downstream));
                String line;
                StringBuilder event_file = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    event_file.append(line).append("\n");
                }
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
        logger.info("Comparing items with history and sending messages");


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
}
