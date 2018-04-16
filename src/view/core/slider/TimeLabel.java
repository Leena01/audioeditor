package view.core.slider;

import view.core.label.Label;

import javax.swing.*;
import java.awt.*;

public class TimeLabel extends Label {
    public TimeLabel(Dimension d, int horizontalAlignment) {
        super("", horizontalAlignment);
        setOpaque(false);
        setMinimumSize(d);
        setPreferredSize(d);
        setForeground(Color.GRAY);
    }
}
