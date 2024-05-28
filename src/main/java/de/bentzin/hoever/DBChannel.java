package de.bentzin.hoever;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author Ture Bentzin
 * @since 28-03-2024
 */
public record DBChannel(long guildID, long channelID, String eventLink) {
    @Contract(pure = true)
    public static boolean collectionContains(@NotNull Collection<DBChannel> collection, long channelID, @NotNull String eventLink) {
        boolean b = false;
        for (DBChannel dbChannel : collection) {
            if (dbChannel.channelID == channelID && dbChannel.eventLink.equals(eventLink)) {
                b = true;
                break;
            }
        }
        return b;
    }
}
