package view.panel;

import view.core.button.Button;
import view.core.button.OptionButton;
import view.core.slider.FrequencySlider;

import static view.util.Constants.*;
import javax.swing.*;
import java.awt.event.ActionListener;

public class ChangePitchPanel extends JPanel {
    private static final String INSTR_TEXT = "Please set the level of the new current frequency. The current frequency is %.0f Hz.";
    private FrequencySlider levelSlider;
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
        levelSlider = new FrequencySlider(LEVEL_SLIDER_SIZE, 0, 50000, 1000, 500);
        previewButton = new Button("Preview", p);
        saveButton = new Button("Save modified file", s);
        buttonPanel.add(previewButton);
        buttonPanel.add(saveButton);
        add(infoPanel);
        add(levelSlider);
        add(buttonPanel);
    }

    public void setFreq(double freq) {
        levelSlider.setValue((int)freq);
        infoLabel.setText(String.format(INSTR_TEXT, freq));
    }

    public double getFreq() {
        return levelSlider.getValue();
    }
}
