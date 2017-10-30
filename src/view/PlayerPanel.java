package view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import util.MatlabHandler;
import util.Slider;
import util.SliderTimer;

class PlayerPanel extends JPanel implements ActionListener, ChangeListener, Observer {
    private JLabel timeField;
    private JLabel totalLengthField;
    private JSlider timeSlider;
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
    private static final int BUTTON_HEIGHT = 30;
    private static final int BUTTON_WIDTH = 40;
    private static final long refreshMillis = 100;
    private static Dimension FIELD_DIMENSION = new Dimension(60, 10);

    PlayerPanel(MatlabHandler matlabHandler, ActionListener fb, ActionListener ufb) {
        super();
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        this.matlabHandler = matlabHandler;
        this.isPlaying = false;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;

        timeField = new JLabel();
        timeField.setOpaque(false);
        timeField.setForeground(Color.WHITE);
        timeField.setPreferredSize(FIELD_DIMENSION);
        c.gridy = 0;
        c.gridwidth = 1;
        add(timeField, c);

        timeSlider = new Slider(JSlider.HORIZONTAL);
        timeSlider.setPreferredSize(new Dimension(400, 20));
        timeSlider.setPaintTicks(false);
        timeSlider.setPaintLabels(false);
        timeSlider.setPaintLabels(false);
        timeSlider.setPaintTrack(true);
        timeSlider.setOpaque(false);
        timeSlider.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        c.gridx = 1;
        add(timeSlider, c);

        totalLengthField = new JLabel();
        totalLengthField.setOpaque(false);
        totalLengthField.setForeground(Color.WHITE);
        totalLengthField.setPreferredSize(FIELD_DIMENSION);
        c.gridx = 2;
        add(totalLengthField, c);

        playButton = new JButton();
        playButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        playButton.setIcon(playIcon);
        playButton.setBorderPainted(false);
        playButton.setRolloverIcon(playIconHover);
        playButton.setOpaque(false);
        playButton.setContentAreaFilled(false);

        pauseButton = new JButton();
        pauseButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        pauseButton.setIcon(pauseIcon);
        pauseButton.setBorderPainted(false);
        pauseButton.setRolloverIcon(pauseIconHover);
        pauseButton.setOpaque(false);
        pauseButton.setContentAreaFilled(false);

        stopButton = new JButton();
        stopButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        stopButton.setIcon(stopIcon);
        stopButton.setBorderPainted(false);
        stopButton.setRolloverIcon(stopIconHover);
        stopButton.setOpaque(false);
        stopButton.setContentAreaFilled(false);

        favoriteButton = new JButton();
        favoriteButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        favoriteButton.setIcon(favoriteIcon);
        favoriteButton.setBorderPainted(false);
        favoriteButton.setOpaque(false);
        favoriteButton.setContentAreaFilled(false);

        unfavoriteButton = new JButton();
        unfavoriteButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
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
        c.gridy = 1;
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
        else if (source == pauseButton) {
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
        else if (source == stopButton) {
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

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == timeSlider) {
            Slider s = (Slider) source;
            // if (!s.getValueIsAdjusting()) {
                int frame = s.getValue();
                sliderTimer.changeTime(frame);
                new Thread(() -> matlabHandler.relocateSong(frame, isPlaying)).start();
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

    void setCurrentSong(double totalSamples, double freq) {
        sliderTimer.stopTimer();
        sliderTimer.schedule(refreshMillis, totalSamples, freq);
    }
}
