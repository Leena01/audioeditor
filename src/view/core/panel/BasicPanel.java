package view.core.panel;

import javax.swing.*;

public abstract class BasicPanel extends JPanel {
    public BasicPanel() {
        super();
    }

    protected abstract void setStyle();
    protected abstract void addPanels();
}
