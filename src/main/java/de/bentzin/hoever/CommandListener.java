package de.bentzin.hoever;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.jetbrains.annotations.NotNull;


import java.awt.*;
import java.util.Arrays;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author Ture Bentzin
 * @since 07-10-2023
 */
public class CommandListener extends ListenerAdapter {

    private final Logger logger = Logger.getLogger("Command");

    public boolean admin(GenericInteractionCreateEvent event) {
        return event.getInteraction().getUser().getIdLong() == Bot.getConfiguration().admin();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        logger.info(event.getUser().getName() + ": " + event.getCommandString());
        if(event.getName().equals("raw")) {
            String[] scrape1 = Bot.webHandler.scrape();
            String scrape = Arrays.toString(scrape1);
            logger.info("Error#1: " + scrape);
            if(scrape.length() > 2000) {
                event.reply("Error #1").queue();

                return;
            }
            event.reply(scrape).queue();
        } else if (event.getName().equals("update"))
        {
            logger.info("update triggered by command");
            ReplyCallbackAction replyCallbackAction = event.deferReply();
            replyCallbackAction.queue();
            int i = Bot.runCheck();
            event.getHook().editOriginal("Found " + i + " new uploads!").queue();

        } else if (event.getName().equals("exit") && admin(event)) {
            MessageEmbed sudo = new EmbedBuilder().setColor(Color.RED).setTitle("Admin / Debug").setDescription("The bot was marked for shutdown...").build();
            event.replyEmbeds(sudo).queue();
            Bot.getJda().shutdown();

        }else if(event.getName().equals("fake") && admin(event) && Bot.getConfiguration().operationMode() == OperationMode.TESTING) {
            OptionMapping url = event.getInteraction().getOption("url");
            if(url != null){
                event.reply("Creating fake data response...").queue();
                Bot.announceNewFile(url.getAsString());
            }

        }

    }
}
