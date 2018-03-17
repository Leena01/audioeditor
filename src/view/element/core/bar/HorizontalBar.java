package view.element.core.bar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static util.Utils.fillColor;

public class HorizontalBar extends JPanel {
    public HorizontalBar(MouseListener ml, MouseMotionListener mml) {
        super(new BorderLayout());
        addMouseListener(ml);
        addMouseMotionListener(mml);
        setOpaque(false);
    }

    public HorizontalBar() {
        super(new BorderLayout());
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
