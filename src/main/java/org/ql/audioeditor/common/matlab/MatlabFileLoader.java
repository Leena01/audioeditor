package org.ql.audioeditor.common.matlab;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;
import static org.ql.audioeditor.common.util.Helper.getPath;

/**
 * MATLAB file loader.
 */
public final class MatlabFileLoader {
    private static final String SUBFOLDER = "/matlab/";
    private static final String PATH = getPath() + SUBFOLDER;

    private MatlabFileLoader() {
        throw new AssertionError();
    }

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
