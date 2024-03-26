package de.bentzin.hoever.command;

import de.bentzin.hoever.Bot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 26-03-2024
 */
public abstract class GCommand {
    @NotNull
    private final String description;
    public boolean admin;
    @NotNull
    private String name;

    public GCommand(@NotNull String name, @NotNull String description) {
        this.name = name;
        this.description = description;
        admin = false;
    }

    public GCommand(@NotNull String name, @NotNull String description, boolean admin) {
        this.name = name;
        this.admin = admin;
        this.description = description;
    }

    public void callCommand(@NotNull SlashCommandInteractionEvent event) {
        if (admin && !(Bot.isAdmin(event.getInteraction().getUser().getIdLong()))) {
            event.reply("You do not have permission to use this command!").setEphemeral(true).queue();
        } else {
            onSlashCommandInteraction(event);
        }

    }

    protected abstract void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event);

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @NotNull
    public SlashCommandData getCommandData() {
        return Commands.slash(name, description);
    }
}
