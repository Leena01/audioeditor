package view.elements;

import javax.swing.*;
import java.awt.*;

import static util.Utils.fillColor;

public class SideBar extends JPanel {
    public SideBar() {
        FlowLayout layout = (FlowLayout)getLayout();
        layout.setVgap(0);
        layout.setHgap(0);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, Color.DARK_GRAY));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        fillColor(g, Color.DARK_GRAY, Color.BLACK, getWidth(), getHeight());
    }
}
