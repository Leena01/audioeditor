package org.ql.audioeditor.common.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public final class ImageLoader {
    private static Properties properties;
    private static InputStream inputStream;
    private static URL plotImageURL;
    private static URL specImageURL;
    private static URL spec3dImageURL;
    private static URL chromImageURL;
    private static URL coverURL;
    private static URL playIconURL;
    private static URL pauseIconURL;
    private static URL stopIconURL;
    private static URL backwardIconURL;
    private static URL forwardIconURL;
    private static URL favoriteIconURL;
    private static URL unfavoriteIconURL;
    private static URL soundOnIconURL;
    private static URL soundOffIconURL;
    private static URL minimizeIconURL;
    private static URL maximizeIconURL;
    private static URL normalizeIconURL;
    private static URL upwardIconURL;
    private static URL downwardIconURL;
    private static URL closeIconURL;

    public static void init(String propFileName) throws IOException {
        properties = new Properties();
        inputStream = ConfigPropertiesLoader.class.getResourceAsStream(propFileName);
        if (inputStream != null)
            properties.load(inputStream);
        else
            throw new FileNotFoundException();

        plotImageURL = ImageLoader.class.getResource(
                properties.getProperty("plot.image.name"));
        specImageURL = ImageLoader.class.getResource(
                properties.getProperty("spec.image.name"));
        spec3dImageURL = ImageLoader.class.getResource(
                properties.getProperty("spec.3d.image.name"));
        chromImageURL = ImageLoader.class.getResource(
                properties.getProperty("chrom.image.name"));
        coverURL = ImageLoader.class.getResource(
                properties.getProperty("cover.name"));
        playIconURL = ImageLoader.class.getResource(
                properties.getProperty("play.icon.name"));
        pauseIconURL = ImageLoader.class.getResource(
                properties.getProperty("pause.icon.name"));
        stopIconURL = ImageLoader.class.getResource(
                properties.getProperty("stop.icon.name"));
        backwardIconURL = ImageLoader.class.getResource(
                properties.getProperty("backward.icon.name"));
        forwardIconURL = ImageLoader.class.getResource(
                properties.getProperty("forward.icon.name"));
        favoriteIconURL = ImageLoader.class.getResource(
                properties.getProperty("favorite.icon.name"));
        unfavoriteIconURL = ImageLoader.class.getResource(
                properties.getProperty("unfavorite.icon.name"));
        soundOnIconURL = ImageLoader.class.getResource(
                properties.getProperty("sound.on.icon.name"));
        soundOffIconURL = ImageLoader.class.getResource(
                properties.getProperty("sound.off.icon.name"));
        minimizeIconURL = ImageLoader.class.getResource(
                properties.getProperty("minimize.icon.name"));
        maximizeIconURL = ImageLoader.class.getResource(
                properties.getProperty("maximize.icon.name"));
        normalizeIconURL = ImageLoader.class.getResource(
                properties.getProperty("normalize.icon.name"));
        upwardIconURL = ImageLoader.class.getResource(
                properties.getProperty("upward.icon.name"));
        downwardIconURL = ImageLoader.class.getResource(
                properties.getProperty("downward.icon.name"));
        closeIconURL = ImageLoader.class.getResource(
                properties.getProperty("close.icon.name"));
    }

    public static URL getPlotImageURL() {
        return plotImageURL;
    }

    public static URL getSpecImageURL() {
        return specImageURL;
    }

    public static URL getSpec3dImageURL() {
        return spec3dImageURL;
    }

    public static URL getChromImageURL() {
        return chromImageURL;
    }

    public static URL getCoverURL() {
        return coverURL;
    }

    public static URL getPlayIconURL() {
        return playIconURL;
    }

    public static URL getPauseIconURL() {
        return pauseIconURL;
    }

    public static URL getStopIconURL() {
        return stopIconURL;
    }

    public static URL getBackwardIconURL() {
        return backwardIconURL;
    }

    public static URL getForwardIconURL() {
        return forwardIconURL;
    }

    public static URL getFavoriteIconURL() {
        return favoriteIconURL;
    }

    public static URL getUnfavoriteIconURL() {
        return unfavoriteIconURL;
    }

    public static URL getSoundOnIconURL() {
        return soundOnIconURL;
    }

    public static URL getSoundOffIconURL() {
        return soundOffIconURL;
    }

    public static URL getMinimizeIconURL() {
        return minimizeIconURL;
    }

    public static URL getMaximizeIconURL() {
        return maximizeIconURL;
    }

    public static URL getNormalizeIconURL() {
        return normalizeIconURL;
    }

    public static URL getUpwardIconURL() {
        return upwardIconURL;
    }

    public static URL getDownwardIconURL() {
        return downwardIconURL;
    }

    public static URL getCloseIconURL() {
        return closeIconURL;
    }

    public static void close() throws IOException {
        if (inputStream != null)
            inputStream.close();
    }
}
