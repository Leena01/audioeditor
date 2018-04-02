package view.core.window;

import view.core.button.TransparentButton;
import view.core.label.Label;
import view.core.bar.HorizontalBar;

import static view.util.Helper.resizeImageIcon;
import static view.util.Constants.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public abstract class Window extends JFrame {
    private static Point COMP_COORDS = null;
    private static final Dimension IMAGE_SIZE = new Dimension(20, 20);
    private static final Dimension BUTTON_SIZE = new Dimension(30, 20);
    private static final ImageIcon UP_ICON = resizeImageIcon(new ImageIcon(UP_ICON_NAME), IMAGE_SIZE);
    private static final ImageIcon DOWN_ICON = resizeImageIcon(new ImageIcon(DOWN_ICON_NAME), IMAGE_SIZE);
    private static final ImageIcon MINIMIZE_ICON = resizeImageIcon(new ImageIcon(MINIMIZE_ICON_NAME), IMAGE_SIZE);
    private static final ImageIcon MAXIMIZE_ICON = resizeImageIcon(new ImageIcon(MAXIMIZE_ICON_NAME), IMAGE_SIZE);
    private static final ImageIcon NORMALIZE_ICON = resizeImageIcon(new ImageIcon(NORMALIZE_ICON_NAME), IMAGE_SIZE);
    private static final ImageIcon CLOSE_ICON = resizeImageIcon(new ImageIcon(CLOSE_ICON_NAME), IMAGE_SIZE);

    protected boolean isNormal;
    private HorizontalBar titleBar;
    private JLabel titleLabel;
    private JPanel titlePanel;
    private JPanel menuPanel;
    private JButton hideButton;
    private JButton minimizeButton;
    private JButton maximizeButton;
    private JButton exitButton;
    private MouseListener maximizeMouseListener = new MouseListener() {
        @Override
        public void mouseReleased(MouseEvent me) {
            COMP_COORDS = null;
        }

        @Override
        public void mousePressed(MouseEvent me) {
            COMP_COORDS = me.getPoint();
        }

        @Override
        public void mouseExited(MouseEvent me) {
            // Do nothing.
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            // Do nothing.
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            if (me.getClickCount() == 2 && !me.isConsumed()) {
                me.consume();
                maximize();
            }
        }
    };
    private MouseMotionListener dragListener = new MouseMotionListener() {
        @Override
        public void mouseMoved(MouseEvent e) {
            // Do nothing.
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(getExtendedState() == NORMAL) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - COMP_COORDS.x, currCoords.y - COMP_COORDS.y);
            }
        }
    };

    public Window() {
        super();
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
        } catch(Exception ignored) { }

        isNormal = true;
        titleBar = new HorizontalBar(maximizeMouseListener, dragListener);
        titlePanel = new JPanel(new GridBagLayout());
        titleLabel = new Label("Audio Editor");
        titleLabel.setFont(new Font("Alex Brush", Font.ITALIC, 36));

        FlowLayout menuPanelLayout = new FlowLayout(FlowLayout.RIGHT);
        menuPanel = new JPanel(menuPanelLayout);
        menuPanelLayout.setHgap(0);
        menuPanelLayout.setVgap(0);

        hideButton = new TransparentButton(UP_ICON, BUTTON_SIZE, ae -> changeSize());
        minimizeButton = new TransparentButton(MINIMIZE_ICON, BUTTON_SIZE, ae -> minimize());
        maximizeButton = new TransparentButton(MAXIMIZE_ICON, BUTTON_SIZE, ae -> maximize());
        exitButton = new TransparentButton(CLOSE_ICON, BUTTON_SIZE, e -> close());

        titlePanel.setOpaque(false);
        menuPanel.setOpaque(false);
        menuPanel.add(hideButton);
        menuPanel.add(minimizeButton);
        menuPanel.add(maximizeButton);
        menuPanel.add(exitButton);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titlePanel.add(titleLabel);
        titleBar.add(menuPanel, BorderLayout.NORTH);
        titleBar.add(titlePanel, BorderLayout.SOUTH);
        add(titleBar, BorderLayout.NORTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setVisible(true);
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
            hideButton.setIcon(UP_ICON);
            setMinimumSize(WIN_MIN_SIZE);
            setSize(WIN_MIN_SIZE);
        }
        else {
            hideButton.setIcon(DOWN_ICON);
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

    protected abstract void hideImage(boolean isHidden);
    protected abstract void maximizeImage(boolean isMaximized);
}