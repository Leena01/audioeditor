package org.ql.audioeditor.common.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public final class ConfigPropertiesLoader {
    private static Properties properties;
    private static InputStream inputStream;
    private static String driver;
    private static String jdbc;
    private static URL url;
    private static int refreshMillis;

    public static void init(String propFileName) throws IOException {
        properties = new Properties();
        inputStream =
            ConfigPropertiesLoader.class.getResourceAsStream(propFileName);
        if (inputStream != null)
            properties.load(inputStream);
        else
            throw new FileNotFoundException();

        driver = properties.getProperty("driver");
        jdbc = properties.getProperty("jdbc");
        url = ConfigPropertiesLoader.class
            .getResource(properties.getProperty("url"));
        refreshMillis =
            Integer.parseInt(properties.getProperty("refresh.millis"));
    }

    public static String getDriver() {
        return driver;
    }

    public static String getJdbc() {
        return jdbc;
    }

    public static URL getUrl() {
        return url;
    }

    public static int getRefreshMillis() {
        return refreshMillis;
    }

    public static void close() throws IOException {
        if (inputStream != null)
            inputStream.close();
    }
}
