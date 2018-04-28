package org.ql.audioeditor.view.core.slider;

import org.ql.audioeditor.logic.matlab.MatlabHandler;

import static org.ql.audioeditor.common.util.Helper.formatDuration;
import static org.ql.audioeditor.common.util.Helper.framesToMillis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.Timer;

public class SliderTimer extends Observable {
    private static final int MIN = 1;
    private static int MAX;

    private Timer timer;
    private JSlider slider;
    private MatlabHandler matlabHandler;
    private JLabel timeField;
    private JLabel totalLengthField;
    private boolean isDaemon;
    private int refreshMillis;
    private double freq;
    private int totalLength;

    public SliderTimer(JSlider slider, MatlabHandler matlabHandler, JLabel timeField, JLabel totalLengthField, boolean isDaemon) {
        this.timer = null;
        this.slider = slider;
        this.matlabHandler = matlabHandler;
        this.timeField = timeField;
        this.totalLengthField = totalLengthField;
        this.isDaemon = isDaemon;
        this.refreshMillis = 0;
        this.freq = 0.0;
        this.totalLength = 0;
        MAX = 0;
        slider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                double percent = p.x / ((double) slider.getWidth());
                int range = slider.getMaximum() - slider.getMinimum();
                double newVal = range * percent;
                int frame = (int)(slider.getMinimum() + newVal);
                changeTime(frame);
                new Thread(() -> matlabHandler.relocateSong(frame)).start();
            }
        });
    }

    public void schedule(int refreshMillis, double totalSamples, double freq) {
        this.refreshMillis = refreshMillis;
        MAX = (int)totalSamples;
        this.freq = freq;
        this.totalLength = (int)((MAX / freq) * 1000);
        this.totalLengthField.setText(formatDuration(totalLength));
        this.slider.setMinimum(MIN);
        this.slider.setMaximum(MAX);
        timeField.setText(formatDuration(framesToMillis(MIN, freq)));
    }

    public void resumeTimer() {
        if (refreshMillis != 0) {
            timer = new Timer(isDaemon);
            timer.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run() {
                    if (matlabHandler.isPlaying())
                        autoMove();
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
        if (timer != null)
            timer.cancel();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (slider != null) {
            setCurrentTime(MIN);
        }
    }

    private void autoMove() {
        if (slider != null && !slider.getValueIsAdjusting()) {
            setCurrentTime((int)matlabHandler.getCurrentFrame());
        }
    }

    public void changeTime(int frame) {
        if (slider != null) {
            if (frame < MIN)
                setCurrentTime(MIN);
            else if (frame > MAX)
                setCurrentTime(MAX);
            else
                setCurrentTime(frame);
        }
    }

    private void setCurrentTime(int currentFrame) {
        timeField.setText(formatDuration(framesToMillis(currentFrame, freq)));
        slider.setValue(currentFrame);
    }
}
