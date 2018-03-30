package view.element.slider;

import view.element.core.label.Label;

import javax.swing.*;
import java.awt.*;

public class TimeLabel extends Label {
    public TimeLabel(Dimension d) {
        super("", SwingConstants.RIGHT);
        setOpaque(false);
        setMinimumSize(d);
        setPreferredSize(d);
    }
}
