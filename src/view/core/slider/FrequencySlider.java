package view.core.slider;

import javax.swing.*;
import java.awt.*;

public class Slider extends JSlider {
    public Slider(Dimension d, int min, int max, int value, int majorTick, int minorTick) {
        super(JSlider.HORIZONTAL, min, max, value);
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
