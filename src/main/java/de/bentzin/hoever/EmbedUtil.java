package de.bentzin.hoever;

import com.google.common.base.Preconditions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Ture Bentzin
 * @since 31-10-2023
 */
public class EmbedUtil {

    private EmbedUtil() {

    }

    @NotNull
    private static MessageEmbed.Provider provider(@NotNull String url) {
        return new MessageEmbed.Provider("Georg Hoever", url);
    }

    @NotNull
    public static MessageEmbed process(@NotNull String url, @NotNull Color color) {
        URL fileURl = null;
        try {
            fileURl = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String filename = fileURl.getFile();
        //filename = filename.substring(0, filename.length() - 2);
        String[] pathSegments = filename.split("/");
        String extractedFilename = pathSegments[pathSegments.length - 1];

        if(extractedFilename.endsWith(".mp4")) {
            //VIDEO
            return video(url, extractedFilename, Color.RED);
        }
        if(extractedFilename.endsWith(".jpg")) {
            return image(url, extractedFilename, Color.BLUE);
        }
        return new EmbedBuilder()
                .setTitle(extractedFilename)
                .setUrl(url)
                .setColor(color)
                .setAuthor("1Hoever")
                .build();
    }

    @NotNull
    protected static MessageEmbed video(@NotNull String videoURL, @NotNull String filename, @NotNull Color color) {
        return new MessageEmbed(videoURL,
                filename,
                null,
                EmbedType.VIDEO,
                null,
                color.getRGB(),
                new MessageEmbed.Thumbnail(videoURL, null, 1029, 1080),
                provider(videoURL),
                new MessageEmbed.AuthorInfo("1Hoever", null, null, null),
                new MessageEmbed.VideoInfo(videoURL, 1029, 1080),
                null,
                null,
                null
        );
    }

    @NotNull
    protected static MessageEmbed image(@NotNull String imageURL, @NotNull String filename, @NotNull Color color) {
        return new MessageEmbed(imageURL,
                filename,
                null,
                EmbedType.IMAGE,
                null,
                color.getRGB(),
                null,
                provider(imageURL),
                new MessageEmbed.AuthorInfo("1Hoever", null, null, null),
                null,
                null,
                new MessageEmbed.ImageInfo(imageURL,null,1029, 1080),
                null
        );
    }
}
