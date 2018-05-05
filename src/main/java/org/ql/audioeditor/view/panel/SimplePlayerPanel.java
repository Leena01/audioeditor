package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.core.button.TransparentButton;
import org.ql.audioeditor.view.core.panel.BasicPanel;
import org.ql.audioeditor.view.core.slider.SliderTimer;
import org.ql.audioeditor.view.core.slider.TimeLabel;
import org.ql.audioeditor.view.core.slider.TrackSlider;
import org.ql.audioeditor.view.core.slider.VolumeSlider;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import static org.ql.audioeditor.common.util.TimeUtils.secondsToFrames;
import static org.ql.audioeditor.common.util.ViewUtils.resizeImageIcon;
import static org.ql.audioeditor.view.param.Constants.VOLUME_SLIDER_SIZE;

/**
 * Panel for playing media.
 */
public class SimplePlayerPanel extends BasicPanel {
    protected static final Dimension SOUND_BUTTON_SIZE = new Dimension(20, 20);
    protected static final Dimension BUTTON_SIZE = new Dimension(36, 36);
    private static final int NULL_VOLUME = 0;
    private static final int VOLUME_MIN = SongPropertiesLoader.getVolumeMin();
    private static final int VOLUME_MAX = SongPropertiesLoader.getVolumeMax();
    private static final int VOLUME_INIT = SongPropertiesLoader.getVolumeInit();
    private static final float CONVERSION_RATE =
        SongPropertiesLoader.getVolumeConversionRate();
    private static final int REFRESH_MILLIS =
        SongPropertiesLoader.getSongRefreshMillis();
    private static final int SECONDS_TO_SKIP =
        SongPropertiesLoader.getSecondsToSkip();
    private static final int LEVELS_TO_SKIP = (VOLUME_MAX - VOLUME_MIN) / 10;
    private static final ImageIcon PLAY_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getPlayIcon()),
            BUTTON_SIZE);
    private static final ImageIcon PAUSE_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getPauseIcon()),
            BUTTON_SIZE);
    private static final ImageIcon STOP_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getStopIcon()),
            BUTTON_SIZE);
    private static final ImageIcon BACKWARD_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getBackwardIcon()),
            BUTTON_SIZE);
    private static final ImageIcon FORWARD_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getForwardIcon()),
            BUTTON_SIZE);
    private static final ImageIcon SOUND_ON_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getSoundOnIcon()),
            SOUND_BUTTON_SIZE);
    private static final ImageIcon SOUND_OFF_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getSoundOffIcon()),
            SOUND_BUTTON_SIZE);

    private static final Border MEDIA_CONTROL_PANEL_BORDER =
        BorderFactory.createEmptyBorder(40, 10, 10, 10);
    private static final Border TRACK_SLIDER_BORDER =
        BorderFactory.createEmptyBorder(0, 0, 10, 0);
    private static final Border TIME_FIELD_BORDER =
        BorderFactory.createEmptyBorder(0, 0, 0, 10);
    private static final Border TOTAL_LENGTH_FIELD_BORDER =
        BorderFactory.createEmptyBorder(0, 10, 0, 0);
    private static final Dimension FIELD_DIMENSION = new Dimension(80, 10);

    protected final JPanel volumePanel;
    protected final JPanel buttonPanel;
    private final JLabel timeField;
    private final JLabel totalLengthField;
    private final TrackSlider trackSlider;
    private final JButton playButton;
    private final JButton pauseButton;
    private final JButton stopButton;
    private final JButton backwardButton;
    private final JButton forwardButton;
    private final JButton soundOnButton;
    private final JButton soundOffButton;
    private final HorizontalBar mediaControlPanel;
    private final VolumeSlider volumeSlider;
    private final MatlabHandler matlabHandler;
    private final SliderTimer sliderTimer;
    private final GridBagConstraints c;
    private final GridBagLayout gridBag;
    private Thread playbackThread;
    private int framesToSkip;
    private int recentVolumeLevel;
    private boolean isPlaying;

    /**
     * Constructor.
     *
     * @param matlabHandler     Matlab handler
     * @param mediaControlPanel Media control panel
     */
    SimplePlayerPanel(MatlabHandler matlabHandler,
        HorizontalBar mediaControlPanel) {
        super();
        this.playbackThread = new Thread();
        playbackThread.start();
        gridBag = new GridBagLayout();
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        gridBag.setConstraints(this, c);
        setLayout(gridBag);
        this.matlabHandler = matlabHandler;
        this.mediaControlPanel = mediaControlPanel;
        framesToSkip = 0;
        recentVolumeLevel = VOLUME_INIT;
        isPlaying = false;

        timeField = new TimeLabel(FIELD_DIMENSION, SwingConstants.RIGHT);
        trackSlider = new TrackSlider(JSlider.HORIZONTAL);
        totalLengthField = new TimeLabel(FIELD_DIMENSION, SwingConstants.LEFT);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playButton = new TransparentButton(PLAY_ICON, BUTTON_SIZE);
        pauseButton = new TransparentButton(PAUSE_ICON, BUTTON_SIZE);
        stopButton = new TransparentButton(STOP_ICON, BUTTON_SIZE);
        backwardButton = new TransparentButton(BACKWARD_ICON, BUTTON_SIZE);
        forwardButton = new TransparentButton(FORWARD_ICON, BUTTON_SIZE);
        soundOnButton = new TransparentButton(SOUND_ON_ICON, BUTTON_SIZE);
        soundOffButton = new TransparentButton(SOUND_OFF_ICON, BUTTON_SIZE);

        volumePanel = new JPanel(new FlowLayout());
        volumeSlider = new VolumeSlider(VOLUME_SLIDER_SIZE, VOLUME_MIN,
            VOLUME_MAX, VOLUME_INIT);
        sliderTimer = new SliderTimer(trackSlider, matlabHandler, timeField,
            totalLengthField, true);

        setStyle();
        addPanels();
        initInnerListeners();
        init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void setStyle() {
        setOpaque(false);
        buttonPanel.setOpaque(false);
        volumePanel.setOpaque(false);
        mediaControlPanel.setBorder(MEDIA_CONTROL_PANEL_BORDER);
        trackSlider.setBorder(TRACK_SLIDER_BORDER);
        timeField.setBorder(TIME_FIELD_BORDER);
        totalLengthField.setBorder(TOTAL_LENGTH_FIELD_BORDER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void addPanels() {
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        add(timeField, c);

        c.gridx = 1;
        add(trackSlider, c);

        c.gridx = 2;
        add(totalLengthField, c);

        buttonPanel.add(backwardButton);
        buttonPanel.add(playButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(forwardButton);
        volumePanel.add(soundOnButton);
        volumePanel.add(soundOffButton);
        volumePanel.add(volumeSlider);

        mediaControlPanel.add(buttonPanel, BorderLayout.CENTER);
        mediaControlPanel.add(volumePanel, BorderLayout.EAST);
    }

    /**
     * Sets the current song.
     *
     * @param totalSamples Total number of samples
     * @param freq         Sampling rate
     * @param plot         Plot
     */
    void setCurrentSong(double totalSamples, double freq, BufferedImage plot) {
        framesToSkip = secondsToFrames(SECONDS_TO_SKIP, freq);
        sliderTimer.schedule(REFRESH_MILLIS, totalSamples, freq);
        if (plot != null) {
            trackSlider.setImage(plot);
        }
    }

    /**
     * Plays the current song.
     */
    protected void playSong() {
        playButton.setVisible(false);
        pauseButton.setVisible(true);
        sliderTimer.resumeTimer();
        playbackThread = new Thread(() -> matlabHandler.resumeSong());
        playbackThread.start();
        isPlaying = true;
    }

    /**
     * Pauses the current song.
     */
    protected void pauseSong() {
        pauseButton.setVisible(false);
        playButton.setVisible(true);
        sliderTimer.pauseTimer();
        playbackThread = new Thread(() -> matlabHandler.pauseSong());
        playbackThread.start();
        isPlaying = false;
    }

    /**
     * Stops the current song.
     */
    protected void stopSong() {
        pauseButton.setVisible(false);
        playButton.setVisible(true);
        playbackThread = new Thread(() -> {
            matlabHandler.stopSong();
            SwingUtilities.invokeLater(
                () -> sliderTimer.stopTimer());
        });
        playbackThread.start();
        isPlaying = false;
    }

    /**
     * Moves the track slider's cursor backward. It skips the number of frames
     * specified in framesToSkip.
     */
    protected void moveBackward() {
        int newValue = trackSlider.getValue() - framesToSkip;
        sliderTimer.changeTime(newValue);
        playbackThread = new Thread(() -> matlabHandler.relocateSong(newValue));
        playbackThread.start();
    }

    /**
     * Moves the track slider's cursor forward. It skips the number of frames
     * specified in framesToSkip.
     */
    protected void moveForward() {
        int newValue = trackSlider.getValue() + framesToSkip;
        sliderTimer.changeTime(newValue);
        playbackThread = new Thread(() -> matlabHandler.relocateSong(newValue));
        playbackThread.start();
    }

    /**
     * Turns the volume up.
     */
    protected void volumeUp() {
        recentVolumeLevel = volumeSlider.getValue() + LEVELS_TO_SKIP;
        volumeSlider.setValue(recentVolumeLevel);
    }

    /**
     * Turns the volume down.
     */
    protected void volumeDown() {
        recentVolumeLevel = volumeSlider.getValue() - LEVELS_TO_SKIP;
        volumeSlider.setValue(recentVolumeLevel);
    }

    /**
     * Turns the volume off.
     */
    protected void turnVolumeOff() {
        recentVolumeLevel = volumeSlider.getValue();
        soundOnButton.setVisible(false);
        soundOffButton.setVisible(true);
        volumeSlider.setValue(NULL_VOLUME);
    }

    /**
     * Turns the volume on.
     */
    protected void turnVolumeOn() {
        soundOffButton.setVisible(false);
        soundOnButton.setVisible(true);
        volumeSlider.setValue(recentVolumeLevel);
    }

    /**
     * Sets the volume to default.
     */
    protected void resetVolume() {
        soundOffButton.setVisible(false);
        soundOnButton.setVisible(true);
        volumeSlider.setValue(VOLUME_INIT);
    }

    /**
     * Returns whether the current song is mute.
     *
     * @return Logical value (true if mute)
     */
    protected boolean isMute() {
        return volumeSlider.getValue() == NULL_VOLUME;
    }

    /**
     * Returns whether the current song is playing.
     *
     * @return Logical value (true if playing)
     */
    protected boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Initializes listeners.
     */
    private void initInnerListeners() {
        sliderTimer.addObserver(
            new SimplePlayerPanel.InnerObserver());
        volumeSlider.addChangeListener(
            new SimplePlayerPanel.InnerChangeListener());
        backwardButton.addMouseListener(
            new SimplePlayerPanel.InnerMouseListener());
        playButton.addMouseListener(
            new SimplePlayerPanel.InnerMouseListener());
        pauseButton.addMouseListener(
            new SimplePlayerPanel.InnerMouseListener());
        stopButton.addMouseListener(
            new SimplePlayerPanel.InnerMouseListener());
        forwardButton.addMouseListener(
            new SimplePlayerPanel.InnerMouseListener());
        soundOnButton.addMouseListener(
            new SimplePlayerPanel.InnerMouseListener());
        soundOffButton.addMouseListener(
            new SimplePlayerPanel.InnerMouseListener());
    }

    /**
     * Initializes GUI elements.
     */
    private void init() {
        pauseButton.setVisible(false);
        soundOffButton.setVisible(false);
    }

    /**
     * Mouse listener for media control buttons. Ignores double clicks.
     */
    private final class InnerMouseListener implements MouseListener {
        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            Object source = e.getSource();
            if (e.getClickCount() == 1) {
                if (source == playButton) {
                    playSong();
                } else if (source == pauseButton) {
                    pauseSong();
                } else if (source == stopButton) {
                    stopSong();
                } else if (source == soundOnButton) {
                    turnVolumeOff();
                } else if (source == soundOffButton) {
                    turnVolumeOn();
                }
            }
            if (source == backwardButton) {
                moveBackward();
            } else if (source == forwardButton) {
                moveForward();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mousePressed(MouseEvent e) {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased(MouseEvent e) {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseEntered(MouseEvent e) {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     * Change listener for sliders.
     */
    private final class InnerChangeListener implements ChangeListener {
        /**
         * {@inheritDoc}
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            Object source = e.getSource();
            if (source == volumeSlider) {
                JSlider vs = (JSlider) source;
                float level = vs.getValue() / CONVERSION_RATE;
                if (!vs.getValueIsAdjusting()) {
                    playbackThread =
                        new Thread(() -> matlabHandler.changeVolume(level));
                    playbackThread.start();
                }
            }
        }
    }

    /**
     * Observer for media control buttons.
     */
    private final class InnerObserver implements Observer {
        /**
         * {@inheritDoc}
         */
        @Override
        public void update(Observable obs, Object obj) {
            pauseButton.setVisible(false);
            playButton.setVisible(true);
        }
    }
}
