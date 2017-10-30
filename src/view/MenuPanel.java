package view;
import java.awt.*;
import java.awt.event.*;

import util.MatlabHandler;
import view.decorated.Button;
import view.decorated.Panel;

import javax.swing.*;

/**
 * Main menu
 */

/**
 * open file
 * favorite/disfav
 * play file
 * pause file
 * fast, slow
 * volume
 * FFT, spectrogram
 * registered songs
 */
class MenuPanel extends Panel {

    private JLabel infoLabel;
    private JButton openFileButton;
    private JButton viewSongsButton;
    private JButton changePitchButton;
    private JButton cutButton;
    private JButton fftButton;
    private JButton analyzeButton;
    private JPanel chooseFilePanel;
    private PlayerPanel playerPanel;
    private JPanel optionPanel;
    private JPanel infoPanel;
    private JPanel mainPanel;
    private JPanel bodyPanel;
    private JLabel noMediaFoundLabel;

    MenuPanel(MatlabHandler matlabHandler, ActionListener of, ActionListener vs, ActionListener fb,
              ActionListener ufb, ActionListener cp, ActionListener cf, ActionListener fft, ActionListener as) {
        super();
        infoPanel = new JPanel(new FlowLayout());
        chooseFilePanel = new JPanel(new FlowLayout());
        playerPanel = new PlayerPanel(matlabHandler, fb, ufb);
        playerPanel.setVisible(false);
        noMediaFoundLabel = new JLabel("No media found. Please choose a file.");
        noMediaFoundLabel.setForeground(Color.WHITE);
        optionPanel = new JPanel(new FlowLayout());
        bodyPanel = new JPanel(new FlowLayout());
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        infoPanel.setOpaque(false);
        chooseFilePanel.setOpaque(false);
        playerPanel.setOpaque(false);
        optionPanel.setOpaque(false);
        bodyPanel.setOpaque(false);
        mainPanel.setOpaque(false);

        infoLabel = new JLabel("Welcome! Please select a song and choose from the options below:");
        infoLabel.setForeground(Color.WHITE);
        openFileButton = new JButton("Open file");
        openFileButton.addActionListener(of);
        viewSongsButton = new JButton("View favorite songs");
        viewSongsButton.addActionListener(vs);

        changePitchButton = new Button("Change pitch");
        changePitchButton.addActionListener(cp);
        cutButton = new Button("Cut file");
        cutButton.addActionListener(cf);
        fftButton = new Button("View spectrogram");
        fftButton.addActionListener(fft);
        analyzeButton = new Button("Analyze song");
        analyzeButton.addActionListener(as);

        infoPanel.add(infoLabel);
        chooseFilePanel.add(openFileButton);
        chooseFilePanel.add(viewSongsButton);
        optionPanel.add(changePitchButton);
        optionPanel.add(cutButton);
        optionPanel.add(fftButton);
        optionPanel.add(analyzeButton);

        mainPanel.add(infoPanel);
        mainPanel.add(chooseFilePanel);
        mainPanel.add(playerPanel);
        mainPanel.add(noMediaFoundLabel);
        mainPanel.add(optionPanel);
        bodyPanel.add(mainPanel);

        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        chooseFilePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 30, 30));
        playerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        optionPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        add(bodyPanel, BorderLayout.CENTER);
        infoLabel.setForeground(Color.WHITE);
    }

    void setCurrentSong(double totalSamples, double freq) {
        noMediaFoundLabel.setVisible(false);
        playerPanel.setVisible(true);
        playerPanel.setCurrentSong(totalSamples, freq);
    }
}