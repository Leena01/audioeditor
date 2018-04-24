package org.ql.audioeditor.view.core.slider;

import org.ql.audioeditor.common.properties.SongPropertiesLoader;

import javax.swing.*;
import java.awt.*;

public class VolumeSlider extends JSlider {
    public VolumeSlider(Dimension d, int min, int max, int value) {
        super(JSlider.HORIZONTAL);
        setPreferredSize(d);
        setMinimumSize(d);
        setMajorTickSpacing(10);
        setMinorTickSpacing(5);
        setPaintTicks(false);
        setPaintLabels(false);
        setPaintTrack(true);
        setFocusable(false);
        setOpaque(false);
        setMaximum(max);
        setMinimum(min);
        setValue(value);
    }
}
