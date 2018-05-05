package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.core.bar.InverseHorizontalBar;
import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.panel.BasicPanel;
import org.ql.audioeditor.view.core.slider.FrequencySlider;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import static org.ql.audioeditor.view.param.Constants.LEVEL_SLIDER_SIZE;
import static org.ql.audioeditor.view.param.Constants
    .MEDIA_CONTROL_PANEL_HEIGHT;
import static org.ql.audioeditor.view.param.Constants.PLAYER_PANEL_BORDER;

/**
 * Panel for making changes in pitch/speed.
 */
public final class ChangePitchPanel extends BasicPanel {
    private static final int MAJOR_TICK = 1000;
    private static final int MINOR_TICK = 500;
    private static final int MAX = SongPropertiesLoader.getFrequencyMax();
    private static final int MIN = SongPropertiesLoader.getFrequencyMin();
    private static final Border CURR_FREQ_BORDER =
        BorderFactory.createEmptyBorder(30, 0, 0, 0);
    private static final Border INFO_LABEL_BORDER =
        BorderFactory.createEmptyBorder(10, 10, 0, 10);
    private static final String INSTR_TEXT =
        "Please set the level of the new frequency. The original frequency is "
            + "%.0f Hz.";
    private static final String NEW_FREQ_TEXT = "New frequency: %d Hz";
    private static final String CURR_FREQ_TEXT =
        "Current frequency in preview: %.0f Hz";
    private final FrequencySlider frequencySlider;
    private final JPanel upperPanel;
    private final JPanel lowerPanel;
    private final JPanel buttonPanel;
    private final JPanel infoPanel;
    private final JLabel infoLabel;
    private final JPanel newFreqPanel;
    private final JLabel newFreqLabel;
    private final JPanel currentFreqPanel;
    private final JLabel currentFreqLabel;
    private final JButton previewButton;
    private final JButton saveButton;
    private final HorizontalBar mediaControlPanel;
    private final SimplePlayerPanel simplePlayerPanel;

    /**
     * Constructor.
     * @param matlabHandler Matlab handler
     * @param p PreviewButton listener
     * @param s SaveButton listener
     */
    public ChangePitchPanel(MatlabHandler matlabHandler, ActionListener p,
        ActionListener s) {
        super();
        setLayout(new BorderLayout());
        upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.PAGE_AXIS));
        lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.PAGE_AXIS));
        buttonPanel = new JPanel();
        infoPanel = new JPanel();
        infoLabel = new JLabel(INSTR_TEXT);
        newFreqPanel = new JPanel();
        newFreqLabel = new JLabel(NEW_FREQ_TEXT);
        currentFreqPanel = new JPanel();
        currentFreqLabel = new JLabel(CURR_FREQ_TEXT);
        frequencySlider = new FrequencySlider(LEVEL_SLIDER_SIZE, MIN, MAX,
            MAJOR_TICK, MINOR_TICK);
        previewButton = new Button("Preview", p);
        saveButton = new Button("Save modified file", s);

        mediaControlPanel = new InverseHorizontalBar(new BorderLayout());
        mediaControlPanel.setHeight(MEDIA_CONTROL_PANEL_HEIGHT);
        simplePlayerPanel = new SimplePlayerPanel(matlabHandler,
            mediaControlPanel);
        initInnerListeners();
        setStyle();
        addPanels();
        init();
    }

    /**
     * Returns the sampling rate shown by the slider.
     * @return Sampling rate
     */
    public int getFreq() {
        return frequencySlider.getValue();
    }

    /**
     * Set new sampling rate.
     * @param freq Sampling rate
     */
    public void setFreq(double freq) {
        frequencySlider.setValue((int) freq);
        currentFreqLabel.setText(String.format(CURR_FREQ_TEXT, freq));
        simplePlayerPanel.resetVolume();
        currentFreqPanel.setVisible(true);
        simplePlayerPanel.setVisible(true);
        mediaControlPanel.setVisible(true);
        saveButton.setVisible(true);
    }

    /**
     * Sets the current song.
     * @param totalSamples Total number of samples
     * @param freq Sampling rate
     * @param plot Plot image
     */
    public void setSong(double totalSamples, double freq, BufferedImage plot) {
        infoLabel.setText(String.format(INSTR_TEXT, freq));
        currentFreqLabel.setText(String.format(CURR_FREQ_TEXT, freq));
        newFreqLabel
            .setText(String.format(NEW_FREQ_TEXT, frequencySlider.getValue()));
        simplePlayerPanel.setCurrentSong(totalSamples, freq, plot);
    }

    /**
     * Hides the current song's settings.
     */
    public void removeSong() {
        simplePlayerPanel.setVisible(false);
        mediaControlPanel.setVisible(false);
        saveButton.setVisible(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setStyle() {
        setBackground(Color.BLACK);
        upperPanel.setOpaque(false);
        lowerPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        infoPanel.setOpaque(false);
        newFreqPanel.setOpaque(false);
        currentFreqPanel.setOpaque(false);
        infoLabel.setBorder(INFO_LABEL_BORDER);
        currentFreqPanel.setBorder(CURR_FREQ_BORDER);
        simplePlayerPanel.setBorder(PLAYER_PANEL_BORDER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPanels() {
        infoPanel.add(infoLabel);
        newFreqPanel.add(newFreqLabel);
        currentFreqPanel.add(currentFreqLabel);
        buttonPanel.add(previewButton);
        buttonPanel.add(saveButton);
        upperPanel.add(infoPanel);
        upperPanel.add(frequencySlider);
        upperPanel.add(newFreqPanel);
        upperPanel.add(buttonPanel);
        lowerPanel.add(currentFreqPanel);
        lowerPanel.add(simplePlayerPanel);
        lowerPanel.add(mediaControlPanel);
        add(upperPanel, BorderLayout.NORTH);
        add(lowerPanel, BorderLayout.SOUTH);

    }

    private void init() {
        currentFreqPanel.setVisible(false);
        mediaControlPanel.setVisible(false);
        simplePlayerPanel.setVisible(false);
        saveButton.setVisible(false);
    }

    private void changeFieldValue() {
        newFreqLabel.setText(String.format(NEW_FREQ_TEXT, frequencySlider
            .getValue()));
    }

    private void initInnerListeners() {
        frequencySlider
            .addChangeListener(new ChangePitchPanel.SliderListener());
    }

    /**
     * Slider listener.
     */
    private final class SliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            Object source = e.getSource();
            if (source == frequencySlider) {
                changeFieldValue();
            }
        }
    }
}
