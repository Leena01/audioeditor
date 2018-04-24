package org.ql.audioeditor.view.core.slider;

import javax.swing.*;
import java.awt.*;

public class FrequencySlider extends JSlider {
    public FrequencySlider(Dimension d, int min, int max, int majorTick, int minorTick) {
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
