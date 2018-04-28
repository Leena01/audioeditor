package org.ql.audioeditor.view.core.bar;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static org.ql.audioeditor.common.util.Helper.fillColor;

public class HorizontalBar extends JPanel {
    public HorizontalBar() {
        super(new BorderLayout());
        setOpaque(false);
    }

    public HorizontalBar(MouseListener ml, MouseMotionListener mml) {
        super(new BorderLayout());
        addMouseListener(ml);
        addMouseMotionListener(mml);
        setOpaque(false);
    }

    public HorizontalBar(BorderLayout bl) {
        super(bl);
        setOpaque(false);
    }

    public void setHeight(int h) {
        int w = this.getWidth();
        setPreferredSize(new Dimension(w, h));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        fillColor(g, Color.GRAY, Color.BLACK, getWidth(), getHeight());
    }
}
