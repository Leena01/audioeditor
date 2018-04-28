package org.ql.audioeditor.view.core.button;

import javax.swing.Icon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class OptionButton extends JButton implements MouseListener {
    private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
    private static final Color HOVER_COLOR = Color.WHITE;
    private static final Color BACKGROUND_COLOR = Color.DARK_GRAY;

    public OptionButton(String text) {
        super(text);
        setAttributes();
        addMouseListener();
    }

    public OptionButton(String text, ActionListener al) {
        super(text);
        addActionListener(al);
        setAttributes();
        addMouseListener();
    }

    public OptionButton(String text, Dimension d) {
        this(text);
        setPreferredSize(d);
        setAttributes();
        addMouseListener();
    }

    public OptionButton(Icon image) {
        super(image);
        setAttributes();
        addMouseListener();
    }

    public OptionButton(Dimension d) {
        super();
        setPreferredSize(d);
        addMouseListener();
    }

    public OptionButton(Icon image, Dimension d) {
        super(image);
        setPreferredSize(d);
        setAttributes();
        addMouseListener();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        setContentAreaFilled(true);
        setContentAreaFilled(false);
        addMouseListener();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setContentAreaFilled(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setContentAreaFilled(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setForeground(HOVER_COLOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setForeground(DEFAULT_COLOR);
    }

    private void addMouseListener() {
        this.addMouseListener(this);
    }

    private void setAttributes() {
        setForeground(DEFAULT_COLOR);
        setBackground(BACKGROUND_COLOR);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
    }
}
