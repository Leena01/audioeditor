package view.elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static util.Utils.fillColor;

class TitleBar extends JPanel {
    TitleBar(MouseListener ml, MouseMotionListener mml) {
        super(new BorderLayout());
        addMouseListener(ml);
        addMouseMotionListener(mml);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        fillColor(g, Color.GRAY, Color.BLACK, getWidth(), getHeight());
    }
}
