package org.ql.audioeditor.view.core.bar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import static org.ql.audioeditor.common.util.Helper.fillColor;

public class InverseHorizontalBar extends HorizontalBar {
    public InverseHorizontalBar(BorderLayout bl) {
        super(bl);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        fillColor(g, Color.BLACK, Color.DARK_GRAY, getWidth(), getHeight());
    }
}
