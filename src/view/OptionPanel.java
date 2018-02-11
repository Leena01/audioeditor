package view;

import view.decorated.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static util.Utils.fillColor;

public class OptionPanel extends JPanel {
    private static final Dimension BUTTON_SIZE = new Dimension(140, 30);

    private JButton changePitchButton;
    private JButton cutButton;
    private JButton fftButton;
    private JButton analyzeButton;

    public OptionPanel(ActionListener cp, ActionListener cf, ActionListener fft, ActionListener as) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = GridBagConstraints.RELATIVE;
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTH;

        changePitchButton = new Button("Change pitch", BUTTON_SIZE);
        changePitchButton.addActionListener(cp);
        cutButton = new Button("Cut file", BUTTON_SIZE);
        cutButton.addActionListener(cf);
        fftButton = new Button("View spectrogram", BUTTON_SIZE);
        fftButton.addActionListener(fft);
        analyzeButton = new Button("Analyze song", BUTTON_SIZE);
        analyzeButton.addActionListener(as);
        add(changePitchButton, c);
        add(cutButton, c);
        add(fftButton, c);
        add(analyzeButton, c);
        setOpaque(false);
    }
}
