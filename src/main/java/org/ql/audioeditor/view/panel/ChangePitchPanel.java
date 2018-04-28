package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.panel.BasicPanel;
import org.ql.audioeditor.view.core.slider.FrequencySlider;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionListener;

import static org.ql.audioeditor.view.param.Constants.LEVEL_SLIDER_SIZE;

public final class ChangePitchPanel extends BasicPanel {
    private static final Border INFO_LABEL_BORDER =
        BorderFactory.createEmptyBorder(10, 10, 0, 10);
    private static final String INSTR_TEXT = "Please set the level of the new" +
        " current frequency. The current frequency is %.0f Hz.";
    private FrequencySlider frequencySlider;
    private JPanel buttonPanel;
    private JPanel infoPanel;
    private JLabel infoLabel;
    private JButton previewButton;
    private JButton saveButton;

    public ChangePitchPanel(ActionListener p, ActionListener s) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        buttonPanel = new JPanel();
        infoPanel = new JPanel();
        infoLabel = new JLabel(INSTR_TEXT);
        frequencySlider = new FrequencySlider(LEVEL_SLIDER_SIZE,
            SongPropertiesLoader.getFrequencyMin(),
            SongPropertiesLoader.getFrequencyMax(),
            1000, 500);
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
        add(buttonPanel);
    }
}
