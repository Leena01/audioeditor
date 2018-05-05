package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.core.panel.BasicPanel;

import java.awt.Color;

/**
 * Panel for analysis.
 */
public final class AnalysisPanel extends BasicPanel {
    private final MatlabHandler matlabHandler;

    /**
     * Constructor.
     *
     * @param matlabHandler Matlab handler.
     */
    public AnalysisPanel(MatlabHandler matlabHandler) {
        super();
        this.matlabHandler = matlabHandler;
        setStyle();
        addPanels();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setStyle() {
        setBackground(Color.BLACK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPanels() {

    }
}
