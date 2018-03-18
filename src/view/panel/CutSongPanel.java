package view.panel;

import view.element.slider.RangeSlider;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class CutSongPanel extends JPanel implements ChangeListener {
    private static final int MIN = 1;
    private static int MAX;

    private RangeSlider rangeSlider;
    private JLabel fromLabel;
    private JLabel toLabel;
    private JLabel fromValue;
    private JLabel toValue;
    private JButton doneButton;
    private GridBagConstraints c;
    private GridBagLayout gridbag;

    public CutSongPanel(ActionListener cutDoneListener) {
        setBackground(Color.BLACK);
        gridbag = new GridBagLayout();
        setLayout(gridbag);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        gridbag.setConstraints(this, c);

        rangeSlider = new RangeSlider();
        rangeSlider.addChangeListener(this);
        rangeSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        add(rangeSlider, c);

        fromLabel = new JLabel("From:", SwingConstants.LEFT);
        toLabel = new JLabel("To:", SwingConstants.LEFT);
        fromValue = new JLabel("", SwingConstants.LEFT);
        toValue = new JLabel("", SwingConstants.LEFT);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 0, 30);
        add(fromLabel, c);
        c.gridx = 1;
        c.gridy = 1;
        add(fromValue, c);
        c.gridx = 0;
        c.gridy = 2;
        add(toLabel, c);
        c.gridx = 1;
        c.gridy = 2;
        add(toValue, c);

        doneButton = new JButton("Done");
        doneButton.addActionListener(cutDoneListener);
        c.gridx = 2;
        c.gridy = 1;
        add(doneButton, c);
    }

    public void setCurrentSong(double totalSamples, BufferedImage plot) {
        MAX = (int)totalSamples;
        rangeSlider.setMinimum(MIN);
        rangeSlider.setMaximum(MAX);
        rangeSlider.setValue(MIN);
        rangeSlider.setUpperValue(MAX);
        fromValue.setText(String.valueOf(rangeSlider.getValue()));
        toValue.setText(String.valueOf(rangeSlider.getUpperValue()));
        rangeSlider.setImage(plot);
    }

    public int getFrom() {
        return rangeSlider.getValue();
    }

    public int getTo() {
        return rangeSlider.getUpperValue();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        RangeSlider slider = (RangeSlider) e.getSource();
        fromValue.setText(String.valueOf(slider.getValue()));
        toValue.setText(String.valueOf(slider.getUpperValue()));
    }
}
