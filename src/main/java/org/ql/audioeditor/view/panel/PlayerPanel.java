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

/**
 * Panel for playing media, saving files to the database and show
 * recommendations.
 */
class PlayerPanel extends SimplePlayerPanel {
    private static final String MOVE_BACKWARD = "moveBackwardAction";
    private static final String MOVE_FORWARD = "moveForwardAction";
    private static final String VOLUME_UP = "volumeUpAction";
    private static final String VOLUME_DOWN = "volumeDownAction";
    private static final String PAUSE = "pauseAction";
    private static final String STOP = "stopAction";
    private static final String MUTE = "muteAction";

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
     * @param matlabHandler Matlab handler
     * @param mediaControlPanel Media control panel
     * @param inputMap Input map
     * @param actionMap Action map
     * @param fb FavoriteButton listener
     * @param ufb InfavoriteButton listener
     * @param sss SimilarSongsButton listener
     */
    PlayerPanel(MatlabHandler matlabHandler, HorizontalBar mediaControlPanel,
        InputMap inputMap, ActionMap actionMap, ActionListener fb,
        ActionListener ufb, ActionListener sss) {
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
        addAdditionalPanels();
        initAdditional();
    }

    void setFavorite(boolean isFavorite) {
        if (isFavorite) {
            favoriteButton.setVisible(false);
            unfavoriteButton.setVisible(true);
        } else {
            unfavoriteButton.setVisible(false);
            favoriteButton.setVisible(true);
        }
    }

    boolean isActive() {
        return active;
    }

    void setActive(boolean active) {
        this.active = active;
    }

    private void initKeyBindings() {
        Action moveBackwardAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (active && !DoubleActionTool.isDoubleAction(e)) {
                    moveBackward();
                }
            }
        };
        Action moveForwardAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (active && !DoubleActionTool.isDoubleAction(e)) {
                    moveForward();
                }
            }
        };
        Action volumeUpAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (active && !DoubleActionTool.isDoubleAction(e)) {
                    volumeUp();
                }
            }
        };
        Action volumeDownAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (active && !DoubleActionTool.isDoubleAction(e)) {
                    volumeDown();
                }
            }
        };
        Action pauseAction = new AbstractAction() {
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
            public void actionPerformed(ActionEvent e) {
                if (active && !DoubleActionTool.isDoubleAction(e)) {
                    stopSong();
                }
            }
        };
        Action muteAction = new AbstractAction() {
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

    private void addAdditionalPanels() {
        buttonPanel.add(favoriteButton);
        buttonPanel.add(unfavoriteButton);
        volumePanel.add(similarSongsButton);
    }

    private void initAdditional() {
        setFavorite(false);
    }
}
