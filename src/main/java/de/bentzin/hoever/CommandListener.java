package de.bentzin.hoever;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.jetbrains.annotations.NotNull;


import java.util.Arrays;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author Ture Bentzin
 * @since 07-10-2023
 */
public class CommandListener extends ListenerAdapter {

    private final Logger logger = Logger.getLogger("Command");

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String[] scrape1 = Bot.webHandler.scrape();
        String scrape = Arrays.toString(scrape1);
        logger.info("Error#1: " + scrape);
        if(scrape.length() > 2000) {
            event.reply("Error #1").queue();

            return;
        }
        event.reply(scrape).queue();
    }
}
