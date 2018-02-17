package view.element.playerpanel;

import javax.swing.*;
import java.awt.*;

public class TrackSlider extends JSlider {
    private static final Object lock = new Object();
    private Image img;

    public TrackSlider(final int n) {
        super(n, 0, 100, 50);
    }

    public Image getImage() {
        return img;
    }

    public void setImage(Image img) {
        this.img = img;
    }

    @Override
    public void setValue(final int value) {
        synchronized (lock) {
            final BoundedRangeModel model = this.getModel();
            model.removeChangeListener(this.changeListener);
            final int value2 = model.getValue();
            if (value2 == value) {
                return;
            }
            model.setValue(value);
            if (this.accessibleContext != null) {
                this.accessibleContext.firePropertyChange("AccessibleValue", value2, model.getValue());
            }
            model.addChangeListener(this.changeListener);
        }
    }
}