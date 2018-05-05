package org.ql.audioeditor.common.properties;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.ql.audioeditor.common.util.Helper.getPath;

/**
 * Image loader (utility class).
 */
public final class ImageLoader {
    private static final String PATH = getPath();
    private static Properties properties;
    private static InputStream inputStream;
    private static String plotImagePath;
    private static String specImagePath;
    private static String spec3dImagePath;
    private static String chromImagePath;
    private static Image cover;
    private static Image playIcon;
    private static Image pauseIcon;
    private static Image stopIcon;
    private static Image backwardIcon;
    private static Image forwardIcon;
    private static Image soundOnIcon;
    private static Image soundOffIcon;
    private static Image previousIcon;
    private static Image nextIcon;
    private static Image favoriteIcon;
    private static Image unfavoriteIcon;
    private static Image playlistIcon;
    private static Image minimizeIcon;
    private static Image maximizeIcon;
    private static Image normalizeIcon;
    private static Image upwardIcon;
    private static Image downwardIcon;
    private static Image closeIcon;

    private ImageLoader() {
        throw new AssertionError();
    }

    /**
     * Initialization.
     *
     * @throws IOException IOException
     */
    public static void init(String propFileName) throws IOException {
        properties = new Properties();
        inputStream =
            ConfigPropertiesLoader.class.getResourceAsStream(propFileName);
        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException();
        }

        plotImagePath = PATH + properties.getProperty("plot.image.name");
        specImagePath = PATH + properties.getProperty("spec.image.name");
        spec3dImagePath = PATH + properties.getProperty("spec.3d.image.name");
        chromImagePath = PATH + properties.getProperty("chrom.image.name");
        cover = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("cover.name")));
        playIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("play.icon.name")));
        pauseIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("pause.icon.name")));
        stopIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("stop.icon.name")));
        backwardIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("backward.icon.name")));
        forwardIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("forward.icon.name")));
        favoriteIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("favorite.icon.name")));
        soundOnIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("sound.on.icon.name")));
        soundOffIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("sound.off.icon.name")));
        previousIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("previous.icon.name")));
        nextIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("next.icon.name")));
        unfavoriteIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("unfavorite.icon.name")));
        playlistIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("playlist.icon.name")));
        minimizeIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("minimize.icon.name")));
        maximizeIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("maximize.icon.name")));
        normalizeIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("normalize.icon.name")));
        upwardIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("upward.icon.name")));
        downwardIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("downward.icon.name")));
        closeIcon = ImageIO.read(ImageLoader.class.getResourceAsStream(
            properties.getProperty("close.icon.name")));
    }

    public static String getPlotImagePath() {
        return plotImagePath;
    }

    public static String getSpecImagePath() {
        return specImagePath;
    }

    public static String getSpec3dImagePath() {
        return spec3dImagePath;
    }

    public static String getChromImagePath() {
        return chromImagePath;
    }

    public static Image getCover() {
        return cover;
    }

    public static Image getPlayIcon() {
        return playIcon;
    }

    public static Image getPauseIcon() {
        return pauseIcon;
    }

    public static Image getStopIcon() {
        return stopIcon;
    }

    public static Image getBackwardIcon() {
        return backwardIcon;
    }

    public static Image getForwardIcon() {
        return forwardIcon;
    }

    public static Image getSoundOnIcon() {
        return soundOnIcon;
    }

    public static Image getSoundOffIcon() {
        return soundOffIcon;
    }

    public static Image getPreviousIcon() {
        return previousIcon;
    }

    public static Image getNextIcon() {
        return nextIcon;
    }

    public static Image getFavoriteIcon() {
        return favoriteIcon;
    }

    public static Image getUnfavoriteIcon() {
        return unfavoriteIcon;
    }

    public static Image getPlaylistIcon() {
        return playlistIcon;
    }

    public static Image getMinimizeIcon() {
        return minimizeIcon;
    }

    public static Image getMaximizeIcon() {
        return maximizeIcon;
    }

    public static Image getNormalizeIcon() {
        return normalizeIcon;
    }

    public static Image getUpwardIcon() {
        return upwardIcon;
    }

    public static Image getDownwardIcon() {
        return downwardIcon;
    }

    public static Image getCloseIcon() {
        return closeIcon;
    }

    public static void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }
}
