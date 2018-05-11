package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.panel.SimplePlayerPanel;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import static org.ql.audioeditor.view.param.Constants.PLAYER_PANEL_BORDER;

/**
 * Panel for onset detection.
 */
public final class OnsetDetPanel extends DetPanel {
    private final SimplePlayerPanel simplePlayerPanel;
    private boolean mediaControlActive;

    /**
     * Constructor.
     *
     * @param matlabHandler     MATLAB handler
     * @param mediaControlPanel Media control panel
     * @param d                 DoneButton listener
     */
    public OnsetDetPanel(MatlabHandler matlabHandler, HorizontalBar
        mediaControlPanel, ActionListener d) {
        super(d);
        simplePlayerPanel = new SimplePlayerPanel(matlabHandler,
            mediaControlPanel);
        setStyleAdditional();
        addPanelsAdditional();
    }

    /**
     * Sets image.
     *
     * @param totalSamples Total number of samples
     * @param freq         Sample rate
     * @param plot         Plot
     */
    public void setOnsetImage(double totalSamples, double freq, BufferedImage
        plot) {
        simplePlayerPanel.setCurrentSong(totalSamples, freq, plot);
        simplePlayerPanel.setVisible(true);
        mediaControlActive = true;
    }

    /**
     * Hides the current song's settings.
     */
    public void removeSong() {
        simplePlayerPanel.setVisible(false);
        mediaControlActive = false;
    }

    /**
     * Returns whether the media controls are active.
     *
     * @return Is media control active
     */
    public boolean isMediaControlActive() {
        return mediaControlActive;
    }

    /**
     * Stops the current song.
     */
    public void stopSong() {
        simplePlayerPanel.stopSong();
    }

    /**
     * Sets the volume to default.
     */
    public void resetVolume() {
        simplePlayerPanel.resetVolume();
    }

    private void setStyleAdditional() {
        simplePlayerPanel.setBorder(PLAYER_PANEL_BORDER);
    }

    private void addPanelsAdditional() {
        mainPanel.add(simplePlayerPanel);
    }
}
