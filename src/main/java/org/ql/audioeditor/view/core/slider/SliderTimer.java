package org.ql.audioeditor.view.core.slider;

import org.ql.audioeditor.logic.matlab.MatlabHandler;

import javax.swing.JLabel;
import javax.swing.JSlider;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import static org.ql.audioeditor.common.util.TimeUtils.formatDuration;
import static org.ql.audioeditor.common.util.TimeUtils.framesToSeconds;

/**
 * Timer with slider.
 */
public class SliderTimer extends Observable {
    private static final int MIN_FRAMES = MatlabHandler.getMinFrames();
    private static int maxFrames;
    private final JSlider slider;
    private final MatlabHandler matlabHandler;
    private final JLabel timeField;
    private final JLabel totalLengthField;
    private final boolean isDaemon;
    private Timer timer;
    private int refreshMillis;
    private double freq;

    /**
     * Constructor.
     *
     * @param slider           Slider
     * @param matlabHandler    Matlab handler
     * @param timeField        Text field to show time elapsed
     * @param totalLengthField Text field to show the total length of the song
     * @param isDaemon         Is daemon
     */
    public SliderTimer(JSlider slider, MatlabHandler matlabHandler,
        JLabel timeField, JLabel totalLengthField, boolean isDaemon) {
        this.timer = null;
        this.slider = slider;
        this.matlabHandler = matlabHandler;
        this.timeField = timeField;
        this.totalLengthField = totalLengthField;
        this.isDaemon = isDaemon;
        this.refreshMillis = 0;
        this.freq = 0.0;
        maxFrames = 0;
        slider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                double percent = p.x / ((double) slider.getWidth());
                int range = slider.getMaximum() - slider.getMinimum();
                double newVal = range * percent;
                int frame = (int) (slider.getMinimum() + newVal);
                changeTime(frame);
                new Thread(() -> matlabHandler.relocateSong(frame)).start();
            }
        });
    }

    /**
     * Schedules the timer.
     *
     * @param refreshMillis Refresh interval (in milliseconds)
     * @param totalSamples  Total number of samples
     * @param freq          Sampling rate
     */
    public void schedule(int refreshMillis, double totalSamples, double freq) {
        this.refreshMillis = refreshMillis;
        maxFrames = (int) totalSamples;
        this.freq = freq;
        this.totalLengthField.
            setText(formatDuration(framesToSeconds(maxFrames, freq)));
        this.slider.setMinimum(MIN_FRAMES);
        this.slider.setMaximum(maxFrames);
        timeField.setText(formatDuration(framesToSeconds(MIN_FRAMES, freq)));
    }

    /**
     * Resumes the timer.
     */
    public void resumeTimer() {
        if (refreshMillis != 0) {
            timer = new Timer(isDaemon);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (isPlaying()) {
                        autoMove();
                    } else {
                        setChanged();
                        notifyObservers();
                        stopTimer();
                    }
                }
            }, refreshMillis, refreshMillis);
        }
    }

    /**
     * Pauses the timer.
     */
    public void pauseTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * Stops the timer.
     */
    public void stopTimer() {
        pauseTimer();
        if (slider != null) {
            setCurrentTime(MIN_FRAMES);
        }
    }

    /**
     * Changes the current time.
     *
     * @param frame Position to set the slider to
     */
    public void changeTime(int frame) {
        if (slider != null) {
            if (frame < MIN_FRAMES) {
                setCurrentTime(MIN_FRAMES);
            } else if (frame > maxFrames) {
                setCurrentTime(maxFrames);
            } else {
                setCurrentTime(frame);
            }
        }
    }

    /**
     * Moves the slider automatically.
     */
    private void autoMove() {
        if (slider != null && !slider.getValueIsAdjusting()) {
            setCurrentTime((int) matlabHandler.getCurrentFrame());
        }
    }

    /**
     * Sets the slider and timeField to the value given.
     *
     * @param currentFrame Current frane
     */
    private void setCurrentTime(int currentFrame) {
        timeField.setText(formatDuration(framesToSeconds(currentFrame, freq)));
        slider.setValue(currentFrame);
    }

    /**
     * Returns whether the song is playing.
     *
     * @return
     */
    private boolean isPlaying() {
        return matlabHandler.isPlaying();
    }
}
