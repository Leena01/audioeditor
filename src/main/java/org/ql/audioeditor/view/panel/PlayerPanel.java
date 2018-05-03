package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.common.util.DoubleActionTool;
import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.core.button.TransparentButton;
import org.ql.audioeditor.view.core.panel.BasicPanel;
import org.ql.audioeditor.view.core.slider.SliderTimer;
import org.ql.audioeditor.view.core.slider.TimeLabel;
import org.ql.audioeditor.view.core.slider.TrackSlider;
import org.ql.audioeditor.view.core.slider.VolumeSlider;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import static org.ql.audioeditor.common.util.Helper.resizeImageIcon;
import static org.ql.audioeditor.common.util.Helper.secondsToFrames;
import static org.ql.audioeditor.view.param.Constants.VOLUME_SLIDER_SIZE;

/**
 * Panel for playing media.
 */
class PlayerPanel extends BasicPanel {
    private static final String MOVE_BACKWARD = "moveBackwardAction";
    private static final String MOVE_FORWARD = "moveForwardAction";
    private static final String VOLUME_UP = "volumeUpAction";
    private static final String VOLUME_DOWN = "volumeDownAction";
    private static final String PAUSE = "pauseAction";
    private static final String STOP = "stopAction";
    private static final String MUTE = "muteAction";
    private static final String MAKE_FAVORITE = "makeFavoriteAction";
    private static final String SHOW_RELATED = "showRelatedAction";
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
    private static final Dimension BUTTON_SIZE = new Dimension(36, 36);
    private static final Dimension SOUND_BUTTON_SIZE = new Dimension(20, 20);

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
    private static final ImageIcon FAVORITE_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getFavoriteIcon()),
            BUTTON_SIZE);
    private static final ImageIcon UNFAVORITE_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getUnfavoriteIcon()),
            BUTTON_SIZE);
    private static final ImageIcon PLAYLIST_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getPlaylistIcon()),
            SOUND_BUTTON_SIZE);
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

    private final JLabel timeField;
    private final JLabel totalLengthField;
    private final TrackSlider trackSlider;
    private final JButton playButton;
    private final JButton pauseButton;
    private final JButton stopButton;
    private final JButton backwardButton;
    private final JButton forwardButton;
    private final JButton favoriteButton;
    private final JButton unfavoriteButton;
    private final JButton similarSongsButton;
    private final JButton soundOnButton;
    private final JButton soundOffButton;
    private final JPanel volumePanel;
    private final JPanel buttonPanel;
    private final HorizontalBar mediaControlPanel;
    private final VolumeSlider volumeSlider;
    private final MatlabHandler matlabHandler;
    private final SliderTimer sliderTimer;
    private final GridBagConstraints c;
    private final GridBagLayout gridBag;
    private final InputMap inputMap;
    private final ActionMap actionMap;
    private Thread playbackThread;
    private int framesToSkip;
    private int recentVolumeLevel;
    private boolean isPlaying;

    PlayerPanel(MatlabHandler matlabHandler, HorizontalBar mediaControlPanel,
        InputMap inputMap, ActionMap actionMap, ActionListener fb,
        ActionListener ufb, ActionListener sss) {
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
        this.inputMap = inputMap;
        this.actionMap = actionMap;
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
        favoriteButton = new TransparentButton(FAVORITE_ICON, BUTTON_SIZE, fb);
        unfavoriteButton =
            new TransparentButton(UNFAVORITE_ICON, BUTTON_SIZE, ufb);
        similarSongsButton =
            new TransparentButton(PLAYLIST_ICON, BUTTON_SIZE, sss);
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

    @Override
    protected void setStyle() {
        buttonPanel.setOpaque(false);
        volumePanel.setOpaque(false);
        mediaControlPanel.setBorder(MEDIA_CONTROL_PANEL_BORDER);
        trackSlider.setBorder(TRACK_SLIDER_BORDER);
        timeField.setBorder(TIME_FIELD_BORDER);
        totalLengthField.setBorder(TOTAL_LENGTH_FIELD_BORDER);
    }

    @Override
    protected void addPanels() {
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
        buttonPanel.add(favoriteButton);
        buttonPanel.add(unfavoriteButton);
        buttonPanel.add(volumePanel);
        volumePanel.add(similarSongsButton);
        volumePanel.add(soundOnButton);
        volumePanel.add(soundOffButton);
        volumePanel.add(volumeSlider);
        mediaControlPanel.add(buttonPanel, BorderLayout.CENTER);
        mediaControlPanel.add(volumePanel, BorderLayout.EAST);
    }

    void setCurrentSong(double totalSamples, double freq, BufferedImage plot,
        Action makeFavoriteAction, Action showRelatedAction) {
        stopSong();
        mediaControlPanel.setVisible(true);
        framesToSkip = secondsToFrames(SECONDS_TO_SKIP, freq);
        sliderTimer.schedule(REFRESH_MILLIS, totalSamples, freq);
        if (plot != null) {
            trackSlider.setImage(plot);
        }
        initKeyBindings(makeFavoriteAction, showRelatedAction);
    }

    void setFavorite(boolean isFavorite) {
        if (isFavorite) {
            favoriteButton.setVisible(false);
            unfavoriteButton.setVisible(true);
        } else {
            unfavoriteButton.setVisible(false);
            favoriteButton.setVisible(true);
        }
    }

    private void playSong() {
        playButton.setVisible(false);
        pauseButton.setVisible(true);
        sliderTimer.resumeTimer();
        playbackThread = new Thread(() -> matlabHandler.resumeSong());
        playbackThread.start();
        isPlaying = true;
    }

    void pauseSong() {
        pauseButton.setVisible(false);
        playButton.setVisible(true);
        sliderTimer.pauseTimer();
        playbackThread = new Thread(() -> matlabHandler.pauseSong());
        playbackThread.start();
        isPlaying = false;
    }

    private void stopSong() {
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

    private void moveBackward() {
        int newValue = trackSlider.getValue() - framesToSkip;
        sliderTimer.changeTime(newValue);
        playbackThread = new Thread(() -> matlabHandler.relocateSong(newValue));
        playbackThread.start();
    }

    private void moveForward() {
        int newValue = trackSlider.getValue() + framesToSkip;
        sliderTimer.changeTime(newValue);
        playbackThread = new Thread(() -> matlabHandler.relocateSong(newValue));
        playbackThread.start();
    }

    private void volumeUp() {
        recentVolumeLevel = volumeSlider.getValue() + LEVELS_TO_SKIP;
        volumeSlider.setValue(recentVolumeLevel);
    }

    private void volumeDown() {
        recentVolumeLevel = volumeSlider.getValue() - LEVELS_TO_SKIP;
        volumeSlider.setValue(recentVolumeLevel);
    }

    private void turnVolumeOff() {
        recentVolumeLevel = volumeSlider.getValue();
        soundOnButton.setVisible(false);
        soundOffButton.setVisible(true);
        volumeSlider.setValue(NULL_VOLUME);
    }

    private void turnVolumeOn() {
        soundOffButton.setVisible(false);
        soundOnButton.setVisible(true);
        volumeSlider.setValue(recentVolumeLevel);
    }

    private boolean isMute() {
        return volumeSlider.getValue() == NULL_VOLUME;
    }

    private boolean isPlaying() {
        return isPlaying;
    }

    private void initKeyBindings(Action makeFavoriteAction,
        Action showRelatedAction) {
        Action moveBackwardAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!DoubleActionTool.isDoubleAction(e)) {
                    moveBackward();
                }
            }
        };
        Action moveForwardAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!DoubleActionTool.isDoubleAction(e)) {
                    moveForward();
                }
            }
        };
        Action volumeUpAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!DoubleActionTool.isDoubleAction(e)) {
                    volumeUp();
                }
            }
        };
        Action volumeDownAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!DoubleActionTool.isDoubleAction(e)) {
                    volumeDown();
                }
            }
        };
        Action pauseAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!DoubleActionTool.isDoubleAction(e)) {
                    if (isPlaying()) {
                        pauseSong();
                    } else {
                        playSong();
                    }
                }
            }
        };
        Action stopAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!DoubleActionTool.isDoubleAction(e)) {
                    stopSong();
                }
            }
        };
        Action muteAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!DoubleActionTool.isDoubleAction(e)) {
                    if (isMute()) {
                        turnVolumeOn();
                    } else {
                        turnVolumeOff();
                    }
                }
            }
        };

        KeyStroke leftKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
        KeyStroke rightKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
        KeyStroke upKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
        KeyStroke downKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
        KeyStroke pKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_P, 0);
        KeyStroke sKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0);
        KeyStroke mKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_M, 0);
        KeyStroke fKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F, 0);
        KeyStroke rKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, 0);
        inputMap.put(leftKeyStroke, MOVE_BACKWARD);
        actionMap.put(MOVE_BACKWARD, moveBackwardAction);
        inputMap.put(rightKeyStroke, MOVE_FORWARD);
        actionMap.put(MOVE_FORWARD, moveForwardAction);
        inputMap.put(upKeyStroke, VOLUME_UP);
        actionMap.put(VOLUME_UP, volumeUpAction);
        inputMap.put(downKeyStroke, VOLUME_DOWN);
        actionMap.put(VOLUME_DOWN, volumeDownAction);
        inputMap.put(pKeyStroke, PAUSE);
        actionMap.put(PAUSE, pauseAction);
        inputMap.put(sKeyStroke, STOP);
        actionMap.put(STOP, stopAction);
        inputMap.put(mKeyStroke, MUTE);
        actionMap.put(MUTE, muteAction);
        inputMap.put(fKeyStroke, MAKE_FAVORITE);
        actionMap.put(MAKE_FAVORITE, makeFavoriteAction);
        inputMap.put(rKeyStroke, SHOW_RELATED);
        actionMap.put(SHOW_RELATED, showRelatedAction);
    }

    private void initInnerListeners() {
        sliderTimer.addObserver(new InnerObserver());
        volumeSlider.addChangeListener(new InnerChangeListener());
        backwardButton.addMouseListener(new InnerMouseListener());
        playButton.addMouseListener(new InnerMouseListener());
        pauseButton.addMouseListener(new InnerMouseListener());
        stopButton.addMouseListener(new InnerMouseListener());
        forwardButton.addMouseListener(new InnerMouseListener());
        soundOnButton.addMouseListener(new InnerMouseListener());
        soundOffButton.addMouseListener(new InnerMouseListener());
    }

    private void init() {
        pauseButton.setVisible(false);
        soundOffButton.setVisible(false);
        setFavorite(false);
    }

    /**
     * Mouse listener for media control buttons. Ignores double clicks.
     */
    private final class InnerMouseListener implements MouseListener {
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

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     * Change listener for sliders.
     */
    private final class InnerChangeListener implements ChangeListener {
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
        @Override
        public void update(Observable obs, Object obj) {
            pauseButton.setVisible(false);
            playButton.setVisible(true);
        }
    }
}
