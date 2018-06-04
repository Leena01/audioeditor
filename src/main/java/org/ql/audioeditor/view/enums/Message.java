package org.ql.audioeditor.view.enums;

/**
 * Messages shown by the GUI.
 */
public enum Message {
    LONG_SONG_ANALYZE_ERROR("This song is too long to analyze."),
    LONG_SONG_LOAD_ERROR("This song is too long."),
    SONGS_DELETED_INFO("Some songs were deleted from the database."),
    SUCCESSFUL_OPERATION_INFO("Successful operation."),
    WRONG_INPUT_ERROR("Wrong input data."),
    TIMEOUT_ERROR("Error: process took too long to finish."),
    INTERRUPTED_ERROR("Error: the process was interrupted."),
    FILE_TYPE_ERROR("This file type is not supported."),
    IMAGE_ERROR("Cannot load image."),
    SONG_TOO_SHORT_ERROR("The current song is too short to analyze."),
    TOO_MANY_FILES_ERROR("Too many files are selected.");

    private final String text;

    Message(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
