package de.bentzin.hoever.web;

import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ture Bentzin
 * @since 26-03-2024
 */
public class HTMLUtils {

    @NotNull
    public static final Logger logger = LoggerFactory.getLogger(DataManager.class);

    public static List<DataBlock> extractDataBlocks(@NotNull String html) {
        List<DataBlock> dataBlocks = new ArrayList<>();
        Pattern pattern = Pattern.compile("<div class=\"largeText\">(.*?)</div>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String blockContent = matcher.group(1);
            List<Pair<String, String>> urlsAndNames = extractNamesAndUrls(blockContent);
            String topic = extractTopic(blockContent);
            DataBlock dataBlock = new DataBlock(urlsAndNames, topic);
            dataBlocks.add(dataBlock);
        }
        return dataBlocks;
    }

    //"<a[ ]*href=\"https://(.*?)\".*?>([A-ZÄ-Üa-z0-9 .:-]*)</a>"gs
    //url,name
    public static List<Pair<String, String>> extractNamesAndUrls(@NotNull String blockContent) {
        List<Pair<String, String>> namesAndUrls = new ArrayList<>();
        //"<a[ ]*href=\"https://(.*?)\".*?>([A-ZÄ-Üa-z0-9 .:-]*)</a>"gs
        Pattern pattern = Pattern.compile("<a[ ]*href=\"https://(.*?)\".*?>([A-ZÄ-Üa-z0-9 .:-]*)</a>", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(blockContent);
        while (matcher.find()) {
            namesAndUrls.add(new Pair<>(matcher.group(1), matcher.group(2)));
        }
        return namesAndUrls;
    }

    private static String extractTopic(@NotNull String blockContent) {
        String topic = "";
        Pattern pattern = Pattern.compile("<li>([0-9A-Za-z :.,\t]*?)<ul>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(blockContent);
        while (matcher.find()) {
            String topicCandidate = matcher.group(1);
            //further validation probably needed
            // topic += " | " + topicCandidate;;
            return topicCandidate;
        }
        return topic;
    }

    public static void main(String[] args) throws IOException {

        InputStream downstream = new URL("https://www.fh-aachen.de/menschen/hoever/lehrveranstaltungen/hoehere-mathematik-1/wochenplaene-2023/24-hoehere-mathematik-1").openStream();
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(downstream));
        String line;
        StringBuilder event_file = new StringBuilder();
        while ((line = reader1.readLine()) != null) {
            event_file.append(line).append("\n");
        }
        String string = event_file.toString();

        logger.info(string);


        List<HTMLUtils.DataBlock> dataBlocks = HTMLUtils.extractDataBlocks(string);
        for (HTMLUtils.DataBlock dataBlock : dataBlocks) {
            logger.info("Topic: {}", dataBlock.getTopic());
            logger.info(dataBlock.getContent().toString());
        }
    }

    public static class DataBlock {
        @NotNull
        private final String topic;
        @NotNull
        private List<Pair<String, String>> content;

        public DataBlock(@NotNull List<Pair<String, String>> content, @NotNull String topic) {
            this.content = content;
            this.topic = topic;
        }

        @NotNull
        public List<Pair<String, String>> getContent() {
            return content;
        }

        public void setContent(@NotNull List<Pair<String, String>> content) {
            this.content = content;
        }

        @NotNull
        public String getTopic() {
            return topic;
        }

        @Override
        public String toString() {
            return "DataBlock{" +
                    "topic='" + topic + '\'' +
                    ", content=" + content +
                    '}';
        }
    }
}

