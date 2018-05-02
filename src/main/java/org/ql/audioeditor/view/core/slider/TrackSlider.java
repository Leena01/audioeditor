package org.ql.audioeditor.view.core.slider;

import org.ql.audioeditor.view.core.slider.ui.AudioSliderUI;

import javax.swing.BoundedRangeModel;
import javax.swing.JSlider;
import java.awt.Image;

/**
 * Track slider.
 */
public class TrackSlider extends JSlider {
    private static final Object LOCK_OBJECT = new Object();
    private static final int TRACK_MIN_DEFAULT = 0;
    private static final int TRACK_MAX_DEFAULT = 100;
    private static final int TRACK_INIT_DEFAULT = 0;
    private Image img;

    public TrackSlider(final int n) {
        super(n, TRACK_MIN_DEFAULT, TRACK_MAX_DEFAULT, TRACK_INIT_DEFAULT);
    }

    public Image getImage() {
        return img;
    }

    public void setImage(Image img) {
        this.img = img;
    }

    @Override
    public void updateUI() {
        setUI(new AudioSliderUI(this));
        updateLabelUIs();
    }

    @Override
    public void setValue(final int value) {
        synchronized (LOCK_OBJECT) {
            final BoundedRangeModel model = this.getModel();
            model.removeChangeListener(this.changeListener);
            final int value2 = model.getValue();
            if (value2 == value) {
                return;
            }
            model.setValue(value);
            if (this.accessibleContext != null) {
                this.accessibleContext
                    .firePropertyChange("AccessibleValue", value2,
                        model.getValue());
            }
            model.addChangeListener(this.changeListener);
        }
    }
}
