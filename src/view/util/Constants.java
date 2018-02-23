package view.util;

import javax.swing.*;
import java.awt.*;

import static util.Utils.resizeImageIcon;

public final class Constants {
    public static final String PLOT_IMAGE_NAME = "temp.png";
    public static final String COVER_NAME = "resources/images/default-artwork.png";
    public static final String PLAY_ICON_NAME = "resources/images/play.png";
    public static final String PLAY_ICON_HOVER_NAME = "resources/images/play2.png";
    public static final String PAUSE_ICON_NAME = "resources/images/pause.png";
    public static final String PAUSE_ICON_HOVER_NAME = "resources/images/pause2.png";
    public static final String STOP_ICON_NAME = "resources/images/stop.png";
    public static final String STOP_ICON_HOVER_NAME = "resources/images/stop2.png";
    // public static final ImageIcon BACKWARD_ICON_NAME = "resources/images/backward.png";
    // public static final ImageIcon BACKWARD_ICON_HOVER_NAME = "resources/images/backward2.png";
    public static final String FAVORITE_ICON_NAME = "resources/images/heart.png";
    public static final String UNFAVORITE_ICON_NAME = "resources/images/heart_red.png";
    public static final String MINIMIZE_ICON_NAME = "resources/images/minimize.png";
    public static final String MAXIMIZE_ICON_NAME = "resources/images/maximize.png";
    public static final String NORMALIZE_ICON_NAME = "resources/images/normalize.png";
    public static final String UP_ICON_NAME = "resources/images/up.png";
    public static final String DOWN_ICON_NAME = "resources/images/down.png";
    public static final String CLOSE_ICON_NAME = "resources/images/close.png";
    public static final String DEFAULT_FIELD = "-";

    public static final int BOTTOM_PANEL_HEIGHT = 30;
    public static final Dimension WIN_MIN_SIZE = new Dimension(850, 600);
    public static final Dimension WIN_MIN_SIZE_HIDDEN = new Dimension(850, 250);
    public static final Dimension COVER_SIZE = new Dimension(1000, 1000);
    public static final Dimension BOTTOM_FIELD_SIZE = new Dimension(250, BOTTOM_PANEL_HEIGHT - 6);
    public static final Dimension AUDIO_SLIDER_SIZE = new Dimension(500, 50);
}