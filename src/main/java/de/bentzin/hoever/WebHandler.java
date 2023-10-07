package de.bentzin.hoever;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ture Bentzin
 * @since 07-10-2023
 */
public class WebHandler {
    @NotNull
    private final URL url;
    @NotNull
    private final Pattern pattern;
    private final Logger logger;

    public WebHandler(@NotNull URL url, @NotNull Pattern pattern) {
        this.url = url;
        this.pattern = pattern;
        logger = Logger.getLogger("WebHandler");
    }

    @NotNull
    public String[] scrape() {
        logger.info("Initiating scrape...");
        long m = System.currentTimeMillis();
        final String homepage = fetchHTML();
        long m1 = System.currentTimeMillis();
        logger.info("Received current homepage... " + dur(m, m1));
        String[] matchingSubstrings = findMatchingSubstrings(homepage, pattern);
        logger.info(homepage);
        long m2 = System.currentTimeMillis();
        logger.info("Found " + matchingSubstrings.length + " matches!");
        return matchingSubstrings;
    }

    @NotNull
    @Contract(pure = true)
    public static String dur(long l1, long l2) {
        return "Took: " + (l2 - l1) + " ms";
    }

    @NotNull
    public String fetchHTML() {
        StringBuilder htmlContent = new StringBuilder();

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    htmlContent.append(line);
                }
                reader.close();
            } else {
                System.out.println("Failed to fetch HTML. Response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return htmlContent.toString();
    }

    @NotNull
    public String[] findMatchingSubstrings(@NotNull Supplier<String> stringSupplier) {
        return findMatchingSubstrings(stringSupplier.get(), pattern);
    }

    @NotNull
    public String[] findMatchingSubstrings(@NotNull String input, Pattern pattern) {
        Matcher matcher = pattern.matcher(input);
        List<String> substringsList = new ArrayList<>();

        while (matcher.find()) {
            String matchingSubstring = matcher.group();
            substringsList.add(matchingSubstring);
        }

        String[] matchingSubstrings = new String[substringsList.size()];
        substringsList.toArray(matchingSubstrings);

        return matchingSubstrings;
    }
}
