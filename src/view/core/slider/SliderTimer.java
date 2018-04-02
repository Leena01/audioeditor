package view.core.slider;

import static view.util.Helper.formatDuration;
import static view.util.Helper.framesToMillis;

import javax.swing.*;
import java.util.*;
import java.util.Timer;

public class SliderTimer extends Observable {
    private static final int MIN = 1;
    private static int MAX;

    private Timer timer;
    private JSlider slider;
    private JLabel timeField;
    private JLabel totalLengthField;
    private boolean isDaemon;
    private long refreshMillis;
    private double freq;
    private long timeElapsed;
    private long totalLength;

    public SliderTimer(JSlider slider, JLabel timeField, JLabel totalLengthField, boolean isDaemon) {
        this.timer = null;
        this.slider = slider;
        this.timeField = timeField;
        this.totalLengthField = totalLengthField;
        this.isDaemon = isDaemon;
        this.refreshMillis = 0;
        this.freq = 0.0;
        this.timeElapsed = 0;
        this.totalLength = 0;
        MAX = 0;
    }

    public void schedule(long refreshMillis, double totalSamples, double freq) {
        this.refreshMillis = refreshMillis;
        MAX = (int)totalSamples;
        this.freq = freq;
        this.totalLength = (long)((MAX / freq) * 1000);
        this.totalLengthField.setText(formatDuration(totalLength));
        this.slider.setMinimum(MIN);
        this.slider.setMaximum(MAX);
    }

    public void resumeTimer() {
        if (refreshMillis != 0) {
            System.out.println(refreshMillis);
            timer = new Timer(isDaemon);
            timer.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run() {
                    autoMove();
                }
            }, timeElapsed % refreshMillis, refreshMillis);
        }
    }

    public void pauseTimer() {
        if (timer != null)
            timer.cancel();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (slider != null) {
            setTime(MIN);
        }
    }

    private void autoMove() {
        if (slider != null && !slider.getValueIsAdjusting()) {
            long next = timeElapsed + refreshMillis;
            System.out.println(next);
            System.out.println(formatDuration(next));
            if (next < totalLength) {
                setTime(next);
            }
            else {
                setTime(MAX);
                stopTimer();
                setChanged();
                notifyObservers();
            }
        }
    }

    public void changeTime(int frame) {
        if (slider != null && !slider.getValueIsAdjusting()) {
            timeElapsed = framesToMillis(frame, freq);
            timeField.setText(formatDuration(timeElapsed));
            System.out.println(frame);
        }
    }

    private void setTime(long timeElapsed) {
        this.timeElapsed = timeElapsed;
        timeField.setText(formatDuration(timeElapsed));
        slider.setValue((int)((timeElapsed / 1000.0) * freq));
    }
}
