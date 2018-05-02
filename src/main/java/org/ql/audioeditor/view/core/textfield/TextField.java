package org.ql.audioeditor.view.core.textfield;

import javax.swing.JTextField;
import java.awt.Dimension;

/**
 * Textfield.
 */
public class TextField extends JTextField {
    public TextField(String text, Dimension d, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setPreferredSize(d);
    }
}
