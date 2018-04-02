package view.core.slider;

import javax.swing.*;
import java.awt.*;

import static view.util.Constants.DEFAULT_INIT_VAL;

public class FrequencySlider extends JSlider {
    public FrequencySlider(Dimension d, int min, int max, int majorTick, int minorTick) {
        super(JSlider.HORIZONTAL, min, max, DEFAULT_INIT_VAL);
        setPreferredSize(d);
        setMinimumSize(d);
        setMajorTickSpacing(majorTick);
        setMinorTickSpacing(minorTick);
        setPaintTicks(true);
        setPaintLabels(true);
        setPaintTrack(true);
        setFocusable(false);
        setOpaque(false);
    }
}
