package org.ql.audioeditor.view.core.label;

import javax.swing.Icon;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Label.
 */
public class Label extends JLabel {
    private static final Color FONT_COLOR = Color.LIGHT_GRAY;

    /**
     * Constructor.
     */
    public Label() {
        super();
        setForeground(FONT_COLOR);
    }

    /**
     * Constructor.
     *
     * @param text Text
     */
    public Label(String text) {
        super(text);
        setForeground(FONT_COLOR);
    }

    /**
     * Constructor.
     *
     * @param text Text
     * @param d    Dimension
     */
    public Label(String text, Dimension d) {
        super(text);
        setForeground(FONT_COLOR);
        setPreferredSize(d);
    }

    /**
     * Constructor.
     *
     * @param image Image
     */
    public Label(Icon image) {
        super(image);
        setForeground(FONT_COLOR);
    }

    /**
     * Constructor.
     *
     * @param text                Text
     * @param horizontalAlignment Horizontal alignment
     */
    public Label(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setForeground(FONT_COLOR);
    }

    /**
     * Constructor.
     *
     * @param text                Text
     * @param d                   Dimension
     * @param horizontalAlignment Horizontal alignment
     */
    public Label(String text, Dimension d, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setForeground(FONT_COLOR);
        setPreferredSize(d);
    }
}
