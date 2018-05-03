package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.panel.BasicPanel;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import static javax.swing.BoxLayout.PAGE_AXIS;
import static org.ql.audioeditor.common.util.Helper.resizeImageIcon;
import static org.ql.audioeditor.view.param.Constants.COVER_SIZE;
import static org.ql.audioeditor.view.param.Constants.COVER_SIZE_MAX;

/**
 * Main menu. Contains the media control panel in case a song is loaded.
 */
public final class MenuPanel extends BasicPanel {
    private static final String NO_MEDIA_LABEL =
        "No media found. Please choose a file.";
    private static final String INFO_LABEL = "Welcome! Please select a song "
        + "and choose from the options on the left.";
    private static final Dimension DISTANCE_DIMENSION = new Dimension(0, 30);
    private static final ImageIcon COVER_IMAGE =
        resizeImageIcon(new ImageIcon(ImageLoader.getCover()), COVER_SIZE);
    private static final ImageIcon COVER_IMAGE_MAX =
        resizeImageIcon(new ImageIcon(ImageLoader.getCover()),
            COVER_SIZE_MAX);
    private static final Border INFO_PANEL_BORDER =
        BorderFactory.createEmptyBorder(0, 0, 15, 0);
    private static final Border BODY_PANEL_BORDER =
        BorderFactory.createEmptyBorder(15, 30, 30, 30);
    private static final Border PLAYER_PANEL_BORDER =
        BorderFactory.createEmptyBorder(15, 15, 15, 15);

    private final JLabel infoLabel;
    private final PlayerPanel playerPanel;
    private final JLabel imageLabel;
    private final JPanel infoPanel;
    private final JPanel imagePanel;
    private final JPanel mainPanel;
    private final JPanel bodyPanel;
    private final JLabel noMediaFoundLabel;
    private ImageIcon coverIcon;
    private ImageIcon coverIconMax;

    public MenuPanel(MatlabHandler matlabHandler, HorizontalBar
        mediaControlPanel, InputMap inputMap, ActionMap
        actionMap, ActionListener fb, ActionListener ufb, ActionListener sss) {
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
                actionMap, fb, ufb, sss);
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
        boolean isNormal, boolean isMaximized, Action makeFavoriteAction,
        Action showRelatedAction) {
        infoPanel.setVisible(false);
        coverIcon.getImage().flush();
        coverIconMax.getImage().flush();
        coverIcon = new ImageIcon(cover);
        coverIconMax = resizeImageIcon(coverIcon, COVER_SIZE_MAX);
        coverIcon = resizeImageIcon(coverIcon, COVER_SIZE);
        maximizeImage(isMaximized);
        if (isNormal) {
            imagePanel.setVisible(true);
        }
        playerPanel.setVisible(true);
        playerPanel.setCurrentSong(totalSamples, freq, plot,
            makeFavoriteAction, showRelatedAction);
    }

    public void hideImage(boolean isHidden) {
        if (isHidden) {
            imagePanel.setVisible(false);
        } else {
            imagePanel.setVisible(true);
        }
    }

    public void maximizeImage(boolean isMaximized) {
        if (isMaximized) {
            imageLabel.setIcon(coverIconMax);
        } else {
            imageLabel.setIcon(coverIcon);
        }
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
        infoPanel.add(Box.createRigidArea(DISTANCE_DIMENSION));
        infoPanel.add(noMediaFoundLabel);
        imagePanel.add(imageLabel);
        mainPanel.add(infoPanel);
        mainPanel.add(imagePanel);
        mainPanel.add(playerPanel);
        bodyPanel.add(mainPanel);
        add(bodyPanel, BorderLayout.CENTER);
    }
}
