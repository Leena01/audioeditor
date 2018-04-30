package org.ql.audioeditor.view.core.window;

import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.core.button.TransparentButton;
import org.ql.audioeditor.view.core.label.Label;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
import static org.ql.audioeditor.common.util.Helper.resizeImageIcon;
import static org.ql.audioeditor.view.param.Constants.WIN_MIN_SIZE;
import static org.ql.audioeditor.view.param.Constants.WIN_MIN_SIZE_HIDDEN;

public abstract class Window extends JFrame {
    private static final int FONT_SIZE = 36;
    private static final String LOOK_AND_FEEL =
        "com.jtattoo.plaf.hifi.HiFiLookAndFeel";
    private static final String TITLE = "Audio Editor";
    private static final String FONT = "Alex Brush";
    private static final String MAXIMIZE = "Maximize";
    private static final Dimension IMAGE_SIZE = new Dimension(20, 20);
    private static final Dimension BUTTON_SIZE = new Dimension(30, 20);
    private static final ImageIcon UPWARD_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getUpwardIcon()),
            IMAGE_SIZE);
    private static final ImageIcon DOWNWARD_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getDownwardIcon()),
            IMAGE_SIZE);
    private static final ImageIcon MINIMIZE_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getMinimizeIcon()),
            IMAGE_SIZE);
    private static final ImageIcon MAXIMIZE_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getMaximizeIcon()),
            IMAGE_SIZE);
    private static final ImageIcon NORMALIZE_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getNormalizeIcon()),
            IMAGE_SIZE);
    private static final ImageIcon CLOSE_ICON =
        resizeImageIcon(new ImageIcon(ImageLoader.getCloseIcon()),
            IMAGE_SIZE);
    private static final Border MENU_PANEL_BORDER =
        BorderFactory.createEmptyBorder(5, 5, 5, 5);
    private static Point COMP_COORDINATES = null;
    private final HorizontalBar titleBar;
    private final JLabel titleLabel;
    private final JPanel titlePanel;
    private final JPanel menuPanel;
    private final JButton hideButton;
    private final JButton minimizeButton;
    private final JButton maximizeButton;
    private final JButton exitButton;
    private final MouseMotionListener dragListener = new MouseMotionListener() {
        @Override
        public void mouseMoved(MouseEvent e) {
            // Do nothing.
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (getExtendedState() == NORMAL) {
                Point currCoordinates = e.getLocationOnScreen();
                setLocation(currCoordinates.x - COMP_COORDINATES.x,
                    currCoordinates.y - COMP_COORDINATES.y);
            }
        }
    };
    protected boolean isNormal;
    private final MouseListener maximizeMouseListener = new MouseListener() {
        @Override
        public void mouseReleased(MouseEvent me) {
            COMP_COORDINATES = null;
        }

        @Override
        public void mousePressed(MouseEvent me) {
            COMP_COORDINATES = me.getPoint();
        }

        @Override
        public void mouseExited(MouseEvent me) {
        }

        @Override
        public void mouseEntered(MouseEvent me) {
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            if (me.getClickCount() == 2 && !me.isConsumed()) {
                me.consume();
                maximize();
            }
        }
    };
    protected InputMap inputMap;
    protected ActionMap actionMap;

    public Window() {
        super();
        try {
            UIManager.setLookAndFeel(LOOK_AND_FEEL);
        } catch (Exception ignored) {
        }

        isNormal = true;
        inputMap = rootPane.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        actionMap = rootPane.getActionMap();
        titleBar = new HorizontalBar(maximizeMouseListener, dragListener);
        titlePanel = new JPanel(new GridBagLayout());
        titleLabel = new Label(TITLE);
        titleLabel.setFont(new Font(FONT, Font.ITALIC, FONT_SIZE));

        FlowLayout menuPanelLayout = new FlowLayout(FlowLayout.RIGHT);
        menuPanel = new JPanel(menuPanelLayout);
        menuPanelLayout.setHgap(0);
        menuPanelLayout.setVgap(0);

        hideButton =
            new TransparentButton(UPWARD_ICON, BUTTON_SIZE, ae -> changeSize());
        minimizeButton =
            new TransparentButton(MINIMIZE_ICON, BUTTON_SIZE, ae -> minimize());
        maximizeButton =
            new TransparentButton(MAXIMIZE_ICON, BUTTON_SIZE, ae -> maximize());
        exitButton =
            new TransparentButton(CLOSE_ICON, BUTTON_SIZE, e -> close());

        titlePanel.setOpaque(false);
        menuPanel.setOpaque(false);
        menuPanel.add(hideButton);
        menuPanel.add(minimizeButton);
        menuPanel.add(maximizeButton);
        menuPanel.add(exitButton);
        menuPanel.setBorder(MENU_PANEL_BORDER);
        titlePanel.add(titleLabel);
        titleBar.add(menuPanel, BorderLayout.NORTH);
        titleBar.add(titlePanel, BorderLayout.SOUTH);
        add(titleBar, BorderLayout.NORTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setVisible(true);
        initKeyBindings();
    }

    private void changeSize() {
        if (getExtendedState() == MAXIMIZED_BOTH) {
            maximizeImage(false);
            setExtendedState(NORMAL);
            maximizeButton.setIcon(MAXIMIZE_ICON);
        }
        hideImage(isNormal);
        isNormal = !isNormal;
        if (isNormal) {
            hideButton.setIcon(UPWARD_ICON);
            setMinimumSize(WIN_MIN_SIZE);
            setSize(WIN_MIN_SIZE);
        }
        else {
            hideButton.setIcon(DOWNWARD_ICON);
            setMinimumSize(WIN_MIN_SIZE_HIDDEN);
            setSize(WIN_MIN_SIZE_HIDDEN);
        }
    }

    private void minimize() {
        setState(ICONIFIED);
    }

    private void maximize() {
        if (getExtendedState() == NORMAL) {
            maximizeImage(true);
            hideImage(false);
            setExtendedState(MAXIMIZED_BOTH);
            maximizeButton.setIcon(NORMALIZE_ICON);
        }
        else {
            maximizeImage(false);
            hideImage(!isNormal);
            setExtendedState(NORMAL);
            maximizeButton.setIcon(MAXIMIZE_ICON);
        }
    }

    private void close() {
        System.exit(0);
    }

    private void initKeyBindings() {
        Action maximizeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                maximize();
            }
        };
        KeyStroke fKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F, 0);
        inputMap.put(fKeyStroke, MAXIMIZE);
        actionMap.put(MAXIMIZE, maximizeAction);
    }

    protected abstract void hideImage(boolean isHidden);

    protected abstract void maximizeImage(boolean isMaximized);
}