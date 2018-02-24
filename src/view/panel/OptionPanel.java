package view.panel;

import view.element.core.button.OptionButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class OptionPanel extends JPanel {
    private OptionButton openFileOptionButton;
    private OptionButton viewSongsOptionButton;
    private OptionButton showDataButton;
    private OptionButton changePitchOptionButton;
    private OptionButton cutOptionButton;
    private OptionButton fftOptionButton;
    private OptionButton analyzeOptionButton;

    public OptionPanel(ActionListener of, ActionListener vs, ActionListener sd, ActionListener cp, ActionListener cf,
                       ActionListener fft, ActionListener as) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = GridBagConstraints.RELATIVE;
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTH;

        openFileOptionButton = new OptionButton("Open file");
        openFileOptionButton.addActionListener(of);
        openFileOptionButton.addMouseListener();
        viewSongsOptionButton = new OptionButton("View favorite songs");
        viewSongsOptionButton.addActionListener(vs);
        viewSongsOptionButton.addMouseListener();
        showDataButton = new OptionButton("View song data");
        showDataButton.addActionListener(sd);
        showDataButton.addMouseListener();
        changePitchOptionButton = new OptionButton("Change pitch");
        changePitchOptionButton.addActionListener(cp);
        changePitchOptionButton.addMouseListener();
        cutOptionButton = new OptionButton("Cut file");
        cutOptionButton.addActionListener(cf);
        cutOptionButton.addMouseListener();
        fftOptionButton = new OptionButton("View spectrogram");
        fftOptionButton.addActionListener(fft);
        fftOptionButton.addMouseListener();
        analyzeOptionButton = new OptionButton("Analyze song");
        analyzeOptionButton.addActionListener(as);
        analyzeOptionButton.addMouseListener();
        add(openFileOptionButton, c);
        add(viewSongsOptionButton, c);
        add(showDataButton, c);
        add(changePitchOptionButton, c);
        add(cutOptionButton, c);
        add(fftOptionButton, c);
        add(analyzeOptionButton, c);
        setOpaque(false);
    }

    public void showOptions(boolean l) {
        showDataButton.setVisible(l);
        changePitchOptionButton.setVisible(l);
        cutOptionButton.setVisible(l);
        fftOptionButton.setVisible(l);
        analyzeOptionButton.setVisible(l);
    }
}
