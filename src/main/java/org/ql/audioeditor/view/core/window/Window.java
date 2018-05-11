package org.ql.audioeditor.view.core.window;

import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.core.button.TransparentButton;
import org.ql.audioeditor.view.core.listener.EmptyMouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
import static org.ql.audioeditor.common.util.ViewUtils.resizeImageIcon;
import static org.ql.audioeditor.view.enums.ActionName.MAXIMIZE;
import static org.ql.audioeditor.view.param.Constants.WIN_MIN_SIZE;
import static org.ql.audioeditor.view.param.Constants.WIN_MIN_SIZE_HIDDEN;

/**
 * General window (frame).
 */
public abstract class Window extends JFrame {
    private static final String LOOK_AND_FEEL =
        "com.jtattoo.plaf.hifi.HiFiLookAndFeel";
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
    private static final int TITLE_HEIGHT = 36;
    private static Point compCoordinates = null;
    private final HorizontalBar titleBar;
    private final JPanel menuPanel;
    private final JButton hideButton;
    private final JButton minimizeButton;
    private final JButton maximizeButton;
    private final JButton exitButton;
    private final MouseMotionListener dragListener = new MouseMotionListener() {
        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (getExtendedState() == NORMAL) {
                Point currCoordinates = e.getLocationOnScreen();
                setLocation(currCoordinates.x - compCoordinates.x,
                    currCoordinates.y - compCoordinates.y);
            }
        }
    };
    protected boolean isNormal;
    private final MouseListener maximizeMouseListener
        = new EmptyMouseListener() {
        @Override
        public void mouseReleased(MouseEvent me) {
            compCoordinates = null;
        }

        @Override
        public void mousePressed(MouseEvent me) {
            compCoordinates = me.getPoint();
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

    /**
     * Constructor.
     */
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

        menuPanel.setOpaque(false);
        menuPanel.add(hideButton);
        menuPanel.add(minimizeButton);
        menuPanel.add(maximizeButton);
        menuPanel.add(exitButton);
        menuPanel.setBorder(MENU_PANEL_BORDER);
        titleBar.add(menuPanel, BorderLayout.NORTH);
        titleBar.setHeight(TITLE_HEIGHT);
        add(titleBar, BorderLayout.NORTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setVisible(true);
        initKeyBindings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Graphics g) {
        Dimension d = getSize();
        Dimension m = getMinimumSize();
        if (d.width < m.width || d.height < m.height) {
            pack();
        }
        super.paint(g);
    }

    /**
     * Changes the size of the window.
     */
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
        } else {
            hideButton.setIcon(DOWNWARD_ICON);
            setMinimumSize(WIN_MIN_SIZE_HIDDEN);
            setSize(WIN_MIN_SIZE_HIDDEN);
        }
    }

    /**
     * Minimizes the window.
     */
    private void minimize() {
        setState(ICONIFIED);
    }

    /**
     * Sets the window to full screen.
     */
    private void maximize() {
        if (getExtendedState() == NORMAL) {
            maximizeImage(true);
            hideImage(false);
            setExtendedState(MAXIMIZED_BOTH);
            maximizeButton.setIcon(NORMALIZE_ICON);
        } else {
            maximizeImage(false);
            hideImage(!isNormal);
            setExtendedState(NORMAL);
            maximizeButton.setIcon(MAXIMIZE_ICON);
        }
    }

    /**
     * Closes the window.
     */
    private void close() {
        System.exit(0);
    }

    /**
     * Initializes key bindings.
     */
    private void initKeyBindings() {
        Action maximizeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                maximize();
            }
        };
        KeyStroke f11KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0);
        inputMap.put(f11KeyStroke, MAXIMIZE);
        actionMap.put(MAXIMIZE, maximizeAction);
    }

    /**
     * Hides/shows the core image according to the given boolean.
     *
     * @param isHidden Indicates whether the core image should be hidden
     */
    protected abstract void hideImage(boolean isHidden);

    /**
     * Maximizes/normalizes the core image according to the given boolean.
     *
     * @param isMaximized Indicates whether the core image should be maximized
     */
    protected abstract void maximizeImage(boolean isMaximized);
}
