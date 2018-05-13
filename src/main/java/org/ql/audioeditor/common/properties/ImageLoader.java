package org.ql.audioeditor.common.properties;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.ql.audioeditor.common.util.GeneralUtils.getPath;

/**
 * Image loader.
 */
public final class ImageLoader {
    private static final String PATH = getPath();
    private static Properties properties;
    private static String fullPath;
    private static InputStream inputStream;
    private static String plotImagePath;
    private static String specImagePath;
    private static String spec3dImagePath;
    private static String chromImagePath;
    private static String onsetImagePath;
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
     * @param propFileName Property file name
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

        fullPath = PATH + properties.getProperty("folder.generated");
        plotImagePath = fullPath + properties.getProperty("plot.image.name");
        specImagePath = fullPath + properties.getProperty("spec.image.name");
        spec3dImagePath = fullPath + properties.getProperty("spec.3d.image.name");
        chromImagePath = fullPath + properties.getProperty("chrom.image.name");
        onsetImagePath = fullPath + properties.getProperty("onset.image.name");
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

    /**
     * Returns the full path of the generated files' folder.
     * @return Path
     */
    public static String getFullPath() {
        return fullPath;
    }

    /**
     * Returns the path to the plot image.
     *
     * @return Path
     */
    public static String getPlotImagePath() {
        return plotImagePath;
    }

    /**
     * Returns the path to the spectrogram image.
     *
     * @return Path
     */
    public static String getSpecImagePath() {
        return specImagePath;
    }

    /**
     * Returns the path to the three-dimensional spectrogram image.
     *
     * @return Path
     */
    public static String getSpec3dImagePath() {
        return spec3dImagePath;
    }

    /**
     * Returns the path to the chromagram image.
     *
     * @return Path
     */
    public static String getChromImagePath() {
        return chromImagePath;
    }

    /**
     * Returns the path to the image showing the detected onsets.
     *
     * @return Path
     */
    public static String getOnsetImagePath() {
        return onsetImagePath;
    }

    /**
     * Returns the cover image.
     *
     * @return Cover
     */
    public static Image getCover() {
        return cover;
    }

    /**
     * Returns the icon for the play button.
     *
     * @return Play icon
     */
    public static Image getPlayIcon() {
        return playIcon;
    }

    /**
     * Returns the icon for the pause button.
     *
     * @return Pause icon
     */
    public static Image getPauseIcon() {
        return pauseIcon;
    }

    /**
     * Returns the icon for the stop button.
     *
     * @return Stop icon
     */
    public static Image getStopIcon() {
        return stopIcon;
    }

    /**
     * Returns the icon for the backward button.
     *
     * @return Backward icon
     */
    public static Image getBackwardIcon() {
        return backwardIcon;
    }

    /**
     * Returns the icon for the forward button.
     *
     * @return Forward icon
     */
    public static Image getForwardIcon() {
        return forwardIcon;
    }

    /**
     * Returns the icon for the sound-on button.
     *
     * @return Sound-on icon
     */
    public static Image getSoundOnIcon() {
        return soundOnIcon;
    }

    /**
     * Returns the icon for the sound-off button.
     *
     * @return Sound-off icon
     */
    public static Image getSoundOffIcon() {
        return soundOffIcon;
    }

    /**
     * Returns the icon for the previous button.
     *
     * @return Previous icon
     */
    public static Image getPreviousIcon() {
        return previousIcon;
    }

    /**
     * Returns the icon for the next button.
     *
     * @return Next icon
     */
    public static Image getNextIcon() {
        return nextIcon;
    }

    /**
     * Returns the icon for the favorite button.
     *
     * @return Favorite icon
     */
    public static Image getFavoriteIcon() {
        return favoriteIcon;
    }

    /**
     * Returns the icon for the unfavorite button.
     *
     * @return Unfavorite icon
     */
    public static Image getUnfavoriteIcon() {
        return unfavoriteIcon;
    }

    /**
     * Returns the icon for the playlist button.
     *
     * @return Playlist icon
     */
    public static Image getPlaylistIcon() {
        return playlistIcon;
    }

    /**
     * Returns the icon for the minimize button.
     *
     * @return Minimize icon
     */
    public static Image getMinimizeIcon() {
        return minimizeIcon;
    }

    /**
     * Returns the icon for the maximize button.
     *
     * @return Maximize icon
     */
    public static Image getMaximizeIcon() {
        return maximizeIcon;
    }

    /**
     * Returns the icon for the toggle button.
     *
     * @return Normalize icon
     */
    public static Image getNormalizeIcon() {
        return normalizeIcon;
    }

    /**
     * Returns the icon for the upward button.
     *
     * @return Upward icon
     */
    public static Image getUpwardIcon() {
        return upwardIcon;
    }

    /**
     * Returns the icon for the downward button.
     *
     * @return Downward icon
     */
    public static Image getDownwardIcon() {
        return downwardIcon;
    }

    /**
     * Returns the icon for the close button.
     *
     * @return Close icon
     */
    public static Image getCloseIcon() {
        return closeIcon;
    }

    /**
     * Closes the input stream.
     *
     * @throws IOException IOException
     */
    public static void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }
}
