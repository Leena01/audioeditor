package view.panel.analysis;

import logic.matlab.MatlabHandler;
import view.core.panel.BasicPanel;

import javax.swing.*;
import java.awt.*;

public class AnalysisPanel extends BasicPanel {
    private MatlabHandler matlabHandler;
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
