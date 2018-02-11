package view.decorated;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;

import static util.Utils.fillColor;

/**
 *
 *@author Vincent
 */
public class Window extends JFrame {

    private static Point compCoords;
    private JLabel titleLabel;
    private JPanel titlePanel;
    private JPanel menuPanel;
    private JButton minimizeButton;
    private JButton exitButton;
    private JPanel titleBar = new JPanel(new BorderLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            fillColor(g, Color.GRAY, Color.BLACK, getWidth(), getHeight());
        }
    };

    public Window() {
        super();
        titlePanel = new JPanel(new GridBagLayout());
        titleLabel = new JLabel("Audio Editor");
        titleLabel.setFont(new Font("Alex Brush", Font.ITALIC, 60));
        titleLabel.setForeground(Color.WHITE);

        menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        minimizeButton = new JButton("-");
        exitButton = new JButton("X");
        exitButton.addActionListener(e -> System.exit(0));
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));
        compCoords = null;
        titleBar.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                compCoords = null;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                compCoords = e.getPoint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Do nothing.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Do nothing.
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Do nothing.
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Do nothing.
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - compCoords.x, currCoords.y - compCoords.y);
            }
        });

        titlePanel.setOpaque(false);
        menuPanel.setOpaque(false);
        menuPanel.add(minimizeButton);
        menuPanel.add(exitButton);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titlePanel.add(titleLabel);
        titleBar.add(menuPanel, BorderLayout.NORTH);
        titleBar.add(titlePanel, BorderLayout.SOUTH);
        add(titleBar, BorderLayout.NORTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JFrame.setDefaultLookAndFeelDecorated(true);
        setUndecorated(true);
        setVisible(true);
    }
}