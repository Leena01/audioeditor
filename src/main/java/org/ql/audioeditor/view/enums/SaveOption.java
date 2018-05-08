package org.ql.audioeditor.view.enums;

/**
 * Save options (dialog).
 */
public enum SaveOption {
    SAVE_AS("Save as..."),
    SAVE("Save"),
    CANCEL("Cancel");

    private final String text;

    SaveOption(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
