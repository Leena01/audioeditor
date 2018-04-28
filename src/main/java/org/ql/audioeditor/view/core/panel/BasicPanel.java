package org.ql.audioeditor.view.core.panel;

import javax.swing.JPanel;

public abstract class BasicPanel extends JPanel {
    public BasicPanel() {
        super();
    }

    protected abstract void setStyle();

    protected abstract void addPanels();
}
