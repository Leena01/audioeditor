package org.ql.audioeditor.view.enums;

/**
 * Selectable attributes.
 */
public enum Attribute {
    ALL("Every song in order"),
    ARTIST("Artist"),
    ALBUM("Album"),
    GENRE("Genre");

    private final String text;

    Attribute(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
