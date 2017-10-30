package util;

import javax.swing.*;

public class Slider extends JSlider {
    public Slider() {
        super();
    }

    public Slider(final int n) {
        super(n, 0, 100, 50);
    }

    public Slider(final int n, final int n2) {
        super(0, n, n2, (n + n2) / 2);
    }

    public Slider(final int n, final int n2, final int n3) {
        super(0, n, n2, n3);
    }

    @Override
    public void setValue(final int value) {
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
