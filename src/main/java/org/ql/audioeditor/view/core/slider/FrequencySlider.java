package org.ql.audioeditor.view.core.slider;

import javax.swing.JSlider;
import java.awt.Dimension;

/**
 * Frequency slider.
 */
public class FrequencySlider extends JSlider {
    /**
     * Constructor.
     * @param d Dimension
     * @param min Minimum value
     * @param max Maximum value
     * @param majorTick Major tick
     * @param minorTick Minor tick
     */
    public FrequencySlider(Dimension d, int min, int max, int majorTick,
        int minorTick) {
        super(JSlider.HORIZONTAL, min, max, (min + max) / 2);
        setPreferredSize(d);
        setMinimumSize(d);
        setMajorTickSpacing(majorTick);
        setMinorTickSpacing(minorTick);
        setPaintTicks(false);
        setFocusable(false);
        setOpaque(false);
    }
}
