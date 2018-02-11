package view.decorated;

import javax.swing.*;
import java.awt.*;

import static util.Utils.fillColor;

public class Panel extends JPanel {

    public Panel() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        fillColor(g, Color.DARK_GRAY, Color.DARK_GRAY, getWidth(), getHeight()); //todo
    }


}
