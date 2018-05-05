package org.ql.audioeditor.common.matlab;

import org.ql.audioeditor.common.properties.ConfigPropertiesLoader;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;
import static org.ql.audioeditor.common.util.GeneralUtils.getPath;

/**
 * MATLAB file loader.
 */
public final class MatlabFileLoader {
    private static final String SUBFOLDER = "/" + ConfigPropertiesLoader
        .getMatlabFolder() + "/";
    private static final String PATH = getPath() + SUBFOLDER;

    private MatlabFileLoader() {
        throw new AssertionError();
    }

    /**
     * Initialization.
     *
     * @throws IOException IOException
     */
    public static void init() throws IOException {
        for (MatlabFiles f : MatlabFiles.values()) {
            copyFile(f.toString());
        }
    }

    private static void copyFile(String file) throws IOException {
        copyInputStreamToFile(
            MatlabFileLoader.class.getResourceAsStream(SUBFOLDER + file),
            new File(PATH + file));
    }
}
