package de.bentzin.hoever.web;

import de.bentzin.hoever.Bot;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    }

    @NotNull
    public Set<Item> parse(@NotNull String file) {
        List<HTMLUtils.DataBlock> dataBlocks = HTMLUtils.extractDataBlocks(file);
        for (HTMLUtils.DataBlock dataBlock : dataBlocks) {
            logger.info("Topic: {}", dataBlock.getTopic());
            logger.info(dataBlock.getContent().toString());
        }
        return Set.of();
    }
}
