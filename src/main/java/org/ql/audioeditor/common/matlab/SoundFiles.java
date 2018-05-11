package org.ql.audioeditor.common.matlab;

import org.ql.audioeditor.common.properties.ConfigPropertiesLoader;

/**
 * List of sound files to use.
 */
public final class SoundFiles {
    private static final String SUBFOLDER = ConfigPropertiesLoader
        .getSoundFilesFolder() + "/";

    private static final String[] NOTES =
        {"Ab6", "G6", "Gb6", "F6", "E6", "Eb6", "D6", "Db6", "C6", "B5", "Bb5",
            "A5",
            "Ab5", "G5", "Gb5", "F5", "E5", "Eb5", "D5", "Db5", "C5", "B4",
            "Bb4", "A4",
            "Ab4", "G4", "Gb4", "F4", "E4", "Eb4", "D4", "Db4", "C4", "B3",
            "Bb3", "A3",
            "Ab3", "G3", "Gb3", "F3", "E3", "Eb3", "D3", "Db3", "C3", "B2",
            "Bb2", "A2"};

    private static final String[] NAME_PARTS = {"Piano.ff.", ".mp3"};

    /**
     * Private constructor. May not be called.
     */
    private SoundFiles() {
        throw new AssertionError();
    }

    /**
     * Returns a filename.
     *
     * @param ind The index of the element to return
     * @return Filename belonging to the given element in NOTES
     */
    public static String get(int ind) {
        return SUBFOLDER + NAME_PARTS[0] + NOTES[ind] + NAME_PARTS[1];
    }

    /**
     * Returns the number of elements present in NOTES.
     *
     * @return Number of notes
     */
    public static int size() {
        return NOTES.length;
    }
}
