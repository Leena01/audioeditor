package view.decorated;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Button extends JButton implements MouseListener {
    private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
    private static final Color HOVER_COLOR = Color.WHITE;
    private static final Color PRESSED_COLOR = Color.GRAY;

    public Button() {
        super();
        setAttributes();
    }

    public Button(String text) {
        super(text);
        setAttributes();
    }

    public Button(String text, Dimension d) {
        this(text);
        setPreferredSize(d);
        setAttributes();
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

    public void addMouseListener() {
        this.addMouseListener(this);
    }

    private void setAttributes() {
        setForeground(DEFAULT_COLOR);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
    }
}
