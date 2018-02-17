package view.element.core.label;

import javax.swing.*;
import java.awt.*;

public class Label extends JLabel {
    private static final Color FONT_COLOR = Color.LIGHT_GRAY;
    public Label() {
        super();
        setForeground(FONT_COLOR);
    }

    public Label(String text) {
        super(text);
        setForeground(FONT_COLOR);
    }

    public Label(Icon image) {
        super(image);
        setForeground(FONT_COLOR);
    }

    public Label(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setForeground(FONT_COLOR);
    }
}
