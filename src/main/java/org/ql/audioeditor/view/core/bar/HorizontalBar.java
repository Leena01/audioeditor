package org.ql.audioeditor.view.core.bar;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static org.ql.audioeditor.common.util.ViewUtils.fillColor;

/**
 * Horizontal bar.
 */
public class HorizontalBar extends JPanel {
    /**
     * Constructor.
     */
    public HorizontalBar() {
        super(new BorderLayout());
        setOpaque(false);
    }

    /**
     * Constructor.
     * @param ml Mouse listener
     * @param mml Mouse motion listener
     */
    public HorizontalBar(MouseListener ml, MouseMotionListener mml) {
        super(new BorderLayout());
        addMouseListener(ml);
        addMouseMotionListener(mml);
        setOpaque(false);
    }

    /**
     * Constructor.
     * @param bl Border layout
     */
    public HorizontalBar(BorderLayout bl) {
        super(bl);
        setOpaque(false);
    }

    /**
     * Sets the height of the bar.
     * @param h Height
     */
    public void setHeight(int h) {
        int w = this.getWidth();
        setPreferredSize(new Dimension(w, h));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        fillColor(g, Color.GRAY, Color.BLACK, getWidth(), getHeight());
    }
}
