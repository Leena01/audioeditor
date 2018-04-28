package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.panel.BasicPanel;
import org.ql.audioeditor.view.core.slider.RangeSlider;
import org.ql.audioeditor.view.core.textfield.TextField;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import static org.ql.audioeditor.common.util.Helper.formatDuration;
import static org.ql.audioeditor.common.util.Helper.framesToMillis;
import static org.ql.audioeditor.view.param.Constants.RANGE_SLIDER_SIZE;
import static org.ql.audioeditor.view.param.Constants.RANGE_SLIDER_SIZE_MAX;

public final class CutSongPanel extends BasicPanel
    implements ChangeListener, ActionListener {
    private static final int MIN = 1;
    private static final Dimension FIELD_SIZE = new Dimension(120, 20);
    private static final Border RANGE_SLIDER_BORDER =
        BorderFactory.createEmptyBorder(10, 0, 15, 0);
    private static final Border SECONDS_LABEL_BORDER =
        BorderFactory.createEmptyBorder(0, 6, 0, 6);
    private static final Border FROM_SEC_VALUE_BORDER =
        BorderFactory.createEmptyBorder(0, 6, 0, 6);
    private static final Border TO_SEC_VALUE_BORDER =
        BorderFactory.createEmptyBorder(0, 6, 0, 6);
    private static final Border BUTTON_PANEL_BORDER =
        BorderFactory.createEmptyBorder(10, 0, 0, 0);
    private static int MAX;
    private RangeSlider rangeSlider;
    private JLabel cutLabel;
    private JLabel framesLabel;
    private JLabel secondsLabel;
    private JLabel fromLabel;
    private JLabel toLabel;
    private JTextField fromValue;
    private JTextField toValue;
    private JLabel fromSecValue;
    private JLabel toSecValue;
    private JButton setButton;
    private JButton doneButton;
    private JButton backOptionButton;
    private JPanel formPanel;
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private double freq;

    public CutSongPanel(ActionListener cutDoneListener, ActionListener b) {
        mainPanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new FlowLayout());
        formPanel = new JPanel(new GridLayout(3, 3));
        rangeSlider = new RangeSlider();
        cutLabel = new Label("Cut song");
        framesLabel = new Label("Frames: ");
        secondsLabel = new Label("Time: ");
        fromLabel = new Label("From:", FIELD_SIZE, SwingConstants.LEFT);
        toLabel = new Label("To:", FIELD_SIZE, SwingConstants.LEFT);
        fromValue = new TextField("", FIELD_SIZE, SwingConstants.LEFT);
        toValue = new TextField("", FIELD_SIZE, SwingConstants.LEFT);
        fromSecValue = new Label("", FIELD_SIZE, SwingConstants.LEFT);
        toSecValue = new Label("", FIELD_SIZE, SwingConstants.LEFT);
        setButton = new Button("Set values", this);
        doneButton = new Button("Done", cutDoneListener);
        backOptionButton = new Button("Back to Main Menu", b);
        initInnerListeners();
        setStyle();
        addPanels();
    }

    public void setCurrentSong(double totalSamples, double freq,
        BufferedImage plot, boolean isMaximized) {
        MAX = (int) totalSamples;
        rangeSlider.setMinimum(MIN);
        rangeSlider.setMaximum(MAX);
        setDefaultValues();
        rangeSlider.setImage(plot);
        this.freq = freq;
        changeFieldValues();
        maximizeImage(isMaximized);
    }

    public int getFrom() {
        return rangeSlider.getValue();
    }

    public int getTo() {
        return rangeSlider.getUpperValue();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == rangeSlider)
            changeFieldValues();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == setButton) {
            try {
                int value = Integer.parseInt(fromValue.getText());
                if (value >= MIN && value <= MAX) {
                    rangeSlider.setValue(value);
                    int newValue = Integer.parseInt(fromValue.getText());
                    fromSecValue.setText(
                        formatDuration(framesToMillis(newValue, freq)));
                }
                else {
                    int oldValue = rangeSlider.getValue();
                    fromValue.setText(String.valueOf(oldValue));
                }
            } catch (NumberFormatException nfe) {
                fromValue.setText(String.valueOf(rangeSlider.getValue()));
            }
            try {
                int upperValue = Integer.parseInt(toValue.getText());
                if (upperValue >= MIN && upperValue <= MAX) {
                    rangeSlider.setUpperValue(upperValue);
                    int newUpperValue = Integer.parseInt(toValue.getText());
                    toSecValue.setText(
                        formatDuration(framesToMillis(newUpperValue, freq)));
                }
                else {
                    int oldUpperValue = rangeSlider.getUpperValue();
                    toValue.setText(String.valueOf(oldUpperValue));
                }
            } catch (NumberFormatException nfe) {
                toValue.setText(String.valueOf(rangeSlider.getUpperValue()));
            }
        }
    }

    public void maximizeImage(boolean isMaximized) {
        if (isMaximized) {
            rangeSlider.setPreferredSize(RANGE_SLIDER_SIZE_MAX);
            rangeSlider.setSize(RANGE_SLIDER_SIZE_MAX);
        }
        else {
            rangeSlider.setPreferredSize(RANGE_SLIDER_SIZE);
            rangeSlider.setSize(RANGE_SLIDER_SIZE);
        }
    }

    private void changeFieldValues() {
        int lowerValue = rangeSlider.getValue();
        int upperValue = rangeSlider.getUpperValue();
        fromValue.setText(String.valueOf(lowerValue));
        toValue.setText(String.valueOf(upperValue));
        fromSecValue.setText(formatDuration(framesToMillis(lowerValue, freq)));
        toSecValue.setText(formatDuration(framesToMillis(upperValue, freq)));
    }

    public void setDefaultValues() {
        rangeSlider.setValue(MIN);
        rangeSlider.setUpperValue(MAX);
    }

    private void initInnerListeners() {
        rangeSlider.addChangeListener(this);
        fromValue.addActionListener(this);
        toValue.addActionListener(this);
    }

    @Override
    protected void setStyle() {
        setBackground(Color.BLACK);
        rangeSlider.setBorder(RANGE_SLIDER_BORDER);
        secondsLabel.setBorder(SECONDS_LABEL_BORDER);
        fromSecValue.setBorder(FROM_SEC_VALUE_BORDER);
        toSecValue.setBorder(TO_SEC_VALUE_BORDER);
        buttonPanel.setBorder(BUTTON_PANEL_BORDER);
        formPanel.setOpaque(false);
        mainPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
    }

    @Override
    protected void addPanels() {
        mainPanel.add(rangeSlider, BorderLayout.NORTH);
        formPanel.add(cutLabel);
        formPanel.add(framesLabel);
        formPanel.add(secondsLabel);
        formPanel.add(fromLabel);
        formPanel.add(fromValue);
        formPanel.add(fromSecValue);
        formPanel.add(toLabel);
        formPanel.add(toValue);
        formPanel.add(toSecValue);
        mainPanel.add(formPanel, BorderLayout.WEST);
        buttonPanel.add(setButton);
        buttonPanel.add(doneButton);
        buttonPanel.add(backOptionButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }
}