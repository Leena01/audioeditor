package org.ql.audioeditor.view.core.button;

import javax.swing.JButton;
import java.awt.event.ActionListener;

public class Button extends JButton {

    public Button(String text, ActionListener al) {
        super(text);
        addActionListener(al);
        setFocusPainted(false);
    }
}
