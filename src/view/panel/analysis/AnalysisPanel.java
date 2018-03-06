package view.panel.analysis;

import logic.matlab.MatlabHandler;

import javax.swing.*;
import java.awt.*;

public class AnalysisPanel extends JPanel {
    private MatlabHandler matlabHandler;
    public AnalysisPanel(MatlabHandler matlabHandler) {
        super();
        this.matlabHandler = matlabHandler;
        setBackground(Color.BLACK);
    }
}
