package de.bentzin.hoever.web;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ture Bentzin
 * @since 26-03-2024
 */
public class HTMLUtils {
    public static List<DataBlock> extractDataBlocks(@NotNull String html) {
        List<DataBlock> dataBlocks = new ArrayList<>();
        Pattern pattern = Pattern.compile("<div class=\"largeText\">(.*?)</div>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String blockContent = matcher.group(1);
            List<String> urls = extractUrls(blockContent);
            List<String> names = extractNames(blockContent);
            String topic = extractTopic(blockContent);
            DataBlock dataBlock = new DataBlock(urls, names, topic);
            dataBlocks.add(dataBlock);
        }
        return dataBlocks;
    }

    private static List<String> extractUrls(@NotNull String blockContent) {
        List<String> urls = new ArrayList<>();
        Pattern pattern = Pattern.compile("<a\\s+href=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(blockContent);
        while (matcher.find()) {
            urls.add(matcher.group(1));
        }
        return urls;
    }

    private static List<String> extractNames(@NotNull String blockContent) {
        List<String> names = new ArrayList<>();
        Pattern pattern = Pattern.compile("<a\\s+href=\"[^\"]*\">([^<]*)</a>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(blockContent);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
        return names;
    }

    private static String extractTopic(@NotNull String blockContent) {
        Pattern pattern = Pattern.compile("<li>(.*?)</li>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(blockContent);
        while (matcher.find()) {
            String topicCandidate = matcher.group(1);
            //further validation probably needed
            return topicCandidate;
        }
        return null;
    }

    public static class DataBlock {
        @NotNull
        private List<String> urls;
        @NotNull
        private List<String> names;
        @NotNull
        private String topic;

        public DataBlock(@NotNull List<String> urls, @NotNull List<String> names, @NotNull String topic) {
            this.urls = urls;
            this.names = names;
            this.topic = topic;
        }

        @NotNull
        public List<String> getUrls() {
            return urls;
        }

        @NotNull
        public List<String> getNames() {
            return names;
        }

        @NotNull
        public String getTopic() {
            return topic;
        }
    }


    public static void main(String[] args) {

        List<HTMLUtils.DataBlock> dataBlocks = HTMLUtils.extractDataBlocks();
        for (HTMLUtils.DataBlock dataBlock : dataBlocks) {
            logger.info("Topic: {}", dataBlock.getTopic());
            logger.info(dataBlock.getNames().toString());
            logger.info(dataBlock.getUrls().toString());
        }
    }
}

