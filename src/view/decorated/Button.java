package view.decorated;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {

    private static final Dimension preferredSize = new Dimension(140, 30);

    public Button() {
        super();
        setPreferredSize(preferredSize);
    }

    public Button(String text) {
        super(text);
        setPreferredSize(preferredSize);
    }
}
