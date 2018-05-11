package org.ql.audioeditor.common.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Song properties loader.
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
    private static int bpmMin;
    private static int bpmMax;
    private static int songRefreshMillis;
    private static int secondsToSkip;

    private SongPropertiesLoader() {
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
        bpmMin =
            Integer.parseInt(properties.getProperty("bpm.min"));
        bpmMax =
            Integer.parseInt(properties.getProperty("bpm.max"));
        songRefreshMillis =
            Integer.parseInt(properties.getProperty("song.refresh.millis"));
        secondsToSkip =
            Integer.parseInt(properties.getProperty("seconds.to.skip"));
    }

    /**
     * Returns the default song ID.
     *
     * @return Default song ID
     */
    public static int getDefaultSongId() {
        return defaultSongId;
    }

    /**
     * Returns the ID for empty songs.
     *
     * @return Empty song ID
     */
    public static int getEmptySongId() {
        return emptySongId;
    }

    /**
     * Returns the default song title.
     *
     * @return Default song title
     */
    public static String getDefaultTitle() {
        return defaultTitle;
    }

    /**
     * Returns the default track.
     *
     * @return Default track
     */
    public static String getDefaultTrack() {
        return defaultTrack;
    }

    /**
     * Returns the default artist.
     *
     * @return Default artist
     */
    public static String getDefaultArtist() {
        return defaultArtist;
    }

    /**
     * Returns the default album.
     *
     * @return Default album
     */
    public static String getDefaultAlbum() {
        return defaultAlbum;
    }

    /**
     * Returns the default year.
     *
     * @return Default year
     */
    public static String getDefaultYear() {
        return defaultYear;
    }

    /**
     * Returns the default genre.
     *
     * @return Default genre
     */
    public static String getDefaultGenre() {
        return defaultGenre;
    }

    /**
     * Returns the default comment.
     *
     * @return Default comment
     */
    public static String getDefaultComment() {
        return defaultComment;
    }

    /**
     * Returns the default song path.
     *
     * @return Default song path
     */
    public static String getDefaultPath() {
        return defaultPath;
    }

    /**
     * Returns the length over which it is not recommended to analyze a song (in
     * seconds).
     *
     * @return Maximum number of seconds
     */
    public static int getMaxSeconds() {
        return maxSeconds;
    }

    /**
     * Returns the name of the window functions used.
     *
     * @return Array of window names
     */
    public static String[] getWindowNames() {
        return windowNames;
    }

    /**
     * Returns the array of supported extensions.
     *
     * @return Array of supported extensions
     */
    public static String[] getExtensionNames() {
        return extensionNames;
    }

    /**
     * Returns the minimum volume.
     *
     * @return Minimum volume
     */
    public static int getVolumeMin() {
        return volumeMin;
    }

    /**
     * Returns the maximum volume.
     *
     * @return Maximum volume
     */
    public static int getVolumeMax() {
        return volumeMax;
    }

    /**
     * Returns the initial volume.
     *
     * @return Initial volume
     */
    public static int getVolumeInit() {
        return volumeInit;
    }

    /**
     * Returns the conversion rate between the symbolic and actual volume
     * levels.
     *
     * @return Conversion rate
     */
    public static float getVolumeConversionRate() {
        return volumeConversionRate;
    }

    /**
     * Returns the smallest genre number according to ID3v1.
     *
     * @return Smallest number
     */
    public static int getGenreMin() {
        return genreMin;
    }

    /**
     * Returns the smallest genre number according to ID3v1.
     *
     * @return Biggest number
     */
    public static int getGenreMax() {
        return genreMax;
    }

    /**
     * Returns the smallest sample rate allowed.
     *
     * @return Smallest sample rate
     */
    public static int getFrequencyMin() {
        return frequencyMin;
    }

    /**
     * Returns the biggest sample rate allowed.
     *
     * @return Biggest sample rate
     */
    public static int getFrequencyMax() {
        return frequencyMax;
    }

    /**
     * Returns the minimum BPM.
     *
     * @return Minimum BPM.
     */
    public static int getBpmMin() {
        return bpmMin;
    }

    /**
     * Returns the maximum BPM.
     *
     * @return Maximum BPM
     */
    public static int getBpmMax() {
        return bpmMax;
    }

    /**
     * Returns the interval at which the track slider should be refreshed (in
     * milliseconds).
     *
     * @return Interval in ms
     */
    public static int getSongRefreshMillis() {
        return songRefreshMillis;
    }

    /**
     * Returns the number of seconds to skip when fast-forwarding (or going
     * backwards).
     *
     * @return Interval in ms
     */
    public static int getSecondsToSkip() {
        return secondsToSkip;
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
