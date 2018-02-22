package view.panel;

import static view.util.Constants.*;
import view.element.core.label.Label;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import logic.matlab.MatlabHandler;

import javax.swing.*;

/**
 * Main menu
 */

/*
 * open file
 * favorite/disfav
 * play file
 * pause file
 * fast, slow
 * volume
 * FFT, spectrogram
 * registered songs
 */
public class MenuPanel extends JPanel {
    private JLabel infoLabel;
    private PlayerPanel playerPanel;
    private JLabel imageLabel;
    private JPanel infoPanel;
    private JPanel mainPanel;
    private JPanel bodyPanel;
    private JLabel noMediaFoundLabel;

    public MenuPanel(MatlabHandler matlabHandler, Component glassPane, ActionListener fb, ActionListener ufb) {
        super();
        setBackground(Color.BLACK);
        infoPanel = new JPanel(new FlowLayout());
        imageLabel = new JLabel(COVER_IMAGE);
        playerPanel = new PlayerPanel(matlabHandler, glassPane, fb, ufb);
        playerPanel.setVisible(false);
        noMediaFoundLabel = new Label("No media found. Please choose a file.");
        // optionPanel.setVisible(false);
        bodyPanel = new JPanel(new FlowLayout());
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        infoPanel.setOpaque(false);
        playerPanel.setOpaque(false);
        bodyPanel.setOpaque(false);
        mainPanel.setOpaque(false);

        infoLabel = new Label("Welcome! Please select a song and choose from the options on the left.");
        infoPanel.add(infoLabel);

        mainPanel.add(infoPanel);
        mainPanel.add(imageLabel);
        // mainPanel.add(playerPanel);
        mainPanel.add(noMediaFoundLabel);
        bodyPanel.add(mainPanel);

        imageLabel.setVisible(false);

        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 30, 30));
        playerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(bodyPanel, BorderLayout.CENTER);
    }

    public void setCurrentSong(double totalSamples, double freq, BufferedImage plot, Image cover) {
        noMediaFoundLabel.setVisible(false);
        imageLabel.setIcon(new ImageIcon(cover));
        imageLabel.setVisible(true);
        playerPanel.setVisible(true);
        playerPanel.setCurrentSong(totalSamples, freq, plot);
    }
}