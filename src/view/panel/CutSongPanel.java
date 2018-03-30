package view.panel;

import static util.Utils.formatDuration;
import static util.Utils.framesToMillis;
import static util.Utils.resizeImageIcon;
import static view.util.Constants.RANGE_SLIDER_SIZE;
import static view.util.Constants.RANGE_SLIDER_SIZE_MAX;

import view.core.slider.RangeSlider;
import view.core.label.Label;
import view.core.button.Button;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class CutSongPanel extends JPanel implements ChangeListener, ActionListener {
    private static final int MIN = 1;
    private static int MAX;
    private static final Dimension FIELD_SIZE = new Dimension(120, 20);

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
    private ImageIcon cutPlotIcon;
    private ImageIcon cutPlotIconMax;
    private double freq;

    public CutSongPanel(ActionListener cutDoneListener, ActionListener b) {
        setBackground(Color.BLACK);
        mainPanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new FlowLayout());
        formPanel = new JPanel(new GridLayout(3, 3));

        rangeSlider = new RangeSlider();
        rangeSlider.addChangeListener(this);
        rangeSlider.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        mainPanel.add(rangeSlider, BorderLayout.NORTH);

        cutLabel = new Label("Cut song");
        framesLabel = new Label("Frames: ");
        secondsLabel = new Label("Time: ");
        secondsLabel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        fromLabel = new Label("From:", SwingConstants.LEFT);
        toLabel = new Label("To:", SwingConstants.LEFT);
        fromValue = new JTextField("", SwingConstants.LEFT);
        toValue = new JTextField("", SwingConstants.LEFT);
        fromSecValue = new JLabel("", SwingConstants.LEFT);
        fromSecValue.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        toSecValue = new JLabel("", SwingConstants.LEFT);
        toSecValue.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));

        fromLabel.setPreferredSize(FIELD_SIZE);
        toLabel.setPreferredSize(FIELD_SIZE);
        fromSecValue.setPreferredSize(FIELD_SIZE);
        fromValue.setPreferredSize(FIELD_SIZE);
        toValue.setPreferredSize(FIELD_SIZE);
        toSecValue.setPreferredSize(FIELD_SIZE);
        fromValue.addActionListener(this);
        toValue.addActionListener(this);

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

        setButton = new Button("Set values", this);
        doneButton = new Button("Done", cutDoneListener);
        backOptionButton = new Button("Back to Main Menu", b);
        buttonPanel.add(setButton);
        buttonPanel.add(doneButton);
        buttonPanel.add(backOptionButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        formPanel.setOpaque(false);
        mainPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
    }

    public void setCurrentSong(double totalSamples, double freq, BufferedImage plot, boolean isMaximized) {
        MAX = (int)totalSamples;
        rangeSlider.setMinimum(MIN);
        rangeSlider.setMaximum(MAX);
        rangeSlider.setValue(MIN);
        rangeSlider.setUpperValue(MAX);
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
                    fromSecValue.setText(formatDuration(framesToMillis(newValue, freq)));
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
                    int newUpperValue = Integer.parseInt(fromValue.getText());
                    toSecValue.setText(formatDuration(framesToMillis(newUpperValue, freq)));
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
        if (isMaximized)
            rangeSlider.setPreferredSize(RANGE_SLIDER_SIZE_MAX);
        else
            rangeSlider.setPreferredSize(RANGE_SLIDER_SIZE);
    }

    private void changeFieldValues() {
        int lowerValue = rangeSlider.getValue();
        int upperValue = rangeSlider.getUpperValue();
        fromValue.setText(String.valueOf(lowerValue));
        toValue.setText(String.valueOf(upperValue));
        fromSecValue.setText(formatDuration(framesToMillis(lowerValue, freq)));
        toSecValue.setText(formatDuration(framesToMillis(upperValue, freq)));
    }
}
