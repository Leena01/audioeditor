package view.panel;

import common.properties.SongPropertiesLoader;
import view.core.button.Button;
import view.core.panel.BasicPanel;
import view.core.slider.FrequencySlider;

import static view.param.Constants.*;
import javax.swing.*;
import java.awt.event.ActionListener;

public final class ChangePitchPanel extends BasicPanel {
    private static final String INSTR_TEXT = "Please set the level of the new current frequency. The current frequency is %.0f Hz.";
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
                SongPropertiesLoader.getFrequencyMin(), SongPropertiesLoader.getFrequencyMax(), 1000, 500);
        previewButton = new Button("Preview", p);
        saveButton = new Button("Save modified file", s);
        setStyle();
        addPanels();
    }

    public void setFreq(double freq) {
        frequencySlider.setValue((int)freq);
        infoLabel.setText(String.format(INSTR_TEXT, freq));
    }

    public double getFreq() {
        return frequencySlider.getValue();
    }

    @Override
    protected void setStyle() {
        buttonPanel.setOpaque(false);
        infoPanel.setOpaque(false);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
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
