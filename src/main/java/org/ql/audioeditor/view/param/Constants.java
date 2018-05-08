package org.ql.audioeditor.view.param;

import org.ql.audioeditor.view.enums.Attribute;
import org.ql.audioeditor.view.enums.SaveOption;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Class for constants related to the GUI.
 */
public final class Constants {
    public static final int BOTTOM_PANEL_HEIGHT = 32;
    public static final int MEDIA_CONTROL_PANEL_HEIGHT = 100;
    public static final Border ROOT_PANE_BORDER =
        BorderFactory.createLineBorder(Color.DARK_GRAY);
    public static final Border CURRENT_SONG_TITLE_BORDER =
        BorderFactory.createEmptyBorder(5, 5, 5, 5);
    public static final Border INFO_LABEL_BORDER =
        BorderFactory.createEmptyBorder(0, 12, 0, 12);
    public static final Border PLAYER_PANEL_BORDER =
        BorderFactory.createEmptyBorder(15, 15, 15, 15);
    public static final Dimension WIN_MIN_SIZE = new Dimension(800, 600);
    public static final Dimension WIN_MIN_SIZE_HIDDEN = new Dimension(800, 330);
    public static final Dimension COVER_SIZE = new Dimension(300, 300);
    public static final Dimension COVER_SIZE_MAX = new Dimension(450, 450);
    public static final Dimension BOTTOM_FIELD_SIZE =
        new Dimension(250, BOTTOM_PANEL_HEIGHT - 5);
    public static final Dimension DATA_LABEL_SIZE = new Dimension(250, 20);
    public static final Dimension AUDIO_SLIDER_SIZE = new Dimension(460, 50);
    public static final Dimension RANGE_SLIDER_SIZE = new Dimension(600, 100);
    public static final Dimension RANGE_SLIDER_SIZE_MAX =
        new Dimension(900, 100);
    public static final Dimension VOLUME_SLIDER_SIZE = new Dimension(140, 36);
    public static final Dimension LEVEL_SLIDER_SIZE = new Dimension(200, 30);
    public static final Dimension IMAGE_SIZE = new Dimension(360, 270);
    public static final Dimension IMAGE_SIZE_MAX = new Dimension(560, 420);
    public static final Object[] OPTIONS = SaveOption.values();
    public static final Object[] ATTRIBUTES = Attribute.values();
    public static final String BACK_TO_MAIN_MENU_TEXT = "Back to main menu";
    public static final int TEXT_FIELD_DIGIT_SIZE_MIN = 1;
    public static final int TEXT_FIELD_DIGIT_SIZE_MAX = 5;
    public static final int YEAR_DIGIT_SIZE = 4;

    private Constants() {
        throw new AssertionError();
    }
}
