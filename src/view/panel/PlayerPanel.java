package view.panel;

import logic.matlab.MatlabHandler;
import view.element.playerpanel.*;
import view.element.core.label.Label;
import view.element.core.button.Button;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import static util.Utils.resizeImageIcon;

class PlayerPanel extends JPanel implements ActionListener, ChangeListener, Observer {
    private static final long REFRESH_MILLIS = 50;
    private static Dimension FIELD_DIMENSION = new Dimension(70, 10);
    private static final Dimension BUTTON_SIZE = new Dimension(40, 30);
    private static final ImageIcon PLAY_ICON = resizeImageIcon(new ImageIcon("resources/images/play.png"), BUTTON_SIZE);
    private static final ImageIcon PLAY_ICON_HOVER = resizeImageIcon(new ImageIcon("resources/images/play2.png"), BUTTON_SIZE);
    private static final ImageIcon PAUSE_ICON = resizeImageIcon(new ImageIcon("resources/images/pause.png"), BUTTON_SIZE);
    private static final ImageIcon PAUSE_ICON_HOVER = resizeImageIcon(new ImageIcon("resources/images/pause2.png"), BUTTON_SIZE);
    private static final ImageIcon STOP_ICON = resizeImageIcon(new ImageIcon("resources/images/stop.png"), BUTTON_SIZE);
    private static final ImageIcon STOP_ICON_HOVER = resizeImageIcon(new ImageIcon("resources/images/stop2.png"), BUTTON_SIZE);
    // private static final ImageIcon BACKWARD_ICON = resizeImageIcon(new ImageIcon("resources/images/backward.png"), BUTTON_SIZE);
    // private static final ImageIcon BACKWARD_ICON_HOVER = resizeImageIcon(new ImageIcon("resources/images/backward2.png"), BUTTON_SIZE);
    private static final ImageIcon FAVORITE_ICON = resizeImageIcon(new ImageIcon("resources/images/heart.png"), BUTTON_SIZE);
    private static final ImageIcon UNFAVORITE_ICON = resizeImageIcon(new ImageIcon("resources/images/heart_red.png"), BUTTON_SIZE);
    private static final ImageIcon BACKGROUND = new ImageIcon("resources/images/background.png");

    private JLabel timeField;
    private JLabel totalLengthField;
    private TrackSlider trackSlider;
    private Button playButton;
    private Button pauseButton;
    private Button stopButton;
    private Button favoriteButton;
    private Button unfavoriteButton;
    private JPanel buttonPanel;
    private Component glassPane;
    private VolumeSlider volumeSlider;
    private MatlabHandler matlabHandler;
    private boolean isPlaying;
    private SliderTimer sliderTimer;
    private GridBagConstraints c;

    PlayerPanel(MatlabHandler matlabHandler, Component glassPane, ActionListener fb, ActionListener ufb) {
        super();
        this.glassPane = glassPane;
        GridBagLayout gridbag = new GridBagLayout();
        setLayout(gridbag);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        gridbag.setConstraints(this, c);
        this.matlabHandler = matlabHandler;
        this.isPlaying = false;

        timeField = new TimeLabel(FIELD_DIMENSION);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        add(timeField, c);

        trackSlider = new TrackSlider(JSlider.HORIZONTAL);
        c.gridx = 1;
        add(trackSlider, c);

        totalLengthField = new TimeLabel(FIELD_DIMENSION);
        c.gridx = 2;
        add(totalLengthField, c);

        playButton = new Button(PLAY_ICON, PLAY_ICON_HOVER, BUTTON_SIZE);
        playButton.addMouseListener();

        pauseButton = new Button(PAUSE_ICON, PAUSE_ICON_HOVER, BUTTON_SIZE);
        pauseButton.addMouseListener();

        stopButton = new Button(STOP_ICON, STOP_ICON_HOVER, BUTTON_SIZE);
        stopButton.addMouseListener();

        favoriteButton = new Button(FAVORITE_ICON, BUTTON_SIZE);
        favoriteButton.addMouseListener();

        unfavoriteButton = new Button(UNFAVORITE_ICON, BUTTON_SIZE);
        unfavoriteButton.addMouseListener();

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        volumeSlider = new VolumeSlider(new Dimension(100, 30));
        volumeSlider.setForeground(Color.BLUE);

        pauseButton.setVisible(false);
        favoriteButton.setVisible(false);
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

        sliderTimer = new SliderTimer(trackSlider, timeField, totalLengthField, true);

        initInnerListeners();
        favoriteButton.addActionListener(fb);
        unfavoriteButton.addActionListener(ufb);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == playButton) {
            playSong();
        }
        else if (source == pauseButton) {
            pauseSong();
        }
        else if (source == stopButton) {
            stopSong();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == trackSlider) {
            TrackSlider s = (TrackSlider) source;
            // if (!s.getValueIsAdjusting()) {
                // sliderTimer.pauseTimer();
                // trackSlider.removeListeners();
                int frame = s.getValue();
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
        }
        else if (source == volumeSlider) {
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
    public void update(Observable obs, Object obj)
    {
        isPlaying = !isPlaying;
        pauseButton.setVisible(false);
        playButton.setVisible(true);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image background = BACKGROUND.getImage();
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }

    void setCurrentSong(double totalSamples, double freq, BufferedImage plot) {
        stopSong();
        sliderTimer.schedule(REFRESH_MILLIS, totalSamples, freq);

        trackSlider.setImage(plot);
        trackSlider.setUI(new AudioSliderUI(trackSlider));
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

    private void pauseSong() {
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

    private void initInnerListeners() {
        sliderTimer.addObserver(this);
        trackSlider.addChangeListener(this);
        volumeSlider.addChangeListener(this);
        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
        stopButton.addActionListener(this);
    }
}
