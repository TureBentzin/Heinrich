package de.bentzin.hoever.command;

import de.bentzin.hoever.Bot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 26-03-2024
 */
public class SetChannelCommand extends GCommand {

    public SetChannelCommand() {
        super("setchannel", "Set the channel 1Hoever should use.");
    }

    @Override
    protected void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        Channel channel = event.getOption("channel").getAsChannel();
        String link = event.getOption("link").getAsString();
        Guild guild = event.getGuild();
        if (guild == null) {
            event.getInteraction().getHook().editOriginal("This command is only available in servers!").queue();
            return;
        }
        if (guild.getTextChannelById(channel.getId()) == null) {
            event.getInteraction().getHook().editOriginal("The channel is not in this server!").queue();
            return;
        }
        if (!link.startsWith("https://www.fh-aachen.de/menschen/hoever/lehrveranstaltungen/")) {
            event.getInteraction().getHook().editOriginal("Unsupported link!\nCheck this page to see what is available https://www.fh-aachen.de/menschen/hoever/lehrveranstaltungen").queue();
            return;
        }
        Bot.getDatabaseManager().setChannel(guild.getIdLong(), channel.getIdLong(), link);
        event.getInteraction().getHook().editOriginal("Channel set to " + channel.getName()).queue();
        logger.info("Set channel for {} to {} in guild {}", link, channel.getName(), guild.getName());

    }

    @NotNull
    @Override
    public SlashCommandData getCommandData() {
        return
                super.getCommandData()
                        .addOption(OptionType.CHANNEL,
                                "channel",
                                "The channel 1Hoever should use.",
                                true)
                        .addOption(OptionType.STRING,
                                "link",
                                "The link to the course \"Wochenplan page\" that should be used.", true);
    }

    @Override
    protected boolean checkAuthorized(@NotNull SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) return false;
        Member member = event.getGuild().getMember(event.getUser());
        return member != null && member.hasPermission(Permission.MANAGE_SERVER);
    }
}
