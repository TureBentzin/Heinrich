package de.bentzin.hoever;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 28-03-2024
 */
public record SendOrder(@NotNull String url, @NotNull String name, @NotNull String topic, @NotNull String event, @NotNull long channel) {
}
