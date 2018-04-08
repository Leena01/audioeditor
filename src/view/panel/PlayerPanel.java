package view.panel;

import static common.util.Helper.resizeImageIcon;
import static view.param.Constants.*;
import logic.matlab.MatlabHandler;
import common.properties.ImageLoader;
import common.properties.SongPropertiesLoader;
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
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

class PlayerPanel extends BasicPanel implements ActionListener, ChangeListener, Observer {
    private static final long REFRESH_MILLIS = 50;
    private static final int SKIP_FRAME = 500; // TODO
    private static Dimension FIELD_DIMENSION = new Dimension(70, 10);
    private static final Dimension BUTTON_SIZE = new Dimension(36, 27);
    private static final Dimension BUTTON_SIZE_FAV = new Dimension(36, 36);
    private static final ImageIcon PLAY_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getPlayIconURL()), BUTTON_SIZE);
    private static final ImageIcon PLAY_ICON_HOVER =
            resizeImageIcon(new ImageIcon(ImageLoader.getPlayHoverIconURL()), BUTTON_SIZE);
    private static final ImageIcon PAUSE_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getPauseIconURL()), BUTTON_SIZE);
    private static final ImageIcon PAUSE_ICON_HOVER =
            resizeImageIcon(new ImageIcon(ImageLoader.getPauseHoverIconURL()), BUTTON_SIZE);
    private static final ImageIcon STOP_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getStopIconURL()), BUTTON_SIZE);
    private static final ImageIcon STOP_ICON_HOVER =
            resizeImageIcon(new ImageIcon(ImageLoader.getStopHoverIconURL()), BUTTON_SIZE);
    private static final ImageIcon BACKWARD_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getBackwardIconURL()), BUTTON_SIZE);
    private static final ImageIcon BACKWARD_ICON_HOVER =
            resizeImageIcon(new ImageIcon(ImageLoader.getBackwardHoverIconURL()), BUTTON_SIZE);
    private static final ImageIcon FAVORITE_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getFavoriteIconURL()), BUTTON_SIZE_FAV);
    private static final ImageIcon UNFAVORITE_ICON =
            resizeImageIcon(new ImageIcon(ImageLoader.getUnfavoriteIconURL()), BUTTON_SIZE_FAV);

    private JLabel timeField;
    private JLabel totalLengthField;
    private TrackSlider trackSlider;
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JButton backwardButton;
    private JButton favoriteButton;
    private JButton unfavoriteButton;
    private JPanel buttonPanel;
    private Component glassPane;
    private VolumeSlider volumeSlider;
    private MatlabHandler matlabHandler;
    private boolean isPlaying;
    private SliderTimer sliderTimer;
    private GridBagConstraints c;
    private GridBagLayout gridBag;

    PlayerPanel(MatlabHandler matlabHandler, Component glassPane, ActionListener fb, ActionListener ufb) {
        super();
        this.glassPane = glassPane;
        gridBag = new GridBagLayout();
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        gridBag.setConstraints(this, c);
        setLayout(gridBag);
        this.matlabHandler = matlabHandler;
        this.isPlaying = false;

        timeField = new TimeLabel(FIELD_DIMENSION);
        trackSlider = new TrackSlider(JSlider.HORIZONTAL);
        totalLengthField = new TimeLabel(FIELD_DIMENSION);

        playButton = new TransparentButton(PLAY_ICON, PLAY_ICON_HOVER, BUTTON_SIZE);
        pauseButton = new TransparentButton(PAUSE_ICON, PAUSE_ICON_HOVER, BUTTON_SIZE);
        stopButton = new TransparentButton(STOP_ICON, STOP_ICON_HOVER, BUTTON_SIZE);
        backwardButton = new TransparentButton(BACKWARD_ICON, BACKWARD_ICON_HOVER, BUTTON_SIZE);
        // forwardButton = new TransparentButton(FORWARD, STOP_ICON_HOVER, BUTTON_SIZE);
        favoriteButton = new TransparentButton(FAVORITE_ICON, BUTTON_SIZE, fb);
        unfavoriteButton = new TransparentButton(UNFAVORITE_ICON, BUTTON_SIZE, ufb);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        volumeSlider = new VolumeSlider(VOLUME_SLIDER_SIZE, SongPropertiesLoader.getVolumeMin(), SongPropertiesLoader.getVolumeMax());
        sliderTimer = new SliderTimer(trackSlider, timeField, totalLengthField, true);

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
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == trackSlider) {
            // if (!s.getValueIsAdjusting()) {
            // sliderTimer.pauseTimer();
            // trackSlider.removeListeners();
            int frame = trackSlider.getValue();
            new Thread(() -> {
                glassPane.setVisible(true);
                sliderTimer.changeTime(frame);
                matlabHandler.relocateSong(frame, isPlaying);
                SwingUtilities.invokeLater(() -> {
                    glassPane.setVisible(false);
                });
            }).start();
            // }
            /* else {
                new Thread(() -> {
                    matlabHandler.pauseSong();
                    sliderTimer.pauseTimer();
                    isPlaying = false;
                }).start();
            } */
        } else if (source == volumeSlider) {
            JSlider vs = (JSlider) source;
            float level = vs.getValue() / 50.0f;
            if (!vs.getValueIsAdjusting()) {
                new Thread(() -> {
                    glassPane.setVisible(true);
                    if (isPlaying)
                        sliderTimer.pauseTimer();
                    matlabHandler.changeVolume(level, isPlaying);
                    SwingUtilities.invokeLater(() -> {
                        if (isPlaying)
                            sliderTimer.resumeTimer();
                        glassPane.setVisible(false);
                    });
                }).start();
                new Thread(() -> matlabHandler.changeVolume(level, isPlaying)).start();
            }
        }
    }

    @Override
    public void update(Observable obs, Object obj) {
        isPlaying = !isPlaying;
        pauseButton.setVisible(false);
        playButton.setVisible(true);
    }

    void setCurrentSong(double totalSamples, double freq, BufferedImage plot) {
        stopSong();
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
        new Thread(() -> {
            glassPane.setVisible(true);
            sliderTimer.pauseTimer();
            matlabHandler.resumeSong();
            isPlaying = true;
            SwingUtilities.invokeLater(() -> {
                sliderTimer.resumeTimer();
                playButton.setVisible(false);
                pauseButton.setVisible(true);
                glassPane.setVisible(false);
            });
        }).start();
    }

    void pauseSong() {
        new Thread(() -> {
            glassPane.setVisible(true);
            matlabHandler.pauseSong();
            isPlaying = false;
            SwingUtilities.invokeLater(() -> {
                sliderTimer.pauseTimer();
                pauseButton.setVisible(false);
                playButton.setVisible(true);
                glassPane.setVisible(false);
            });
        }).start();
    }

    private void stopSong() {
        new Thread(() -> {
            glassPane.setVisible(true);
            matlabHandler.stopSong();
            isPlaying = false;
            SwingUtilities.invokeLater(() -> {
                sliderTimer.stopTimer();
                pauseButton.setVisible(false);
                playButton.setVisible(true);
                glassPane.setVisible(false);
            });
        }).start();
    }

    private void moveBackward() {
        // TODO
        new Thread(() -> {
            int frame = trackSlider.getValue();
            glassPane.setVisible(true);
            sliderTimer.changeTime(frame - SKIP_FRAME);
            matlabHandler.relocateSong(frame, isPlaying);
            SwingUtilities.invokeLater(() -> glassPane.setVisible(false));
        }).start();
    }

    private void moveForward() {
        // TODO
        new Thread(() -> {
            int frame = trackSlider.getValue();
            glassPane.setVisible(true);
            sliderTimer.changeTime(frame + SKIP_FRAME);
            matlabHandler.relocateSong(frame, isPlaying);
            SwingUtilities.invokeLater(() -> glassPane.setVisible(false));
        }).start();
    }

    private void initInnerListeners() {
        sliderTimer.addObserver(this);
        trackSlider.addChangeListener(this);
        volumeSlider.addChangeListener(this);
        backwardButton.addChangeListener(this);
        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
        stopButton.addActionListener(this);
    }

    private void init() {
        pauseButton.setVisible(false);
        setFavorite(false);
    }

    @Override
    protected void setStyle() {
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
        buttonPanel.add(favoriteButton);
        buttonPanel.add(unfavoriteButton);
        buttonPanel.add(volumeSlider);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 2;
        add(buttonPanel, c);
    }
}
