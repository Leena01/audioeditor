package view.core.bar;

import java.awt.*;

import static common.util.Helper.fillColor;

public class InverseHorizontalBar extends HorizontalBar {
    public InverseHorizontalBar(FlowLayout fl) {
        super(fl);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        fillColor(g, Color.BLACK, Color.DARK_GRAY, getWidth(), getHeight());
    }
}
