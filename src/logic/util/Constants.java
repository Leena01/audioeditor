package logic.util;

import database.entities.Song;

public final class Constants {
    public static final Song LAST_OPEN_SONG = new Song();
    public static final int DEFAULT_SONG_ID = 0;
    public static final int EMPTY_SONG_ID = -1;
    public static final String DEFAULT = "-";

    public static String[] WINDOW_NAMES =
            {"Triangular", "Hann", "Hamming", "Blackman", "Blackman-Harris", "Flat top"};

}
