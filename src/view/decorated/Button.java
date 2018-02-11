package view.decorated;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static util.Utils.fillColor;

public class Button extends JButton implements MouseListener {
    public Button() {
        super();
        setAttributes();
    }

    public Button(String text) {
        super(text);
        setAttributes();
    }

    public Button(String text, Dimension d) {
        this(text);
        setPreferredSize(d);
        setAttributes();
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("GGGGGG");
        if(e.getSource() == this)
            this.setForeground(Color.DARK_GRAY);
    }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) {
        if(e.getSource() == this)
            this.setBackground(Color.LIGHT_GRAY);
    }

    public void mouseExited(MouseEvent e) {
        setContentAreaFilled(false);
    }

    private void setAttributes() {
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        addMouseListener(this);
    }
}
