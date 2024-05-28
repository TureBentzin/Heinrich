package de.bentzin.hoever.web;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * @since 26-03-2024
 */
public class Item {
    @NotNull
    private String name;
    @NotNull
    private String link;
    @Nullable
    private String topic;

    public Item(@NotNull String topic, @NotNull String link, @Nullable String name) {
        this.name = name;
        this.link = link;
        this.topic = topic;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getLink() {
        return link;
    }

    public void setLink(@NotNull String link) {
        this.link = link;
    }

    @Nullable
    public String getTopic() {
        return topic;
    }

    public void setTopic(@Nullable String topic) {
        this.topic = topic;
    }
}
