package org.ql.audioeditor.common.util;

import java.io.File;

/**
 * General utility class.
 */
public final class GeneralUtils {
    private static final String PATH = new File("").getAbsolutePath();

    /**
     * Private constructor. May not be called.
     */
    private GeneralUtils() {
        throw new AssertionError();
    }

    /**
     * Returns the current path of the software.
     *
     * @return Path
     */
    public static String getPath() {
        return PATH;
    }

    /**
     * Gets the extension of a file.
     *
     * @param file File
     * @return Extension
     */
    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Gets the absolute path of a file's directory.
     *
     * @param path File path
     * @return Absolute path of directory
     */
    public static String getDir(String path) {
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null) {
            return parent.getAbsolutePath();
        } else {
            return path;
        }
    }

    /**
     * Converts a string to a number.
     *
     * @param numberString String to convert
     * @return Resulting number
     */
    public static int convertToNumber(String numberString) {
        if (!numberString.equals("")) {
            try {
                return Integer.parseInt(numberString);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
