package de.bentzin.hoever;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 28-03-2024
 */
public record SendOrder(@NotNull String url, @NotNull String event, @NotNull String channel) {
}
