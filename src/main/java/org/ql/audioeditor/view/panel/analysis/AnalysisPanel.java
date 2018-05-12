package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.panel.BasicPanel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import static org.ql.audioeditor.view.enums.PanelName.BEAT_EST_PANEL;
import static org.ql.audioeditor.view.enums.PanelName.KEY_DET_PANEL;
import static org.ql.audioeditor.view.enums.PanelName.ONSET_DET_PANEL;
import static org.ql.audioeditor.view.param.Constants.BACK_TO_MAIN_MENU_TEXT;

/**
 * Panel for analysis.
 */
public final class AnalysisPanel extends BasicPanel {
    private static final Border BUTTON_PANEL_BORDER =
        BorderFactory.createEmptyBorder(20, 10, 20, 10);
    private final JButton beatButton;
    private final JButton onsetButton;
    private final JButton keyButton;
    private final JButton backOptionButton;
    private final JPanel buttonPanel;
    private final BeatEstPanel beatEstPanel;
    private final OnsetDetPanel onsetDetPanel;
    private final KeyDetPanel keyDetPanel;
    private final CardLayout cardLayout;
    private final JPanel resultPanel;
    private final JPanel mainPanel;

    /**
     * Constructor.
     *
     * @param matlabHandler     MATLAB handler
     * @param mediaControlPanel Media control panel
     * @param be                BeatButton listener
     * @param o                 OnsetButton listener
     * @param k                 KeyButton listener
     * @param b                 BackOptionButton listener
     * @param bed               BeatEstPanel doneButton listener
     * @param od                OnsetDetPanel doneButton listener
     * @param kd                KeyDetPanel doneButton listener
     */
    public AnalysisPanel(MatlabHandler matlabHandler, HorizontalBar
        mediaControlPanel, ActionListener be, ActionListener o,
        ActionListener k, ActionListener b, ActionListener
        bed, ActionListener od, ActionListener kd) {
        super();
        beatButton = new Button("Beat estimation", be);
        onsetButton = new Button("Onset detection", o);
        keyButton = new Button("Key detection", k);
        backOptionButton = new Button(BACK_TO_MAIN_MENU_TEXT, b);
        buttonPanel = new JPanel(new FlowLayout());

        beatEstPanel = new BeatEstPanel(bed);
        onsetDetPanel = new OnsetDetPanel(matlabHandler, mediaControlPanel, od);
        keyDetPanel = new KeyDetPanel(kd);

        cardLayout = new CardLayout();
        resultPanel = new JPanel(cardLayout);
        mainPanel = new JPanel();
        setStyle();
        addPanels();
    }

    /**
     * Shows BeatEstPanel.
     */
    public void setBeatEstOn() {
        cardLayout.show(resultPanel, BEAT_EST_PANEL.toString());
    }

    /**
     * Shows OnsetDetPanel.
     */
    public void setOnsetDetOn() {
        cardLayout.show(resultPanel, ONSET_DET_PANEL.toString());
    }

    /**
     * Shows KeyDetPanel.
     */
    public void setKeyDetOn() {
        cardLayout.show(resultPanel, KEY_DET_PANEL.toString());
    }

    /**
     * Stops the current song.
     */
    public void stopSong() {
        onsetDetPanel.stopSong();
    }

    /**
     * Sets the volume to default.
     */
    public void resetVolume() {
        onsetDetPanel.resetVolume();
    }

    /**
     * Returns the minimum BPM entered by the user.
     *
     * @return Minimum BPM
     */
    public int getMinBpm() {
        return beatEstPanel.getMinBpm();
    }

    /**
     * Returns the maximum BPM entered by the user.
     *
     * @return Maximum BPM
     */
    public int getMaxBpm() {
        return beatEstPanel.getMaxBpm();
    }

    /**
     * Shows the beat estimation.
     *
     * @param est Estimated BPM
     */
    public void showEstimation(int est) {
        beatEstPanel.showEstimation(est);
    }

    /**
     * Shows the key estimation.
     *
     * @param est Estimated key
     */
    public void showEstimation(String est) {
        keyDetPanel.showEstimation(est);
    }

    /**
     * Resets the fields to their default value.
     */
    public void resetFields() {
        beatEstPanel.resetFields();
        onsetDetPanel.resetFields();
        keyDetPanel.resetFields();
    }

    /**
     * Hides image.
     *
     * @param isHidden Is the image hidden
     */
    public void hideImage(boolean isHidden) {
        onsetDetPanel.hideImage(isHidden);
    }

    /**
     * Maximizes the image.
     *
     * @param isMaximized Is the image maximized
     */
    public void maximizeImage(boolean isMaximized) {
        onsetDetPanel.maximizeImage(isMaximized);
    }

    /**
     * Returns whether the media controls are active.
     *
     * @return Is media control active
     */
    public boolean isMediaControlActive() {
        return onsetDetPanel.isMediaControlActive() && onsetDetPanel
            .isVisible();
    }

    /**
     * Returns the BPM.
     *
     * @return BPM
     */
    public String getBpmOnset() {
        return onsetDetPanel.getBpm();
    }

    /**
     * Returns the filter size.
     *
     * @return Filter size
     */
    public String getSmoothnessOnset() {
        return onsetDetPanel.getSmoothness();
    }

    /**
     * Returns the base note value.
     *
     * @return Base note value
     */
    public Integer getBaseOnset() {
        return onsetDetPanel.getBase();
    }

    /**
     * Returns the smallest note value.
     *
     * @return Smallest note value
     */
    public Integer getSmallestOnset() {
        return onsetDetPanel.getSmallest();
    }

    /**
     * Returns the BPM.
     *
     * @return BPM
     */
    public String getBpmKey() {
        return keyDetPanel.getBpm();
    }

    /**
     * Returns the filter size.
     *
     * @return Filter size
     */
    public String getSmoothnessKey() {
        return keyDetPanel.getSmoothness();
    }

    /**
     * Returns the base note value.
     *
     * @return Base note value
     */
    public Integer getBaseKey() {
        return keyDetPanel.getBase();
    }

    /**
     * Returns the smallest note value.
     *
     * @return Smallest note value
     */
    public Integer getSmallestKey() {
        return keyDetPanel.getSmallest();
    }

    /**
     * Shows the plot.
     *
     * @param totalSamples Total number of samples
     * @param freq         Sample rate
     * @param plot         Plot
     */
    public void setOnsetImage(double totalSamples, double freq, BufferedImage
        plot) {
        onsetDetPanel.setOnsetImage(totalSamples, freq, plot);
    }

    /**
     * Hides the current song's settings.
     */
    public void removeSong() {
        beatEstPanel.hideEstimation();
        onsetDetPanel.removeSong();
        keyDetPanel.hideEstimation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setStyle() {
        setBackground(Color.BLACK);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        resultPanel.setOpaque(false);
        mainPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BUTTON_PANEL_BORDER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPanels() {
        buttonPanel.add(beatButton);
        buttonPanel.add(onsetButton);
        buttonPanel.add(keyButton);
        buttonPanel.add(backOptionButton);
        resultPanel.add(beatEstPanel, BEAT_EST_PANEL.toString());
        resultPanel.add(onsetDetPanel, ONSET_DET_PANEL.toString());
        resultPanel.add(keyDetPanel, KEY_DET_PANEL.toString());
        mainPanel.add(buttonPanel);
        mainPanel.add(resultPanel);
        add(mainPanel);
    }
}
