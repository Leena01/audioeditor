package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.panel.BasicPanel;

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
import static org.ql.audioeditor.common.util.ViewUtils.resizeImageIcon;
import static org.ql.audioeditor.view.param.Constants.COVER_SIZE;
import static org.ql.audioeditor.view.param.Constants.COVER_SIZE_MAX;
import static org.ql.audioeditor.view.param.Constants.PLAYER_PANEL_BORDER;

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

    /**
     * Constructor.
     *
     * @param matlabHandler     Matlab handler
     * @param mediaControlPanel Media control panel
     * @param inputMap          Input map
     * @param actionMap         Action map
     * @param fb                FavoriteButton listener
     * @param ufb               Unfavorite button listener
     * @param p                 PreviousButton listener
     * @param n                 NextButton listener
     * @param sss               Show similar songs listener
     */
    public MenuPanel(MatlabHandler matlabHandler, HorizontalBar
        mediaControlPanel, InputMap inputMap, ActionMap
        actionMap, ActionListener fb, ActionListener ufb, ActionListener p,
        ActionListener n, ActionListener sss) {
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
                actionMap, fb, ufb, p, n, sss);
        noMediaFoundLabel = new Label(NO_MEDIA_LABEL);
        bodyPanel = new JPanel(new FlowLayout());
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, PAGE_AXIS));
        setStyle();
        addPanels();
        init();
    }

    /**
     * Sets the current song.
     *
     * @param totalSamples Total number of samples
     * @param freq         Sampling rate
     * @param plot         Plot
     * @param cover        Cover
     * @param isNormal     Is the image size normal
     * @param isMaximized  Is the window maximized
     */
    public void setCurrentSong(double totalSamples, double freq,
        BufferedImage plot, Image cover,
        boolean isNormal, boolean isMaximized) {
        infoPanel.setVisible(false);
        if (coverIcon.getImage() != null) {
            coverIcon.getImage().flush();
        }
        if (coverIconMax.getImage() != null) {
            coverIconMax.getImage().flush();
        }
        coverIcon = new ImageIcon(cover);
        coverIconMax = resizeImageIcon(coverIcon, COVER_SIZE_MAX);
        coverIcon = resizeImageIcon(coverIcon, COVER_SIZE);
        maximizeImage(isMaximized);
        if (isNormal) {
            imagePanel.setVisible(true);
        }
        playerPanel.setVisible(true);
        playerPanel.setCurrentSong(totalSamples, freq, plot);
    }

    /**
     * Hides the image.
     *
     * @param isHidden Is the image hidden
     */
    public void hideImage(boolean isHidden) {
        if (isHidden) {
            imagePanel.setVisible(false);
        } else {
            imagePanel.setVisible(true);
        }
    }

    /**
     * Maximizes the image.
     *
     * @param isMaximized Is the image maximized
     */
    public void maximizeImage(boolean isMaximized) {
        if (isMaximized) {
            imageLabel.setIcon(coverIconMax);
        } else {
            imageLabel.setIcon(coverIcon);
        }
    }

    /**
     * Saves the current song/removes the current song from the database.
     *
     * @param isFavorite Is the current song in the database (true if saved)
     */
    public void setFavorite(boolean isFavorite) {
        playerPanel.setFavorite(isFavorite);
    }

    /**
     * Returns whether the playerPanel is currently visible.
     *
     * @return Logical value (true if active)
     */
    public boolean isActive() {
        return playerPanel.isActive();
    }

    /**
     * Sets the playerpanel's visibility.
     *
     * @param active New visibility (true if active)
     */
    public void setActive(boolean active) {
        playerPanel.setActive(active);
    }

    /**
     * Plays/resumes the current song.
     */
    public void playSong() {
        playerPanel.playSong();
    }

    /**
     * Pauses the current song.
     */
    public void pauseSong() {
        playerPanel.pauseSong();
    }

    /**
     * Stops the current song.
     */
    public void stopSong() {
        playerPanel.stopSong();
    }

    /**
     * Sets the volume to default.
     */
    public void resetVolume() {
        playerPanel.resetVolume();
    }

    /**
     * Initializes GUI elements.
     */
    private void init() {
        playerPanel.setVisible(false);
        imagePanel.setVisible(false);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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
