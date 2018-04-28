package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.panel.BasicPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import static javax.swing.BoxLayout.PAGE_AXIS;
import static org.ql.audioeditor.common.util.Helper.resizeImageIcon;
import static org.ql.audioeditor.view.param.Constants.COVER_SIZE;
import static org.ql.audioeditor.view.param.Constants.COVER_SIZE_MAX;

/**
 * Main menu
 */

public final class MenuPanel extends BasicPanel {
    private static final String NO_MEDIA_LABEL =
        "No media found. Please choose a file.";
    private static final String INFO_LABEL = "Welcome! Please select a song " +
        "and choose from the options on the left.";
    private static final ImageIcon COVER_IMAGE =
        resizeImageIcon(new ImageIcon(ImageLoader.getCoverURL()), COVER_SIZE);
    private static final ImageIcon COVER_IMAGE_MAX =
        resizeImageIcon(new ImageIcon(ImageLoader.getCoverURL()),
            COVER_SIZE_MAX);
    private static final Border INFO_PANEL_BORDER =
        BorderFactory.createEmptyBorder(0, 0, 15, 0);
    private static final Border BODY_PANEL_BORDER =
        BorderFactory.createEmptyBorder(15, 30, 30, 30);
    private static final Border PLAYER_PANEL_BORDER =
        BorderFactory.createEmptyBorder(15, 15, 15, 15);

    private JLabel infoLabel;
    private PlayerPanel playerPanel;
    private JLabel imageLabel;
    private JPanel infoPanel;
    private JPanel imagePanel;
    private JPanel mainPanel;
    private JPanel bodyPanel;
    private JLabel noMediaFoundLabel;
    private ImageIcon coverIcon;
    private ImageIcon coverIconMax;

    public MenuPanel(MatlabHandler matlabHandler, HorizontalBar
        mediaControlPanel, InputMap inputMap, ActionMap
        actionMap, ActionListener fb, ActionListener ufb) {
        super();
        infoLabel = new Label(INFO_LABEL);
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        coverIcon = COVER_IMAGE;
        coverIconMax = COVER_IMAGE_MAX;
        imageLabel = new JLabel(coverIcon);
        imagePanel = new JPanel();
        playerPanel =
            new PlayerPanel(matlabHandler, mediaControlPanel, inputMap,
                actionMap, fb, ufb);
        noMediaFoundLabel = new Label(NO_MEDIA_LABEL);
        bodyPanel = new JPanel(new FlowLayout());
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, PAGE_AXIS));
        setStyle();
        addPanels();
        init();
    }

    public void setCurrentSong(double totalSamples, double freq,
        BufferedImage plot, Image cover,
        boolean isNormal, boolean isMaximized) {
        infoPanel.setVisible(false);
        coverIcon.getImage().flush();
        coverIconMax.getImage().flush();
        coverIcon = new ImageIcon(cover);
        coverIconMax = resizeImageIcon(coverIcon, COVER_SIZE_MAX);
        coverIcon = resizeImageIcon(coverIcon, COVER_SIZE);
        maximizeImage(isMaximized);
        if (isNormal)
            imagePanel.setVisible(true);
        playerPanel.setVisible(true);
        playerPanel.setCurrentSong(totalSamples, freq, plot);
    }

    public void hideImage(boolean isHidden) {
        if (isHidden)
            imagePanel.setVisible(false);
        else
            imagePanel.setVisible(true);
    }

    public void maximizeImage(boolean isMaximized) {
        if (isMaximized)
            imageLabel.setIcon(coverIconMax);
        else
            imageLabel.setIcon(coverIcon);
    }

    public void setFavorite(boolean isFavorite) {
        playerPanel.setFavorite(isFavorite);
    }

    public void pauseSong() {
        playerPanel.pauseSong();
    }

    private void init() {
        playerPanel.setVisible(false);
        imagePanel.setVisible(false);
    }

    @Override
    protected void setStyle() {
        setBackground(Color.BLACK);
        noMediaFoundLabel.setHorizontalAlignment(JLabel.RIGHT);
        infoPanel.setOpaque(false);
        imagePanel.setOpaque(false);
        playerPanel.setOpaque(false);
        bodyPanel.setOpaque(false);
        mainPanel.setOpaque(false);
        infoPanel.setBorder(INFO_PANEL_BORDER);
        bodyPanel.setBorder(BODY_PANEL_BORDER);
        playerPanel.setBorder(PLAYER_PANEL_BORDER);
    }

    @Override
    protected void addPanels() {
        infoPanel.add(infoLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        infoPanel.add(noMediaFoundLabel);
        imagePanel.add(imageLabel);
        mainPanel.add(infoPanel);
        mainPanel.add(imagePanel);
        mainPanel.add(playerPanel);
        bodyPanel.add(mainPanel);
        add(bodyPanel, BorderLayout.CENTER);
    }
}