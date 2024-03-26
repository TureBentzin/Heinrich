package de.bentzin.hoever.web;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author Ture Bentzin
 * @since 26-03-2024
 */
public abstract class DataSource {
    @NotNull
    public abstract Set<Item> fetchItems();
}
