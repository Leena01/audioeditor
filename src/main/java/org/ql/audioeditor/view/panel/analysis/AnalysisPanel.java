package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.logic.matlab.MatlabHandler;
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

import static org.ql.audioeditor.view.param.Constants.BACK_TO_MAIN_MENU_TEXT;

/**
 * Panel for analysis.
 */
public final class AnalysisPanel extends BasicPanel {
    private static final String BEAT_EST_PANEL = "Beat estimation panel";
    private static final String ONSET_DET_PANEL = "Onset detection panel";
    private static final Border BUTTON_PANEL_BORDER =
        BorderFactory.createEmptyBorder(20, 10, 20, 10);
    private final JButton beatButton;
    private final JButton onsetButton;
    private final JButton keyButton;
    private final JButton backOptionButton;
    private final JPanel buttonPanel;
    private final BeatEstPanel beatEstPanel;
    private final OnsetDetPanel onsetDetPanel;
    private final CardLayout cardLayout;
    private final JPanel resultPanel;
    private final JPanel mainPanel;

    /**
     * Constructor.
     *
     * @param matlabHandler MATLAB handler
     * @param be            BeatButton listener
     * @param o             OnsetButton listener
     * @param k             KeyButton listener
     * @param b             BackOptionButton listener
     * @param bed           BeatEstPanel doneButton listener
     * @param od            OnsetDetPanel doneButton listener
     */
    public AnalysisPanel(MatlabHandler matlabHandler, ActionListener be,
        ActionListener o, ActionListener k, ActionListener b, ActionListener
        bed, ActionListener od) {
        super();
        beatButton = new Button("Beat estimation", be);
        onsetButton = new Button("Onset detection", o);
        keyButton = new Button("Key detection", k);
        backOptionButton = new Button(BACK_TO_MAIN_MENU_TEXT, b);
        buttonPanel = new JPanel(new FlowLayout());

        beatEstPanel = new BeatEstPanel(bed);
        onsetDetPanel = new OnsetDetPanel(matlabHandler, od);

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
        cardLayout.show(resultPanel, BEAT_EST_PANEL);
    }

    /**
     * Shows OnsetDetPanel.
     */
    public void setOnsetDetOn() {
        cardLayout.show(resultPanel, ONSET_DET_PANEL);
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
     * Resets the fields to their default value.
     */
    public void resetFields() {
        beatEstPanel.resetFields();
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
     * Removes the images.
     */
    public void removeImages() {
        onsetDetPanel.removeImages();
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
     * Returns the BPM.
     *
     * @return BPM
     */
    public String getBpm() {
        return onsetDetPanel.getBpm();
    }

    /**
     * Returns the filter size.
     *
     * @return Filter size
     */
    public String getSmoothness() {
        return onsetDetPanel.getSmoothness();
    }

    /**
     * Returns the base note value.
     *
     * @return Base note value
     */
    public String getBase() {
        return onsetDetPanel.getBase();
    }

    /**
     * Returns the smallest note value.
     *
     * @return Smallest note value
     */
    public String getSmallest() {
        return onsetDetPanel.getSmallest();
    }

    /**
     * Shows the plot.
     *
     * @param totalSamples Total number of samples
     * @param freq         Sampling rate
     * @param plot         Plot
     */
    public void setOnsetImage(double totalSamples, double freq, BufferedImage
        plot) {
        onsetDetPanel.setOnsetImage(totalSamples, freq, plot);
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
        resultPanel.add(beatEstPanel, BEAT_EST_PANEL);
        resultPanel.add(onsetDetPanel, ONSET_DET_PANEL);
        mainPanel.add(buttonPanel);
        mainPanel.add(resultPanel);
        add(mainPanel);
    }
}
