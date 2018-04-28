package org.ql.audioeditor.view.core.bar;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static org.ql.audioeditor.common.util.Helper.fillColor;

public class SideBar extends JPanel {
    private static final Border BORDER =
            BorderFactory.createMatteBorder(0, 0, 0, 3, Color.DARK_GRAY);
    public SideBar() {
        FlowLayout layout = (FlowLayout)getLayout();
        layout.setVgap(0);
        layout.setHgap(0);
        setBorder(BORDER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        fillColor(g, Color.DARK_GRAY, Color.BLACK, getWidth(), getHeight());
    }
}
