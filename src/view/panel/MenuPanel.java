package view.panel;

import static util.Utils.resizeImageIcon;
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
    private static final String NO_MEDIA_LABEL = "No media found. Please choose a file.";
    private static final String INFO_LABEL = "Welcome! Please select a song and choose from the options on the left.";
    private static final ImageIcon COVER_IMAGE = resizeImageIcon(new ImageIcon(COVER_NAME), COVER_SIZE);

    private JLabel infoLabel;
    private PlayerPanel playerPanel;
    private JLabel imageLabel;
    private JPanel infoPanel;
    private JPanel imagePanel;
    private JPanel mainPanel;
    private JPanel bodyPanel;
    private JLabel noMediaFoundLabel;
    private ImageIcon coverIcon;

    public MenuPanel(MatlabHandler matlabHandler, Component glassPane, ActionListener fb, ActionListener ufb) {
        super();
        setBackground(Color.BLACK);
        infoPanel = new JPanel(new FlowLayout());
        imagePanel = new JPanel();
        coverIcon = COVER_IMAGE;
        imageLabel = new JLabel(coverIcon);
        playerPanel = new PlayerPanel(matlabHandler, glassPane, fb, ufb);
        playerPanel.setVisible(false);
        noMediaFoundLabel = new Label(NO_MEDIA_LABEL);
        bodyPanel = new JPanel(new FlowLayout());
        mainPanel = new JPanel();

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        infoPanel.setOpaque(false);
        imagePanel.setOpaque(false);
        playerPanel.setOpaque(false);
        bodyPanel.setOpaque(false);
        mainPanel.setOpaque(false);

        infoLabel = new Label(INFO_LABEL);
        infoPanel.add(infoLabel);
        infoPanel.add(noMediaFoundLabel);
        imagePanel.add(imageLabel);

        mainPanel.add(infoPanel);
        mainPanel.add(imagePanel);
        mainPanel.add(playerPanel);
        bodyPanel.add(mainPanel);

        imagePanel.setVisible(false);
        noMediaFoundLabel.setHorizontalAlignment(JLabel.RIGHT);

        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 30, 30));
        playerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(bodyPanel, BorderLayout.CENTER);
    }

    public void setCurrentSong(double totalSamples, double freq, BufferedImage plot, Image cover, boolean isNormal) {
        infoPanel.setVisible(false);
        coverIcon = new ImageIcon(cover);
        coverIcon = resizeImageIcon(coverIcon, COVER_SIZE);
        imageLabel.setIcon(coverIcon);
        if (isNormal)
            imagePanel.setVisible(true);
        playerPanel.setVisible(true);
        playerPanel.setCurrentSong(totalSamples, freq, plot);
    }

    public void hideCover(boolean isHidden) {
        if (isHidden)
            imagePanel.setVisible(false);
        else
            imagePanel.setVisible(true);
    }

    public void maximizeCover(boolean isMaximized) {
        if (isMaximized)
            coverIcon = resizeImageIcon(coverIcon, COVER_SIZE_MAX);
        else
            coverIcon = resizeImageIcon(coverIcon, COVER_SIZE);
        imageLabel.setIcon(coverIcon);
    }
}