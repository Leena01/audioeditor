package view.decorated;

import javax.swing.*;
import java.awt.*;

import static util.Utils.fillColor;

public class Panel extends JPanel {

    private JLabel titleLabel;
    private JPanel titlePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            fillColor(g, Color.GRAY, Color.BLACK, getWidth(), getHeight());
        }
    };

    public Panel() {
        super();
        setLayout(new BorderLayout());
        titleLabel = new JLabel("Audio Editor");
        titleLabel.setFont(new Font("Alex Brush", Font.ITALIC, 60));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        titlePanel.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        fillColor(g, Color.DARK_GRAY, Color.BLACK, getWidth(), getHeight());
    }


}
