package view.core.button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Button extends JButton {

    public Button(String text, ActionListener al) {
        super(text);
        addActionListener(al);
        setFocusPainted(false);
    }
}
