package org.ql.audioeditor.view.core.button;

import javax.swing.Icon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Transparent button.
 */
public class TransparentButton extends JButton implements MouseListener {
    private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
    private static final Color HOVER_COLOR = Color.WHITE;
    private static final Color PRESSED_COLOR = Color.GRAY;

    public TransparentButton(String text, ActionListener al) {
        super(text);
        addActionListener(al);
        setAttributes();
        addMouseListener();
    }

    public TransparentButton(Icon image, Dimension d) {
        super(image);
        setPreferredSize(d);
        setAttributes();
        addMouseListener();
    }

    public TransparentButton(Icon image, Dimension d, ActionListener al) {
        super(image);
        setPreferredSize(d);
        addActionListener(al);
        setAttributes();
        addMouseListener();
    }

    public void mouseClicked(MouseEvent e) {
        setForeground(PRESSED_COLOR);
        setForeground(DEFAULT_COLOR);
    }

    public void mousePressed(MouseEvent e) {
        setForeground(PRESSED_COLOR);
    }

    public void mouseReleased(MouseEvent e) {
        setForeground(DEFAULT_COLOR);
    }

    public void mouseEntered(MouseEvent e) {
        setForeground(HOVER_COLOR);
    }

    public void mouseExited(MouseEvent e) {
        setForeground(DEFAULT_COLOR);
    }

    private void addMouseListener() {
        this.addMouseListener(this);
    }

    private void setAttributes() {
        setForeground(DEFAULT_COLOR);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
    }
}
