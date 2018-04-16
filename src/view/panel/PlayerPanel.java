package view.panel;

import static common.util.Helper.resizeImageIcon;
import static common.util.Helper.secondsToFrames;
import static view.param.Constants.*;

import logic.matlab.MatlabHandler;
import common.properties.ImageLoader;
import common.properties.SongPropertiesLoader;
import view.core.bar.HorizontalBar;
import view.core.button.TransparentButton;
import view.core.panel.BasicPanel;
import view.core.slider.SliderTimer;
import view.core.slider.TimeLabel;
import view.core.slider.TrackSlider;
import view.core.slider.VolumeSlider;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

class PlayerPanel extends BasicPanel implements ActionListener, ChangeListener, MouseListener, Observer {
    private static final int REFRESH_MILLIS = SongPropertiesLoader.getSongRefreshMillis();
    private static final float CONVERSION_RATE = SongPropertiesLoader.getVolumeConversionRate();
    private static final int SECONDS_TO_SKIP = SongPropertiesLoader.getSecondsToSkip();
    private static Dimension FIELD_DIMENSION = new Dimension(80, 10);
    private static final Dimension BUTTON_SIZE = new Dimension(36, 36);
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
    private HorizontalBar mediaControlPanel;
    private VolumeSlider volumeSlider;
    private MatlabHandler matlabHandler;
    private SliderTimer sliderTimer;
    private GridBagConstraints c;
    private GridBagLayout gridBag;
    private Thread playbackThread;
    private int framesToSkip;

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

        timeField = new TimeLabel(FIELD_DIMENSION, SwingConstants.RIGHT);
        trackSlider = new TrackSlider(JSlider.HORIZONTAL);
        totalLengthField = new TimeLabel(FIELD_DIMENSION, SwingConstants.LEFT);

        playButton = new TransparentButton(PLAY_ICON, BUTTON_SIZE);
        pauseButton = new TransparentButton(PAUSE_ICON, BUTTON_SIZE);
        stopButton = new TransparentButton(STOP_ICON, BUTTON_SIZE);
        backwardButton = new TransparentButton(BACKWARD_ICON, BUTTON_SIZE);
        forwardButton = new TransparentButton(FORWARD_ICON, BUTTON_SIZE);
        favoriteButton = new TransparentButton(FAVORITE_ICON, BUTTON_SIZE, fb);
        unfavoriteButton = new TransparentButton(UNFAVORITE_ICON, BUTTON_SIZE, ufb);

        volumeSlider = new VolumeSlider(VOLUME_SLIDER_SIZE, SongPropertiesLoader.getVolumeMin(),
                SongPropertiesLoader.getVolumeMax());
        sliderTimer = new SliderTimer(trackSlider, matlabHandler, timeField, totalLengthField, true);

        setStyle();
        addPanels();
        initInnerListeners();
        init();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == playButton) {
            playSong();
        } else if (source == pauseButton) {
            pauseSong();
        } else if (source == stopButton) {
            stopSong();
        } else if (source == backwardButton) {
            moveBackward();
        } else if (source == forwardButton) {
            moveForward();
        }
    }

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
                new Thread(() -> matlabHandler.changeVolume(level)).start();
            }
        }
    }


    // TODO
    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            System.out.println("double clicked");
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
    public void update(Observable obs, Object obj) {
        pauseButton.setVisible(false);
        playButton.setVisible(true);
    }

    void setCurrentSong(double totalSamples, double freq, BufferedImage plot) {
        stopSong();
        mediaControlPanel.setVisible(true);
        sliderTimer.schedule(REFRESH_MILLIS, totalSamples, freq);
        trackSlider.setImage(plot);
        framesToSkip = secondsToFrames(SECONDS_TO_SKIP, freq);
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
        // if (newValue < )
        playbackThread = new Thread(() -> {
            sliderTimer.changeTime(newValue);
            matlabHandler.relocateSong(newValue);
        });
        playbackThread.start();
    }

    private void moveForward() {
        // TODO
        playbackThread.interrupt();
        int newValue = trackSlider.getValue() + framesToSkip;
        playbackThread = new Thread(() -> {
            sliderTimer.changeTime(newValue);
            matlabHandler.relocateSong(newValue);
        });
        playbackThread.start();
    }

    private void initInnerListeners() {
        sliderTimer.addObserver(this);
        volumeSlider.addChangeListener(this);
        backwardButton.addActionListener(this);
        playButton.addActionListener(this);
        this.addMouseListener(this);
        pauseButton.addActionListener(this);
        stopButton.addActionListener(this);
        forwardButton.addActionListener(this);
    }

    private void init() {
        pauseButton.setVisible(false);
        setFavorite(false);
    }

    @Override
    protected void setStyle() {
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

        mediaControlPanel.add(backwardButton);
        mediaControlPanel.add(playButton);
        mediaControlPanel.add(pauseButton);
        mediaControlPanel.add(stopButton);
        mediaControlPanel.add(forwardButton);
        mediaControlPanel.add(favoriteButton);
        mediaControlPanel.add(unfavoriteButton);
        mediaControlPanel.add(volumeSlider);
    }
}
