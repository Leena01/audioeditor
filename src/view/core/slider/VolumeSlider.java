package view.core.slider;

import javax.swing.*;
import java.awt.*;

public class VolumeSlider extends JSlider {
    public VolumeSlider(Dimension d) {
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
        setValue(50);
    }
}
