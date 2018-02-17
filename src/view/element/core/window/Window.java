package view.element.core.window;

import view.element.core.bar.HorizontalBar;
import view.element.core.button.Button;
import view.element.core.label.Label;

import static util.Utils.resizeImageIcon;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Window extends JFrame {
    private static Point COMP_COORDS = null;
    private static final Dimension IMAGE_SIZE = new Dimension(20, 20);
    private static final Dimension BUTTON_SIZE = new Dimension(30, 20);
    private static final ImageIcon MINIMIZE_ICON = resizeImageIcon(new ImageIcon("resources/images/minimize.png"), IMAGE_SIZE);
    private static final ImageIcon MAXIMIZE_ICON = resizeImageIcon(new ImageIcon("resources/images/maximize.png"), IMAGE_SIZE);
    private static final ImageIcon NORMALIZE_ICON = resizeImageIcon(new ImageIcon("resources/images/normalize.png"), IMAGE_SIZE);
    private static final ImageIcon CLOSE_ICON = resizeImageIcon(new ImageIcon("resources/images/close.png"), IMAGE_SIZE);

    private HorizontalBar titleBar;
    private JLabel titleLabel;
    private JPanel titlePanel;
    private JPanel menuPanel;
    private view.element.core.button.Button minimizeButton;
    private view.element.core.button.Button maximizeButton;
    private view.element.core.button.Button exitButton;
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

        titleBar = new HorizontalBar(maximizeMouseListener, dragListener);
        titlePanel = new JPanel(new GridBagLayout());
        titleLabel = new Label("Audio Editor");
        titleLabel.setFont(new Font("Alex Brush", Font.ITALIC, 36));

        FlowLayout menuPanelLayout = new FlowLayout(FlowLayout.RIGHT);
        menuPanel = new JPanel(menuPanelLayout);
        menuPanelLayout.setHgap(0);
        menuPanelLayout.setVgap(0);

        minimizeButton = new view.element.core.button.Button(MINIMIZE_ICON, BUTTON_SIZE);
        minimizeButton.addMouseListener();
        minimizeButton.addActionListener(ae -> setExtendedState(JFrame.ICONIFIED));
        maximizeButton = new view.element.core.button.Button(MAXIMIZE_ICON, BUTTON_SIZE);
        maximizeButton.addMouseListener();
        maximizeButton.addActionListener(ae -> maximize());
        exitButton = new Button(CLOSE_ICON, BUTTON_SIZE);
        exitButton.addMouseListener();
        exitButton.addActionListener(e -> System.exit(0));

        titlePanel.setOpaque(false);
        menuPanel.setOpaque(false);
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

    private void maximize() {
        if (getExtendedState() == NORMAL) {
            setExtendedState(MAXIMIZED_BOTH);
            maximizeButton.setIcon(NORMALIZE_ICON);
        }
        else {
            setExtendedState(NORMAL);
            maximizeButton.setIcon(MAXIMIZE_ICON);
        }
    }
}