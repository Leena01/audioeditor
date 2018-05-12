package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.view.core.label.Label;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;

/**
 * Panel for key detection.
 */
public final class KeyDetPanel extends DetPanel {
    private static final String EST_MESSAGE = "The detected key is %s.";
    private final JLabel estimationLabel;
    private final JPanel estimationPanel;

    /**
     * Constructor.
     *
     * @param d DoneButton listener
     */
    public KeyDetPanel(ActionListener d) {
        super(d);

        estimationLabel = new Label(EST_MESSAGE);
        estimationPanel = new JPanel();
        setStyleAdditional();
        addPanelsAdditional();
        hideEstimation();
    }

    /**
     * Shows the detected key.
     *
     * @param est Detected key
     */
    public void showEstimation(String est) {
        estimationLabel.setText(String.format(EST_MESSAGE, est));
        estimationPanel.setVisible(true);
    }

    /**
     * Hides the detected key.
     */
    public void hideEstimation() {
        estimationLabel.setText(String.format(EST_MESSAGE, ""));
        estimationPanel.setVisible(false);
    }

    private void setStyleAdditional() {
        estimationPanel.setOpaque(false);
    }

    private void addPanelsAdditional() {
        estimationPanel.add(estimationLabel);
        mainPanel.add(estimationPanel);
    }
}
