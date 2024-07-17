package de.bentzin.heinrich.command.heinrich;

import de.bentzin.heinrich.Bot;
import de.bentzin.heinrich.FaktsObject;
import de.bentzin.heinrich.command.GCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class FaktCommand extends GCommand {

    record FaktCache(List<String> data, long timestamp) {
    }

    int fail = 0;

    @Nullable
    private FaktCache cache = null;

    public FaktCommand() {
        super("fakt", "Erfahre einen Fakt von Heinrich");
    }

    @Override
    protected void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (fail > 3) {
            event.reply("This Command is currently not available").setEphemeral(true).queue();
            return;
        }
        Random rand = new Random();
        try {
            if (cache == null || System.currentTimeMillis() - cache.timestamp > Bot.getConfig().getFaktCacheTTL()) {
                List<String> fakts = Bot.getGsonManager().fromJson(new File(Bot.getConfig().getFaktsFile()), FaktsObject.class).getFakts();
                cache = new FaktCache(fakts, System.currentTimeMillis());
            }
        } catch (Exception e) {
            fail++;
            event.reply("Something went wrong!").setEphemeral(true).queue();
            logger.error("Error while loading fakts [" + fail + "/3]", e);

            return;
        }
        event.reply(cache.data.get(rand.nextInt(cache.data.size() - 1))).setEphemeral(false).queue();
        fail = 0;
    }
}
