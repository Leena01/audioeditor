package org.ql.audioeditor.view.core.panel;

import javax.swing.JPanel;

/**
 * Basic panel.
 */
public abstract class BasicPanel extends JPanel {
    public BasicPanel() {
        super();
    }

    /**
     * Changes the visual representation of certain elements (e.g. distance,
     * border, color).
     */
    protected abstract void setStyle();

    /**
     * Adds inner panels to the main panel.
     */
    protected abstract void addPanels();
}
