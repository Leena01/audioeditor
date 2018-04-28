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

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import static org.ql.audioeditor.common.util.Helper.resizeImageIcon;
import static org.ql.audioeditor.common.util.Helper.secondsToFrames;
import static org.ql.audioeditor.view.param.Constants.VOLUME_SLIDER_SIZE;

class PlayerPanel extends BasicPanel
    implements MouseListener, ChangeListener, Observer {
    private static final String MOVE_BACKWARD = "moveBackwardAction";
    private static final String MOVE_FORWARD = "moveForwardAction";
    private static final String VOLUME_UP = "volumeUpAction";
    private static final String VOLUME_DOWN = "volumeDownAction";
    private static final String PAUSE = "pauseAction";
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
        resizeImageIcon(new ImageIcon(ImageLoader.getPlayIconURL()),
            BUTTON_SIZE);
    private static final ImageIcon PAUSE_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getPauseIconURL()),
            BUTTON_SIZE);
    private static final ImageIcon STOP_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getStopIconURL()),
            BUTTON_SIZE);
    private static final ImageIcon BACKWARD_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getBackwardIconURL()),
            BUTTON_SIZE);
    private static final ImageIcon FORWARD_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getForwardIconURL()),
            BUTTON_SIZE);
    private static final ImageIcon FAVORITE_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getFavoriteIconURL()),
            BUTTON_SIZE);
    private static final ImageIcon UNFAVORITE_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getUnfavoriteIconURL()),
            BUTTON_SIZE);
    private static final ImageIcon SOUND_ON_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getSoundOnIconURL()),
            SOUND_BUTTON_SIZE);
    private static final ImageIcon SOUND_OFF_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getSoundOffIconURL()),
            SOUND_BUTTON_SIZE);

    private static final Border MEDIA_CONTROL_PANEL_BORDER =
        BorderFactory.createEmptyBorder(40, 10, 10, 10);
    private static final Border TRACK_SLIDER_BORDER =
        BorderFactory.createEmptyBorder(0, 0, 10, 0);
    private static final Border TIME_FIELD_BORDER =
        BorderFactory.createEmptyBorder(0, 0, 0, 10);
    private static final Border TOTAL_LENGTH_FIELD_BORDER =
        BorderFactory.createEmptyBorder(0, 10, 0, 0);
    private static Dimension FIELD_DIMENSION = new Dimension(80, 10);

    private JLabel timeField;
    private JLabel totalLengthField;
    private TrackSlider trackSlider;
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JButton backwardButton;
    private JButton forwardButton;
    private JButton favoriteButton;
    private JButton unfavoriteButton;
    private JButton soundOnButton;
    private JButton soundOffButton;
    private JPanel volumePanel;
    private JPanel buttonPanel;
    private HorizontalBar mediaControlPanel;
    private VolumeSlider volumeSlider;
    private MatlabHandler matlabHandler;
    private SliderTimer sliderTimer;
    private GridBagConstraints c;
    private GridBagLayout gridBag;
    private Thread playbackThread;
    private InputMap inputMap;
    private ActionMap actionMap;
    private int framesToSkip;
    private int recentVolumeLevel;

    PlayerPanel(MatlabHandler matlabHandler, HorizontalBar mediaControlPanel,
        InputMap inputMap, ActionMap actionMap, ActionListener fb,
        ActionListener ufb) {
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
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if (e.getClickCount() == 1) {
            if (source == playButton) {
                playSong();
            }
            else if (source == pauseButton) {
                pauseSong();
            }
            else if (source == stopButton) {
                stopSong();
            }
            else if (source == soundOnButton) {
                turnVolumeOff();
            }
            else if (source == soundOffButton) {
                turnVolumeOn();
            }
        }
        if (source == backwardButton) {
            moveBackward();
        }
        else if (source == forwardButton) {
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

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == volumeSlider) {
            JSlider vs = (JSlider) source;
            float level = vs.getValue() / CONVERSION_RATE;
            if (!vs.getValueIsAdjusting()) {
                playbackThread.interrupt();
                playbackThread =
                    new Thread(() -> matlabHandler.changeVolume(level));
                playbackThread.start();
            }
        }
    }

    @Override
    public void update(Observable obs, Object obj) {
        pauseButton.setVisible(false);
        playButton.setVisible(true);
    }

    void setCurrentSong(double totalSamples, double freq, BufferedImage plot) {
        stopSong();
        mediaControlPanel.setVisible(true);
        framesToSkip = secondsToFrames(SECONDS_TO_SKIP, freq);
        sliderTimer.schedule(REFRESH_MILLIS, totalSamples, freq);
        trackSlider.setImage(plot);
        initKeyBindings();
    }

    void setFavorite(boolean isFavorite) {
        if (isFavorite) {
            favoriteButton.setVisible(false);
            unfavoriteButton.setVisible(true);
        }
        else {
            unfavoriteButton.setVisible(false);
            favoriteButton.setVisible(true);
        }
    }

    private void playSong() {
        playbackThread.interrupt();
        playButton.setVisible(false);
        pauseButton.setVisible(true);
        sliderTimer.resumeTimer();
        playbackThread = new Thread(() -> matlabHandler.resumeSong());
        playbackThread.start();
    }

    void pauseSong() {
        playbackThread.interrupt();
        pauseButton.setVisible(false);
        playButton.setVisible(true);
        sliderTimer.pauseTimer();
        playbackThread = new Thread(() -> matlabHandler.pauseSong());
        playbackThread.start();
    }

    private void stopSong() {
        playbackThread.interrupt();
        pauseButton.setVisible(false);
        playButton.setVisible(true);
        sliderTimer.stopTimer();
        playbackThread = new Thread(() -> matlabHandler.stopSong());
        playbackThread.start();
    }

    private void moveBackward() {
        playbackThread.interrupt();
        int newValue = trackSlider.getValue() - framesToSkip;
        sliderTimer.changeTime(newValue);
        playbackThread = new Thread(() -> matlabHandler.relocateSong(newValue));
        playbackThread.start();
    }

    private void moveForward() {
        playbackThread.interrupt();
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

    private void initKeyBindings() {
        Action moveBackwardAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moveBackward();
            }
        };
        Action moveForwardAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moveForward();
            }
        };
        Action volumeUpAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                volumeUp();
            }
        };
        Action volumeDownAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                volumeDown();
            }
        };
        Action pauseAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (matlabHandler.isPlaying())
                    pauseSong();
                else
                    playSong();
            }
        };

        KeyStroke leftKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
        KeyStroke rightKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
        KeyStroke upKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
        KeyStroke downKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
        KeyStroke spaceKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
        inputMap.put(leftKeyStroke, MOVE_BACKWARD);
        actionMap.put(MOVE_BACKWARD, moveBackwardAction);
        inputMap.put(rightKeyStroke, MOVE_FORWARD);
        actionMap.put(MOVE_FORWARD, moveForwardAction);
        inputMap.put(upKeyStroke, VOLUME_UP);
        actionMap.put(VOLUME_UP, volumeUpAction);
        inputMap.put(downKeyStroke, VOLUME_DOWN);
        actionMap.put(VOLUME_DOWN, volumeDownAction);
        inputMap.put(spaceKeyStroke, PAUSE);
        actionMap.put(PAUSE, pauseAction);
    }

    private void initInnerListeners() {
        sliderTimer.addObserver(this);
        volumeSlider.addChangeListener(this);
        backwardButton.addMouseListener(this);
        playButton.addMouseListener(this);
        pauseButton.addMouseListener(this);
        stopButton.addMouseListener(this);
        forwardButton.addMouseListener(this);
        soundOnButton.addMouseListener(this);
        soundOffButton.addMouseListener(this);
    }

    private void init() {
        pauseButton.setVisible(false);
        soundOffButton.setVisible(false);
        setFavorite(false);
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
        volumePanel.add(soundOnButton);
        volumePanel.add(soundOffButton);
        volumePanel.add(volumeSlider);
        mediaControlPanel.add(buttonPanel, BorderLayout.CENTER);
        mediaControlPanel.add(volumePanel, BorderLayout.EAST);
    }
}