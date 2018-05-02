package org.ql.audioeditor.view.core.slider;

import javax.swing.JSlider;
import java.awt.Dimension;

/**
 * Volume slider.
 */
public class VolumeSlider extends JSlider {
    private static final int MAJOR_TICK_SPACING = 10;
    private static final int MINOR_TICK_SPACING = 5;
    public VolumeSlider(Dimension d, int min, int max, int value) {
        super(JSlider.HORIZONTAL);
        setPreferredSize(d);
        setMinimumSize(d);
        setMajorTickSpacing(MAJOR_TICK_SPACING);
        setMinorTickSpacing(MINOR_TICK_SPACING);
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
