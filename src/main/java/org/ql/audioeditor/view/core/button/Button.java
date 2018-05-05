package org.ql.audioeditor.view.core.button;

import javax.swing.JButton;
import java.awt.event.ActionListener;

/**
 * Button.
 */
public class Button extends JButton {
    /**
     * Constructor.
     *
     * @param text Text
     * @param al   Action listener
     */
    public Button(String text, ActionListener al) {
        super(text);
        addActionListener(al);
        setFocusPainted(false);
    }
}
