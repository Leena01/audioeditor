package view.util;

import javax.swing.*;
import java.awt.*;

import static util.Utils.resizeImageIcon;

public final class Constants {
    public static final String PLOT_IMAGE_NAME = "temp.png";
    public static final String COVER_NAME = "resources/images/default-artwork.png";
    public static final String DEFAULT_FIELD = "-";

    public static final Dimension WIN_MIN_SIZE = new Dimension(800, 400);
    private static final Dimension COVER_SIZE = new Dimension(200, 200);
    public static final int BOTTOM_PANEL_HEIGHT = 30;
    public static final Dimension BOTTOM_FIELD_SIZE = new Dimension(250, BOTTOM_PANEL_HEIGHT - 6);

    public static final ImageIcon COVER_IMAGE = resizeImageIcon(new ImageIcon(COVER_NAME), COVER_SIZE);
}
