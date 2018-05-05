package org.ql.audioeditor.view.core.bar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import static org.ql.audioeditor.common.util.ViewUtils.fillColor;

/**
 * Inverse horizontal bar.
 */
public class InverseHorizontalBar extends HorizontalBar {
    /**
     * Constructor.
     * @param bl Border layout
     */
    public InverseHorizontalBar(BorderLayout bl) {
        super(bl);
        setOpaque(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        fillColor(g, Color.BLACK, Color.DARK_GRAY, getWidth(), getHeight());
    }
}
