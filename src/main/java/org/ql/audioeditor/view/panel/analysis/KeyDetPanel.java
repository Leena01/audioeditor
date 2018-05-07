package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.view.core.panel.BasicPanel;

/**
 * Panel for key detection.
 */
public final class KeyDetPanel extends BasicPanel {
    /**
     * Constructor.
     */
    public KeyDetPanel() {
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
