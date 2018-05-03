package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.panel.BasicPanel;
import org.ql.audioeditor.view.core.slider.FrequencySlider;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.event.ActionListener;

import static org.ql.audioeditor.view.param.Constants.LEVEL_SLIDER_SIZE;

/**
 * Panel for making changes in pitch/speed.
 */
public final class ChangePitchPanel extends BasicPanel {
    private static final int MAJOR_TICK = 1000;
    private static final int MINOR_TICK = 500;
    private static final int MAX = SongPropertiesLoader.getFrequencyMax();
    private static final int MIN = SongPropertiesLoader.getFrequencyMin();
    private static final Border INFO_LABEL_BORDER =
        BorderFactory.createEmptyBorder(10, 10, 0, 10);
    private static final String INSTR_TEXT =
        "Please set the level of the new frequency. The current frequency is "
            + "%.0f Hz.";
    private static final String NEW_FREQ_TEXT = "New frequency: %.0f Hz";
    private final FrequencySlider frequencySlider;
    private final JPanel buttonPanel;
    private final JPanel infoPanel;
    private final JLabel infoLabel;
    private final JPanel newFreqPanel;
    private final JLabel newFreqLabel;
    private final JButton previewButton;
    private final JButton saveButton;

    public ChangePitchPanel(ActionListener p, ActionListener s) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        buttonPanel = new JPanel();
        infoPanel = new JPanel();
        infoLabel = new JLabel(INSTR_TEXT);
        newFreqPanel = new JPanel();
        newFreqLabel = new JLabel(NEW_FREQ_TEXT);
        frequencySlider = new FrequencySlider(LEVEL_SLIDER_SIZE, MIN, MAX,
            MAJOR_TICK, MINOR_TICK);
        previewButton = new Button("Preview", p);
        saveButton = new Button("Save modified file", s);
        setStyle();
        addPanels();
    }

    public double getFreq() {
        return frequencySlider.getValue();
    }

    public void setFreq(double freq) {
        frequencySlider.setValue((int) freq);
        infoLabel.setText(String.format(INSTR_TEXT, freq));
    }

    @Override
    protected void setStyle() {
        buttonPanel.setOpaque(false);
        infoPanel.setOpaque(false);
        infoLabel.setBorder(INFO_LABEL_BORDER);
    }

    @Override
    protected void addPanels() {
        infoPanel.add(infoLabel);
        buttonPanel.add(previewButton);
        buttonPanel.add(saveButton);
        add(infoPanel);
        add(frequencySlider);
        add(newFreqPanel);
        add(buttonPanel);
    }
}
