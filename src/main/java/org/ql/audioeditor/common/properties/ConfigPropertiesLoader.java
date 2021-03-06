package org.ql.audioeditor.common.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Config properties loader.
 */
public final class ConfigPropertiesLoader {
    private static Properties properties;
    private static InputStream inputStream;
    private static String driver;
    private static String jdbc;
    private static String url;
    private static String matlabFolder;
    private static String soundFilesFolder;
    private static int refreshMillis;
    private static int timeoutSeconds;

    private ConfigPropertiesLoader() {
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

        driver = properties.getProperty("driver");
        jdbc = properties.getProperty("jdbc");
        url = properties.getProperty("url");
        matlabFolder = properties.getProperty("matlab.folder");
        soundFilesFolder = properties.getProperty("sound.files.folder");
        refreshMillis =
            Integer.parseInt(properties.getProperty("refresh.millis"));
        timeoutSeconds = Integer
            .parseInt(properties.getProperty("timeout.seconds"));
    }

    /**
     * Returns the driver.
     *
     * @return Driver
     */
    public static String getDriver() {
        return driver;
    }

    /**
     * Returns the beginning of the JDBC path.
     *
     * @return JDBC path
     */
    public static String getJdbc() {
        return jdbc;
    }

    /**
     * Returns the URL of the database.
     *
     * @return URL
     */
    public static String getUrl() {
        return url;
    }

    /**
     * Returns the path of the subfolder in which the MATLAB files are stored.
     *
     * @return Folder path
     */
    public static String getMatlabFolder() {
        return matlabFolder;
    }

    /**
     * Returns the path of the subfolder in which the sound files are stored.
     *
     * @return Folder path
     */
    public static String getSoundFilesFolder() {
        return soundFilesFolder;
    }

    /**
     * Returns the interval at which the database should be refreshed (in
     * milliseconds).
     *
     * @return Interval in ms
     */
    public static int getRefreshMillis() {
        return refreshMillis;
    }

    /**
     * Returns the timeout (in seconds). Any ongoing process is interrupted if
     * this time period is exceeded.
     *
     * @return The period of time allowed
     */
    public static int getTimeoutSeconds() {
        return timeoutSeconds;
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
