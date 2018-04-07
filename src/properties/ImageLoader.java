package properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ImageLoader {
    private static Properties properties;
    private static InputStream inputStream;

    public static void init(String propFileName) throws IOException {
        properties = new Properties();
        inputStream = ConfigPropertiesLoader.class.getResourceAsStream(propFileName);
        if (inputStream != null)
            properties.load(inputStream);
        else
            throw new FileNotFoundException();
    }

    public static void close() throws IOException {
        if (inputStream != null)
            inputStream.close();
    }
}
