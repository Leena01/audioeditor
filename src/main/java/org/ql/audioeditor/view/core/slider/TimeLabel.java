package org.ql.audioeditor.view.core.slider;

import org.ql.audioeditor.view.core.label.Label;

import java.awt.Color;
import java.awt.Dimension;

/**
 * Time label.
 */
public class TimeLabel extends Label {
    /**
     * Constructor.
     * @param d Dimension
     * @param horizontalAlignment Horizontal alignment
     */
    public TimeLabel(Dimension d, int horizontalAlignment) {
        super("", horizontalAlignment);
        setOpaque(false);
        setMinimumSize(d);
        setPreferredSize(d);
        setForeground(Color.GRAY);
    }
}
