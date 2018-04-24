package org.ql.audioeditor.view.core.slider;

import org.ql.audioeditor.view.core.label.Label;

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
