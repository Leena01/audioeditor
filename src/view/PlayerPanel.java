package view;

import model.MatlabHandler;
import util.*;
import view.decorated.AudioSliderUI;
import view.decorated.Slider;
import view.decorated.Label;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

class PlayerPanel extends JPanel implements ActionListener, ChangeListener, Observer {
    private static final ImageIcon playIcon = new ImageIcon("resources/images/play.png");
    private static final ImageIcon playIconHover = new ImageIcon("resources/images/play2.png");
    private static final ImageIcon pauseIcon = new ImageIcon("resources/images/pause.png");
    private static final ImageIcon pauseIconHover = new ImageIcon("resources/images/pause2.png");
    private static final ImageIcon stopIcon = new ImageIcon("resources/images/stop.png");
    private static final ImageIcon stopIconHover = new ImageIcon("resources/images/stop2.png");
    // private static final ImageIcon backwardIcon = new ImageIcon("resources/images/backward.png");
    // private static final ImageIcon backwardIconHover = new ImageIcon("resources/images/backward2.png");
    private static final ImageIcon favoriteIcon = new ImageIcon("resources/images/heart.png");
    private static final ImageIcon unfavoriteIcon = new ImageIcon("resources/images/heart_red.png");
    private static final Dimension BUTTON_SIZE = new Dimension(40, 30);
    private static final long refreshMillis = 50;
    private static Dimension FIELD_DIMENSION = new Dimension(70, 10);

    private JLabel timeField;
    private JLabel totalLengthField;
    private Slider timeSlider;
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JButton favoriteButton;
    private JButton unfavoriteButton;
    private JPanel buttonPanel;
    private JSlider volumeSlider;
    private MatlabHandler matlabHandler;
    private boolean isPlaying;
    private SliderTimer sliderTimer;
    private GridBagConstraints c;

    PlayerPanel(MatlabHandler matlabHandler, ActionListener fb, ActionListener ufb) {
        super();
        GridBagLayout gridbag = new GridBagLayout();
        setLayout(gridbag);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        gridbag.setConstraints(this, c);
        this.matlabHandler = matlabHandler;
        this.isPlaying = false;

        timeField = new Label("", SwingConstants.RIGHT);
        timeField.setOpaque(false);
        timeField.setMinimumSize(FIELD_DIMENSION);
        timeField.setPreferredSize(FIELD_DIMENSION);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        add(timeField, c);

        timeSlider = new Slider(JSlider.HORIZONTAL);
        c.gridx = 1;
        add(timeSlider, c);

        totalLengthField = new Label("", SwingConstants.LEFT);
        totalLengthField.setOpaque(false);
        totalLengthField.setMinimumSize(FIELD_DIMENSION);
        totalLengthField.setPreferredSize(FIELD_DIMENSION);
        c.gridx = 2;
        add(totalLengthField, c);

        playButton = new JButton();
        playButton.setPreferredSize(BUTTON_SIZE);
        playButton.setIcon(playIcon);
        playButton.setBorderPainted(false);
        playButton.setRolloverIcon(playIconHover);
        playButton.setOpaque(false);
        playButton.setContentAreaFilled(false);

        pauseButton = new JButton();
        pauseButton.setPreferredSize(BUTTON_SIZE);
        pauseButton.setIcon(pauseIcon);
        pauseButton.setBorderPainted(false);
        pauseButton.setRolloverIcon(pauseIconHover);
        pauseButton.setOpaque(false);
        pauseButton.setContentAreaFilled(false);

        stopButton = new JButton();
        stopButton.setPreferredSize(BUTTON_SIZE);
        stopButton.setIcon(stopIcon);
        stopButton.setBorderPainted(false);
        stopButton.setRolloverIcon(stopIconHover);
        stopButton.setOpaque(false);
        stopButton.setContentAreaFilled(false);

        favoriteButton = new JButton();
        favoriteButton.setPreferredSize(BUTTON_SIZE);
        favoriteButton.setIcon(favoriteIcon);
        favoriteButton.setBorderPainted(false);
        favoriteButton.setOpaque(false);
        favoriteButton.setContentAreaFilled(false);

        unfavoriteButton = new JButton();
        unfavoriteButton.setPreferredSize(BUTTON_SIZE);
        unfavoriteButton.setIcon(unfavoriteIcon);
        unfavoriteButton.setBorderPainted(false);
        unfavoriteButton.setOpaque(false);
        unfavoriteButton.setContentAreaFilled(false);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        volumeSlider = new JSlider(JSlider.HORIZONTAL);
        // volumeSlider.setForeground(Color.BLUE);
        volumeSlider.setPreferredSize(new Dimension(100, 30));
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(false);
        volumeSlider.setPaintLabels(false);
        volumeSlider.setPaintTrack(true);
        volumeSlider.setOpaque(false);
        volumeSlider.setValue(50);

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

        sliderTimer = new SliderTimer(timeSlider, timeField, totalLengthField, true);

        timeSlider.addChangeListener(this);
        volumeSlider.addChangeListener(this);
        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
        stopButton.addActionListener(this);
        favoriteButton.addActionListener(fb);
        unfavoriteButton.addActionListener(ufb);
        sliderTimer.addObserver(this);
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
        if (source == timeSlider) {
            Slider s = (Slider) source;
            // if (!s.getValueIsAdjusting()) {
                // sliderTimer.pauseTimer();
                // timeSlider.removeListeners();
                int frame = s.getValue();
                sliderTimer.changeTime(frame);
                new Thread(() -> {
                    matlabHandler.relocateSong(frame, isPlaying);

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
        Image background = new ImageIcon("resources/images/background.png").getImage();
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }

    void setCurrentSong(double totalSamples, double freq, BufferedImage plot) {
        stopSong();
        sliderTimer.schedule(refreshMillis, totalSamples, freq);

        timeSlider.setImage(plot);
        timeSlider.setUI(new AudioSliderUI(timeSlider));
        // BufferedImage scaledPlot = Utils.resize(plot, 375, 60);
        // timeSlider.setImage(scaledPlot);

        /*
        Graphics2D g2d = scaledPlot.createGraphics();
        g2d.setColor(Color.BLUE);
        BasicStroke bs = new BasicStroke(2);
        g2d.setStroke(bs);
        g2d.drawLine(200, 0, 200,  scaledPlot.getHeight());
        g2d.dispose();

        JLabel img = new Label(new ImageIcon(scaledPlot));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        add(img, c);
        img.setHorizontalAlignment(JLabel.CENTER);
        img.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        */
    }

    private void playSong() {
        new Thread(() -> {
            matlabHandler.resumeSong();
            isPlaying = true;
            SwingUtilities.invokeLater(() -> {
                sliderTimer.resumeTimer();
                playButton.setVisible(false);
                pauseButton.setVisible(true);
            });
        }).start();
    }

    private void pauseSong() {
        new Thread(() -> {
            matlabHandler.pauseSong();
            isPlaying = false;
            SwingUtilities.invokeLater(() -> {
                sliderTimer.pauseTimer();
                pauseButton.setVisible(false);
                playButton.setVisible(true);
            });
        }).start();
    }

    private void stopSong() {
        new Thread(() -> {
            matlabHandler.stopSong();
            isPlaying = false;
            SwingUtilities.invokeLater(() -> {
                sliderTimer.stopTimer();
                pauseButton.setVisible(false);
                playButton.setVisible(true);
            });
        }).start();
    }
}
