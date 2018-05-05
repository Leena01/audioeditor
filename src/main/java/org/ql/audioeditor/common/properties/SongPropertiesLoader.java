package org.ql.audioeditor.common.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Song properties loader (utility class).
 */
public final class SongPropertiesLoader {
    private static Properties properties;
    private static InputStream inputStream;
    private static int defaultSongId;
    private static int emptySongId;
    private static String defaultTitle;
    private static String defaultTrack;
    private static String defaultArtist;
    private static String defaultAlbum;
    private static String defaultYear;
    private static String defaultGenre;
    private static String defaultComment;
    private static String defaultPath;
    private static int maxSeconds;
    private static String[] windowNames;
    private static String[] extensionNames;
    private static int volumeMin;
    private static int volumeMax;
    private static int volumeInit;
    private static float volumeConversionRate;
    private static int genreMin;
    private static int genreMax;
    private static int frequencyMin;
    private static int frequencyMax;
    private static int songRefreshMillis;
    private static int secondsToSkip;

    private SongPropertiesLoader() {
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

        defaultSongId =
            Integer.parseInt(properties.getProperty("default.song.id"));
        emptySongId = Integer.parseInt(properties.getProperty("empty.song.id"));
        defaultTitle = properties.getProperty("default.title");
        defaultTrack = properties.getProperty("default.track");
        defaultArtist = properties.getProperty("default.artist");
        defaultAlbum = properties.getProperty("default.album");
        defaultYear = properties.getProperty("default.year");
        defaultGenre = properties.getProperty("default.genre");
        defaultComment = properties.getProperty("default.comment");
        defaultPath = properties.getProperty("default.path");
        maxSeconds = Integer.parseInt(properties.getProperty("max.seconds"));
        windowNames = properties.getProperty("window.names").split(",");
        extensionNames = properties.getProperty("extension.names").split(",");
        volumeMin = Integer.parseInt(properties.getProperty("volume.min"));
        volumeMax = Integer.parseInt(properties.getProperty("volume.max"));
        volumeInit = Integer.parseInt(properties.getProperty("volume.init"));
        volumeConversionRate =
            Float.parseFloat(properties.getProperty("volume.conversion.rate"));
        genreMin = Integer.parseInt(properties.getProperty("genre.min"));
        genreMax = Integer.parseInt(properties.getProperty("genre.max"));
        frequencyMin =
            Integer.parseInt(properties.getProperty("frequency.min"));
        frequencyMax =
            Integer.parseInt(properties.getProperty("frequency.max"));
        songRefreshMillis =
            Integer.parseInt(properties.getProperty("song.refresh.millis"));
        secondsToSkip =
            Integer.parseInt(properties.getProperty("seconds.to.skip"));
    }

    public static int getDefaultSongId() {
        return defaultSongId;
    }

    public static int getEmptySongId() {
        return emptySongId;
    }

    public static String getDefaultTitle() {
        return defaultTitle;
    }

    public static String getDefaultTrack() {
        return defaultTrack;
    }

    public static String getDefaultArtist() {
        return defaultArtist;
    }

    public static String getDefaultAlbum() {
        return defaultAlbum;
    }

    public static String getDefaultYear() {
        return defaultYear;
    }

    public static String getDefaultGenre() {
        return defaultGenre;
    }

    public static String getDefaultComment() {
        return defaultComment;
    }

    public static String getDefaultPath() {
        return defaultPath;
    }

    public static int getMaxSeconds() {
        return maxSeconds;
    }

    public static String[] getWindowNames() {
        return windowNames;
    }

    public static String[] getExtensionNames() {
        return extensionNames;
    }

    public static int getVolumeMin() {
        return volumeMin;
    }

    public static int getVolumeMax() {
        return volumeMax;
    }

    public static int getVolumeInit() {
        return volumeInit;
    }

    public static float getVolumeConversionRate() {
        return volumeConversionRate;
    }

    public static int getGenreMin() {
        return genreMin;
    }

    public static int getGenreMax() {
        return genreMax;
    }

    public static int getFrequencyMin() {
        return frequencyMin;
    }

    public static int getFrequencyMax() {
        return frequencyMax;
    }

    public static int getSongRefreshMillis() {
        return songRefreshMillis;
    }

    public static int getSecondsToSkip() {
        return secondsToSkip;
    }

    public static void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }
}
