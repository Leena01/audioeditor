package org.ql.audioeditor.view.panel;

import static org.ql.audioeditor.common.util.Helper.resizeImageIcon;
import static org.ql.audioeditor.common.util.Helper.secondsToFrames;
import static org.ql.audioeditor.view.param.Constants.*;

import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.core.button.TransparentButton;
import org.ql.audioeditor.view.core.panel.BasicPanel;
import org.ql.audioeditor.view.core.slider.SliderTimer;
import org.ql.audioeditor.view.core.slider.TimeLabel;
import org.ql.audioeditor.view.core.slider.TrackSlider;
import org.ql.audioeditor.view.core.slider.VolumeSlider;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

class PlayerPanel extends BasicPanel implements MouseListener, ChangeListener, Observer {
    private static final int NULL_VOLUME = 0;
    private static final int REFRESH_MILLIS = SongPropertiesLoader.getSongRefreshMillis();
    private static final float CONVERSION_RATE = SongPropertiesLoader.getVolumeConversionRate();
    private static final int SECONDS_TO_SKIP = SongPropertiesLoader.getSecondsToSkip();
    private static Dimension FIELD_DIMENSION = new Dimension(80, 10);
    private static final Dimension BUTTON_SIZE = new Dimension(36, 36);
    private static final Dimension SOUND_BUTTON_SIZE = new Dimension(20, 20);
    private static final ImageIcon PLAY_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getPlayIconURL()), BUTTON_SIZE);
    private static final ImageIcon PAUSE_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getPauseIconURL()), BUTTON_SIZE);
    private static final ImageIcon STOP_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getStopIconURL()), BUTTON_SIZE);
    private static final ImageIcon BACKWARD_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getBackwardIconURL()), BUTTON_SIZE);
    private static final ImageIcon FORWARD_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getForwardIconURL()), BUTTON_SIZE);
    private static final ImageIcon FAVORITE_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getFavoriteIconURL()), BUTTON_SIZE);
    private static final ImageIcon UNFAVORITE_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getUnfavoriteIconURL()), BUTTON_SIZE);
    private static final ImageIcon SOUND_ON_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getSoundOnIconURL()), SOUND_BUTTON_SIZE);
    private static final ImageIcon SOUND_OFF_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getSoundOffIconURL()), SOUND_BUTTON_SIZE);

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
    private int framesToSkip;
    private int recentVolumeLevel;

    PlayerPanel(MatlabHandler matlabHandler, HorizontalBar mediaControlPanel, ActionListener fb, ActionListener ufb) {
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
        recentVolumeLevel = SongPropertiesLoader.getVolumeInit();

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
        unfavoriteButton = new TransparentButton(UNFAVORITE_ICON, BUTTON_SIZE, ufb);
        soundOnButton = new TransparentButton(SOUND_ON_ICON, BUTTON_SIZE);
        soundOffButton = new TransparentButton(SOUND_OFF_ICON, BUTTON_SIZE);

        volumePanel = new JPanel(new FlowLayout());
        volumeSlider = new VolumeSlider(VOLUME_SLIDER_SIZE, SongPropertiesLoader.getVolumeMin(),
                SongPropertiesLoader.getVolumeMax(), recentVolumeLevel);
        sliderTimer = new SliderTimer(trackSlider, matlabHandler, timeField, totalLengthField, true);

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
            } else if (source == pauseButton) {
                pauseSong();
            } else if (source == stopButton) {
                stopSong();
            } else if (source == soundOnButton) {
                turnOff();
            } else if (source == soundOffButton) {
                turnOn();
            }
        }
        if (source == backwardButton) {
            moveBackward();
        } else if (source == forwardButton) {
            moveForward();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == volumeSlider) {
            JSlider vs = (JSlider) source;
            float level = vs.getValue() / CONVERSION_RATE;
            if (!vs.getValueIsAdjusting()) {
                playbackThread.interrupt();
                playbackThread = new Thread(() -> matlabHandler.changeVolume(level));
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

    private void turnOff() {
        playbackThread.interrupt();
        recentVolumeLevel = volumeSlider.getValue();
        soundOnButton.setVisible(false);
        soundOffButton.setVisible(true);
        volumeSlider.setValue(NULL_VOLUME);
    }

    private void turnOn() {
        playbackThread.interrupt();
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
        Action pauseAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                pauseSong();
            }
        };
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
                        "moveBackwardAction");
        getActionMap().put("moveBackwardAction", moveBackwardAction);
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
                        "moveForwardAction");
        getActionMap().put("moveForwardAction", moveForwardAction);
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),
                        "pauseAction");
        getActionMap().put("pauseAction", pauseAction);
    }

    private void initInnerListeners() {
        initKeyBindings();
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
        mediaControlPanel.setBorder(BorderFactory.createEmptyBorder(40, 10, 10, 10));
        trackSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        timeField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        totalLengthField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
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
