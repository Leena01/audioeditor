package view.panel;

import view.core.button.OptionButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class OptionPanel extends JPanel {
    private OptionButton mainMenuOptionButton;
    private OptionButton openFileOptionButton;
    private OptionButton viewSongsOptionButton;
    private OptionButton showDataButton;
    private OptionButton changePitchOptionButton;
    private OptionButton cutOptionButton;
    private OptionButton fftOptionButton;
    private OptionButton analyzeOptionButton;

    public OptionPanel(ActionListener mm, ActionListener of, ActionListener vs, ActionListener sd, ActionListener cp,
                       ActionListener cf, ActionListener fft, ActionListener as) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = GridBagConstraints.RELATIVE;
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTH;

        mainMenuOptionButton = new OptionButton("Main menu", mm);
        openFileOptionButton = new OptionButton("Open file", of);
        viewSongsOptionButton = new OptionButton("View favorite songs", vs);
        showDataButton = new OptionButton("View song data", sd);
        changePitchOptionButton = new OptionButton("Change pitch", cp);
        cutOptionButton = new OptionButton("Cut file", cf);
        fftOptionButton = new OptionButton("View spectrogram", fft);
        analyzeOptionButton = new OptionButton("Analyze song", as);
        add(mainMenuOptionButton, c);
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
