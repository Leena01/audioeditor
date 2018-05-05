package org.ql.audioeditor.view.core.textfield;

import javax.swing.JTextField;
import java.awt.Dimension;

/**
 * Textfield.
 */
public class TextField extends JTextField {
    /**
     * Constructor.
     *
     * @param text                Text
     * @param d                   Dimension
     * @param horizontalAlignment Horinzontal alignment
     */
    public TextField(String text, Dimension d, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setPreferredSize(d);
    }
}
