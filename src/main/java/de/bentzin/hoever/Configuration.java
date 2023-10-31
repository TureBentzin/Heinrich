package de.bentzin.hoever;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @author Ture Bentzin
 * @since 07-10-2023
 */
public record Configuration(URL homepage, Pattern hrefPattern, long admin, OperationMode operationMode) {

    @NotNull
    public static Configuration configuration(@NotNull String homepage, @NotNull Pattern hrefPattern, long admin) throws MalformedURLException {
        return configuration(homepage, hrefPattern, admin, OperationMode.PRODUCTION);
    }

    @NotNull
    public static Configuration configuration(@NotNull String homepage, @NotNull Pattern hrefPattern, long admin, @NotNull OperationMode operationMode) throws MalformedURLException {
        return new Configuration(new URL(homepage), hrefPattern, admin, operationMode);
    }
}
