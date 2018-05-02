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

import static org.ql.audioeditor.common.util.Helper.MILLIS_SECONDS_CONVERSION;
import static org.ql.audioeditor.common.util.Helper.formatDuration;
import static org.ql.audioeditor.common.util.Helper.framesToMillis;

/**
 * Timer with slider.
 */
public class SliderTimer extends Observable {
    private static final int MIN_FRAMES = 1;
    private static int maxFrames;
    private final JSlider slider;
    private final MatlabHandler matlabHandler;
    private final JLabel timeField;
    private final JLabel totalLengthField;
    private final boolean isDaemon;
    private Timer timer;
    private int refreshMillis;
    private double freq;
    private int totalLength;

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
        this.totalLength = 0;
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

    public void schedule(int refreshMillis, double totalSamples, double freq) {
        this.refreshMillis = refreshMillis;
        maxFrames = (int) totalSamples;
        this.freq = freq;
        this.totalLength =
            (int) ((maxFrames / freq) * MILLIS_SECONDS_CONVERSION);
        this.totalLengthField.setText(formatDuration(totalLength));
        this.slider.setMinimum(MIN_FRAMES);
        this.slider.setMaximum(maxFrames);
        timeField.setText(formatDuration(framesToMillis(MIN_FRAMES, freq)));
    }

    public void resumeTimer() {
        if (refreshMillis != 0) {
            timer = new Timer(isDaemon);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (isPlaying()) {
                        autoMove();
                    }
                    else {
                        setChanged();
                        notifyObservers();
                        stopTimer();
                    }
                }
            }, refreshMillis, refreshMillis);
        }
    }

    public void pauseTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (slider != null) {
            setCurrentTime(MIN_FRAMES);
        }
    }

    private void autoMove() {
        if (slider != null && !slider.getValueIsAdjusting()) {
            setCurrentTime((int) matlabHandler.getCurrentFrame());
        }
    }

    public void changeTime(int frame) {
        if (slider != null) {
            if (frame < MIN_FRAMES) {
                setCurrentTime(MIN_FRAMES);
            }
            else if (frame > maxFrames) {
                setCurrentTime(maxFrames);
            }
            else {
                setCurrentTime(frame);
            }
        }
    }

    public int getMin() {
        return MIN_FRAMES;
    }

    private void setCurrentTime(int currentFrame) {
        timeField.setText(formatDuration(framesToMillis(currentFrame, freq)));
        slider.setValue(currentFrame);
    }

    private boolean isPlaying() {
        return matlabHandler.isPlaying();
    }
}
