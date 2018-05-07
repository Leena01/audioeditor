package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.view.core.panel.BasicPanel;

/**
 * Panel for onset detection.
 */
public final class OnsetDetPanel extends BasicPanel {
    /**
     * Constructor.
     */
    public OnsetDetPanel() {
        setStyle();
        addPanels();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setStyle() {
        setOpaque(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPanels() {
    }
}
