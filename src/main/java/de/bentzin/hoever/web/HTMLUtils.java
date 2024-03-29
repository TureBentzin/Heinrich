package de.bentzin.hoever.web;

import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.bentzin.hoever.web.DataManager.allowedHosts;

/**
 * @author Ture Bentzin
 * @since 26-03-2024
 */
public class HTMLUtils {

    @NotNull
    public static final Logger logger = LoggerFactory.getLogger(DataManager.class);

    @NotNull
    public static List<DataBlock> extractDataBlocks(@NotNull String html) {
        List<DataBlock> dataBlocks = new ArrayList<>();
        Pattern pattern = Pattern.compile("<header>.*?<h2 class=\"\">.*?:(.*?)</h2>.*?</header>.*?<div class=\"(shortText|largeText)\">(.*?)</div>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String blockContent = matcher.group(3);
            String header = matcher.group(1);
            logger.info("Header: {}", header);
            logger.info("Block: {}", blockContent);
            List<Pair<String, String>> urlsAndNames = extractNamesAndUrls(blockContent);
            String topic = matcher.group(1).replace("\n", "");
            DataBlock dataBlock = new DataBlock(urlsAndNames, topic);
            dataBlocks.add(dataBlock);
        }
        return dataBlocks;
    }

    //"<a[ ]*href=\"https://(.*?)\".*?>([A-ZÄ-Üa-z0-9 .:-]*)</a>"gs
    //url,name
    @NotNull
    public static List<Pair<String, String>> extractNamesAndUrls(@NotNull String blockContent) {
        List<Pair<String, String>> namesAndUrls = new ArrayList<>();
        //"<a[ ]*href=\"https://(.*?)\".*?>([A-ZÄ-Üa-z0-9 .:-]*)</a>"gs
        Pattern pattern = Pattern.compile("<a[ ]*href=\"(https://.*?)\".*?>([A-ZÄ-Üä-üa-z0-9 .:-]*)</a>", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(blockContent);
        while (matcher.find()) {
            {
                //checks if the url is allowed
                String url = matcher.group(1);
                boolean allowed = false;
                for (String allowedHost : allowedHosts) {
                    if (url.startsWith(allowedHost)) {
                        allowed = true;
                        break;
                    }
                }
                if (!allowed) {
                    logger.warn("URL not allowed: {}", url);
                    continue;
                }

            }
            namesAndUrls.add(new Pair<>(matcher.group(1), matcher.group(2)));
        }
        return namesAndUrls;
    }

    private static String extractTopic(@NotNull String blockContent) {
        String topic = "";
        blockContent = blockContent.replace("\n", "");
        Pattern pattern = Pattern.compile("<h2 class=\"\">.*?:(.*?)</h2>", Pattern.DOTALL); //big header
        Matcher matcher = pattern.matcher(blockContent);
        while (matcher.find()) {
            String topicCandidate = matcher.group(1);
            //further validation probably needed
            // topic += " | " + topicCandidate;;
            return topicCandidate;
        }


        return topic;
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

