package view.util;
import java.awt.*;

public final class Constants {
    public static final String PLOT_IMAGE_NAME = "temp.png";
    public static final String SPEC_IMAGE_NAME = "temp2.png";
    public static final String SPEC_3D_IMAGE_NAME = "temp3.png";
    public static final String COVER_NAME = "resources/images/default-artwork.png";
    public static final String PLAY_ICON_NAME = "resources/images/play.png";
    public static final String PLAY_ICON_HOVER_NAME = "resources/images/play2.png";
    public static final String PAUSE_ICON_NAME = "resources/images/pause.png";
    public static final String PAUSE_ICON_HOVER_NAME = "resources/images/pause2.png";
    public static final String STOP_ICON_NAME = "resources/images/stop.png";
    public static final String STOP_ICON_HOVER_NAME = "resources/images/stop2.png";
    public static final String BACKWARD_ICON_NAME = "resources/images/backward.png";
    public static final String BACKWARD_ICON_HOVER_NAME = "resources/images/backward2.png";
    public static final String FAVORITE_ICON_NAME = "resources/images/heart_inactive.png";
    public static final String UNFAVORITE_ICON_NAME = "resources/images/heart_active.png";
    public static final String MINIMIZE_ICON_NAME = "resources/images/minimize.png";
    public static final String MAXIMIZE_ICON_NAME = "resources/images/maximize.png";
    public static final String NORMALIZE_ICON_NAME = "resources/images/normalize.png";
    public static final String UP_ICON_NAME = "resources/images/up.png";
    public static final String DOWN_ICON_NAME = "resources/images/down.png";
    public static final String CLOSE_ICON_NAME = "resources/images/close.png";

    public static final int GENRE_MIN = -1;
    public static final int GENRE_MAX = 191;

    public static final int BOTTOM_PANEL_HEIGHT = 30;
    public static final Dimension WIN_MIN_SIZE = new Dimension(800, 600);
    public static final Dimension WIN_MIN_SIZE_HIDDEN = new Dimension(800, 320);
    public static final Dimension COVER_SIZE = new Dimension(300, 300);
    public static final Dimension COVER_SIZE_MAX = new Dimension(480, 480);
    public static final Dimension BOTTOM_FIELD_SIZE = new Dimension(250, BOTTOM_PANEL_HEIGHT - 5);
    public static final Dimension DATA_LABEL_SIZE = new Dimension(250, 20);
    public static final Dimension AUDIO_SLIDER_SIZE = new Dimension(500, 50);
    public static final Dimension RANGE_SLIDER_SIZE = new Dimension(600, 100);
    public static final Dimension RANGE_SLIDER_SIZE_MAX = new Dimension(900, 100);
    public static final Dimension VOLUME_SLIDER_SIZE = new Dimension(80, 30);
    public static final Dimension LEVEL_SLIDER_SIZE = new Dimension(200, 30);
    public static final Dimension SPEC_IMAGE_SIZE = new Dimension(360, 270);
    public static final Dimension SPEC_IMAGE_SIZE_MAX = new Dimension(560, 420);

    public static final Object[] OPTIONS = {"Save as...", "Save", "Cancel"};
    public static String[] EXTENSION_NAMES =
            {"wav", "ogg", "flac", "au", "aiff", "aif", "aifc", "mp3", "m4a", "mp4"};
    public static final int DEFAULT_INIT_VAL = 0;
    public static final int REFRESH_MILLIS = 2000;
}
