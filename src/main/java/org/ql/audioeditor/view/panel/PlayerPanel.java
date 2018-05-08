package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.common.util.DoubleActionTool;
import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.core.button.TransparentButton;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static org.ql.audioeditor.common.util.ViewUtils.resizeImageIcon;
import static org.ql.audioeditor.view.enums.ActionName.MOVE_BACKWARD;
import static org.ql.audioeditor.view.enums.ActionName.MOVE_FORWARD;
import static org.ql.audioeditor.view.enums.ActionName.MUTE;
import static org.ql.audioeditor.view.enums.ActionName.PAUSE;
import static org.ql.audioeditor.view.enums.ActionName.STOP;
import static org.ql.audioeditor.view.enums.ActionName.VOLUME_DOWN;
import static org.ql.audioeditor.view.enums.ActionName.VOLUME_UP;

/**
 * Panel for playing media, saving files to the database and show
 * recommendations.
 */
final class PlayerPanel extends SimplePlayerPanel {

    private static final ImageIcon FAVORITE_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getFavoriteIcon()),
            BUTTON_SIZE);
    private static final ImageIcon UNFAVORITE_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getUnfavoriteIcon()),
            BUTTON_SIZE);
    private static final ImageIcon PLAYLIST_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getPlaylistIcon()),
            SOUND_BUTTON_SIZE);

    private final JButton favoriteButton;
    private final JButton unfavoriteButton;
    private final JButton similarSongsButton;
    private final InputMap inputMap;
    private final ActionMap actionMap;
    private boolean active;

    /**
     * Constructor.
     *
     * @param matlabHandler     Matlab handler
     * @param mediaControlPanel Media control panel
     * @param inputMap          Input map
     * @param actionMap         Action map
     * @param fb                FavoriteButton listener
     * @param ufb               UnfavoriteButton listener
     * @param p                 PreviousButton listener
     * @param n                 NextButton listener
     * @param sss               SimilarSongsButton listener
     */
    PlayerPanel(MatlabHandler matlabHandler, HorizontalBar mediaControlPanel,
        InputMap inputMap, ActionMap actionMap, ActionListener fb,
        ActionListener ufb, ActionListener p, ActionListener n,
        ActionListener sss) {
        super(matlabHandler, mediaControlPanel);
        this.inputMap = inputMap;
        this.actionMap = actionMap;
        active = false;

        favoriteButton = new TransparentButton(FAVORITE_ICON, BUTTON_SIZE, fb);
        unfavoriteButton =
            new TransparentButton(UNFAVORITE_ICON, BUTTON_SIZE, ufb);
        similarSongsButton =
            new TransparentButton(PLAYLIST_ICON, BUTTON_SIZE, sss);

        initKeyBindings();
        initAdditionalListeners(p, n);
        addAdditional();
        initAdditional();
    }

    /**
     * Saves the current song/removes the current song from the database.
     *
     * @param isFavorite Is the current song in the database (true if saved)
     */
    void setFavorite(boolean isFavorite) {
        if (isFavorite) {
            favoriteButton.setVisible(false);
            unfavoriteButton.setVisible(true);
        } else {
            unfavoriteButton.setVisible(false);
            favoriteButton.setVisible(true);
        }
    }

    /**
     * Returns whether the playerPanel is currently visible.
     *
     * @return Logical value (true if active)
     */
    boolean isActive() {
        return active;
    }

    /**
     * Sets the playerpanel's visibility.
     *
     * @param active New visibility (true if active)
     */
    void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Initializes key bindings.
     */
    private void initKeyBindings() {
        Action moveBackwardAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (active && !DoubleActionTool.isDoubleAction(e)) {
                    moveBackward();
                }
            }
        };
        Action moveForwardAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (active && !DoubleActionTool.isDoubleAction(e)) {
                    moveForward();
                }
            }
        };
        Action volumeUpAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (active && !DoubleActionTool.isDoubleAction(e)) {
                    volumeUp();
                }
            }
        };
        Action volumeDownAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (active && !DoubleActionTool.isDoubleAction(e)) {
                    volumeDown();
                }
            }
        };
        Action pauseAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (active && !DoubleActionTool.isDoubleAction(e)) {
                    if (isPlaying()) {
                        pauseSong();
                    } else {
                        playSong();
                    }
                }
            }
        };
        Action stopAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (active && !DoubleActionTool.isDoubleAction(e)) {
                    stopSong();
                }
            }
        };
        Action muteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (active && !DoubleActionTool.isDoubleAction(e)) {
                    if (isMute()) {
                        turnVolumeOn();
                    } else {
                        turnVolumeOff();
                    }
                }
            }
        };

        KeyStroke leftKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
        KeyStroke rightKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
        KeyStroke upKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
        KeyStroke downKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
        KeyStroke pKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_P, 0);
        KeyStroke sKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0);
        KeyStroke mKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_M, 0);
        inputMap.put(leftKeyStroke, MOVE_BACKWARD);
        actionMap.put(MOVE_BACKWARD, moveBackwardAction);
        inputMap.put(rightKeyStroke, MOVE_FORWARD);
        actionMap.put(MOVE_FORWARD, moveForwardAction);
        inputMap.put(upKeyStroke, VOLUME_UP);
        actionMap.put(VOLUME_UP, volumeUpAction);
        inputMap.put(downKeyStroke, VOLUME_DOWN);
        actionMap.put(VOLUME_DOWN, volumeDownAction);
        inputMap.put(pKeyStroke, PAUSE);
        actionMap.put(PAUSE, pauseAction);
        inputMap.put(sKeyStroke, STOP);
        actionMap.put(STOP, stopAction);
        inputMap.put(mKeyStroke, MUTE);
        actionMap.put(MUTE, muteAction);
    }

    /**
     * Adds additional elements to this panel.
     */
    private void addAdditional() {
        buttonPanel.add(favoriteButton);
        buttonPanel.add(unfavoriteButton);
        volumePanel.add(similarSongsButton);
    }

    /**
     * Initializes additional GUI elements.
     */
    private void initAdditional() {
        setFavorite(false);
        previousButton.setVisible(true);
        nextButton.setVisible(true);
    }
}
