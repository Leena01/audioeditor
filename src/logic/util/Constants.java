package util;

import database.entities.Song;

public final class Constants {
    public static final Song LAST_OPEN_SONG = new Song();
    public static final int DEFAULT_SONG_ID = 0;
    public static final int EMPTY_SONG_ID = -1;
    public static final String DEFAULT = "-";
    public static final int REFRESH_MILLIS = 2000;

    public static String[] WINDOW_NAMES =
            {"Triangular", "Hann", "Hamming", "Blackman", "Blackman-Harris", "Flat top"};
    public static String[] EXTENSION_NAMES =
            {"wav", "ogg", "flac", "au", "aiff", "aif", "aifc", "mp3", "m4a", "mp4"};

}
