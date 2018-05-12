package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.panel.BasicPanel;
import org.ql.audioeditor.view.core.textfield.FormattedTextField;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

/**
 * Panel for beat estimation.
 */
public final class BeatEstPanel extends BasicPanel {
    private static final GridLayout FORM_PANEL_LAYOUT = new GridLayout(3, 2,
        10, 10);
    private static final Border INSTR_PANEL_BORDER = BorderFactory
        .createEmptyBorder(0, 0, 15, 0);
    private static final String ISTR_MESSAGE =
        "Please enter the minimum and maximum BPM.";
    private static final String EST_MESSAGE = "The estimated BPM is %d.";
    private static final int BPM_MIN = SongPropertiesLoader.getBpmMin();
    private static final int BPM_MAX = SongPropertiesLoader.getBpmMax();
    private static final String BPM_LIMIT = String.format(
        "The minimum BPM is %d and the maximum is %d.", BPM_MIN, BPM_MAX);

    private final JLabel instrLabel;
    private final JPanel instrPanel;
    private final JPanel formPanel;
    private final JLabel minBpmLabel;
    private final JTextField minBpmTextField;
    private final JLabel maxBpmLabel;
    private final JTextField maxBpmTextField;
    private final JButton doneButton;
    private final JLabel estimationLabel;
    private final JPanel estimationPanel;
    private final JPanel mainPanel;

    /**
     * Constructor.
     *
     * @param al DoneButton listener
     */
    public BeatEstPanel(ActionListener al) {
        formPanel = new JPanel(FORM_PANEL_LAYOUT);
        instrLabel = new Label(ISTR_MESSAGE);
        instrPanel = new JPanel();
        minBpmLabel = new Label("Minimum BPM:");
        minBpmLabel.setToolTipText(BPM_LIMIT);

        NumberFormatter nf = new NumberFormatter();
        nf.setMinimum(BPM_MIN);
        nf.setMaximum(BPM_MAX);
        minBpmTextField = new FormattedTextField(nf);
        minBpmTextField.setToolTipText(BPM_LIMIT);

        maxBpmLabel = new Label("Maximum BPM:");
        maxBpmLabel.setToolTipText(BPM_LIMIT);

        maxBpmTextField = new FormattedTextField(nf);
        maxBpmTextField.setToolTipText(BPM_LIMIT);

        doneButton = new Button("Done", al);
        estimationLabel = new Label(EST_MESSAGE);
        estimationPanel = new JPanel();
        mainPanel = new JPanel();

        resetFields();
        setStyle();
        addPanels();
        hideEstimation();
    }

    /**
     * Returns the minimum BPM entered by the user. Should not be smaller than
     * BPM_MIN.
     *
     * @return Minimum BPM
     */
    public int getMinBpm() {
        String text = minBpmTextField.getText();
        if (text.equals("")) {
            minBpmTextField.setText(Integer.toString(BPM_MIN));
            return BPM_MIN;
        }
        int minBpm = Integer.parseInt(text);
        if (minBpm < BPM_MIN) {
            minBpmTextField.setText(Integer.toString(BPM_MIN));
            return BPM_MIN;
        }
        return minBpm;
    }

    /**
     * Returns the maximum BPM entered by the user. Should not be greater than
     * BPM_MAX.
     *
     * @return Maximum BPM
     */
    public int getMaxBpm() {
        String text = maxBpmTextField.getText();
        if (text.equals("")) {
            maxBpmTextField.setText(Integer.toString(BPM_MAX));
            return BPM_MAX;
        }
        int maxBpm = Integer.parseInt(text);
        if (maxBpm > BPM_MAX) {
            maxBpmTextField.setText(Integer.toString(BPM_MAX));
            return BPM_MAX;
        }
        return maxBpm;
    }

    /**
     * Shows the estimated number.
     *
     * @param est Estimated number
     */
    public void showEstimation(int est) {
        estimationLabel.setText(String.format(EST_MESSAGE, est));
        estimationPanel.setVisible(true);
    }

    /**
     * Shows the estimated number.
     */
    public void hideEstimation() {
        estimationLabel.setText(String.format(EST_MESSAGE, 0));
        estimationPanel.setVisible(false);
    }

    /**
     * Resets the fields to their default values.
     */
    public void resetFields() {
        minBpmTextField.setText(Integer.toString(BPM_MIN));
        maxBpmTextField.setText(Integer.toString(BPM_MAX));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setStyle() {
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        setOpaque(false);
        mainPanel.setOpaque(false);
        instrPanel.setOpaque(false);
        formPanel.setOpaque(false);
        estimationPanel.setOpaque(false);
        mainPanel.setOpaque(false);
        instrPanel.setBorder(INSTR_PANEL_BORDER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPanels() {
        instrPanel.add(instrLabel);
        formPanel.add(minBpmLabel);
        formPanel.add(minBpmTextField);
        formPanel.add(maxBpmLabel);
        formPanel.add(maxBpmTextField);
        formPanel.add(doneButton);
        estimationPanel.add(estimationLabel);
        mainPanel.add(instrPanel);
        mainPanel.add(formPanel);
        mainPanel.add(estimationPanel);
        add(mainPanel);
    }
}
