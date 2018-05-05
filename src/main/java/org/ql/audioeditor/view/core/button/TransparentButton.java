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
public class TransparentButton extends JButton {
    private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
    private static final Color HOVER_COLOR = Color.WHITE;
    private static final Color PRESSED_COLOR = Color.GRAY;

    /**
     * Constructor.
     *
     * @param text Text
     * @param al   Action listener
     */
    public TransparentButton(String text, ActionListener al) {
        super(text);
        addActionListener(al);
        setAttributes();
        addMouseListener();
    }

    /**
     * Constructor.
     *
     * @param image Image
     * @param d     Dimension
     */
    public TransparentButton(Icon image, Dimension d) {
        super(image);
        setPreferredSize(d);
        setAttributes();
        addMouseListener();
    }

    /**
     * Constructor.
     *
     * @param image Image
     * @param d     Dimension
     * @param al    Action listener
     */
    public TransparentButton(Icon image, Dimension d, ActionListener al) {
        super(image);
        setPreferredSize(d);
        addActionListener(al);
        setAttributes();
        addMouseListener();
    }

    /**
     * Adds mouse listener to button.
     */
    private void addMouseListener() {
        this.addMouseListener(new ButtonListener());
    }

    /**
     * Sets attributes.
     */
    private void setAttributes() {
        setForeground(DEFAULT_COLOR);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
    }

    /**
     * Mouse listener for class 'TransparentButton'.
     */
    private final class ButtonListener implements MouseListener {
        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            setForeground(PRESSED_COLOR);
            setForeground(DEFAULT_COLOR);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mousePressed(MouseEvent e) {
            setForeground(PRESSED_COLOR);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            setForeground(DEFAULT_COLOR);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            setForeground(HOVER_COLOR);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseExited(MouseEvent e) {
            setForeground(DEFAULT_COLOR);
        }
    }
}
