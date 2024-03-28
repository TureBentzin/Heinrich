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
            List<String> urls = urlsAndNames.stream().map(Pair::getFirst).toList();
            List<String> names = urlsAndNames.stream().map(Pair::getSecond).toList();
            //List<String> urls = extractUrls(blockContent);
            //List<String> names = extractNames(blockContent);
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
        Pattern pattern = Pattern.compile("<a[ ]*href=\"https://.*?\".*?>([A-ZÄ-Üa-z0-9 .:-]*)</a>", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(blockContent);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
        return names;
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

 /*


        File file = new File("E:/WorkSpace/Ich versuche es nochmal/Ich versuche es nochmal/WORKSPACE 2020/Hoever/test-env/datatest/dataset.html");
        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        StringBuilder builder = new StringBuilder();
        int c;
        try {
            while ((c = reader.read()) != -1) {
                builder.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String string = builder.toString();


  */

        logger.info(string);


        List<HTMLUtils.DataBlock> dataBlocks = HTMLUtils.extractDataBlocks(string);
        for (HTMLUtils.DataBlock dataBlock : dataBlocks) {
            logger.info("Topic: {}", dataBlock.getTopic());
            logger.info(dataBlock.getNames().toString());
            logger.info(dataBlock.getUrls().toString());
        }
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
}

