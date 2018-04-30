package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.core.panel.BasicPanel;

import java.awt.Color;

public class AnalysisPanel extends BasicPanel {
    private final MatlabHandler matlabHandler;

    public AnalysisPanel(MatlabHandler matlabHandler) {
        super();
        this.matlabHandler = matlabHandler;
        setStyle();
        addPanels();
    }

    @Override
    protected void setStyle() {
        setBackground(Color.BLACK);
    }

    @Override
    protected void addPanels() {

    }
}
