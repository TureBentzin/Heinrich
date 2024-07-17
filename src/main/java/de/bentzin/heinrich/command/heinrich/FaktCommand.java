package de.bentzin.heinrich.command.heinrich;

import de.bentzin.heinrich.command.GCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class FaktCommand extends GCommand {

    public FaktCommand() {
        super("fakt", "Erfahre einen Fakt von Heinrich");
    }

    @Override
    protected void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

    }
}
