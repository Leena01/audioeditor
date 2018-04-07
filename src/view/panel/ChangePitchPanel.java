package view.panel;

import properties.SongPropertiesLoader;
import view.core.button.Button;
import view.core.slider.FrequencySlider;

import static view.util.Constants.*;
import javax.swing.*;
import java.awt.event.ActionListener;

public class ChangePitchPanel extends JPanel {
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
        buttonPanel.setOpaque(false);
        infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoLabel = new JLabel(INSTR_TEXT);
        infoPanel.add(infoLabel);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        frequencySlider = new FrequencySlider(LEVEL_SLIDER_SIZE,
                SongPropertiesLoader.getFrequencyMin(), SongPropertiesLoader.getFrequencyMax(), 1000, 500);
        previewButton = new Button("Preview", p);
        saveButton = new Button("Save modified file", s);
        buttonPanel.add(previewButton);
        buttonPanel.add(saveButton);
        add(infoPanel);
        add(frequencySlider);
        add(buttonPanel);
    }

    public void setFreq(double freq) {
        frequencySlider.setValue((int)freq);
        infoLabel.setText(String.format(INSTR_TEXT, freq));
    }

    public double getFreq() {
        return frequencySlider.getValue();
    }
}
