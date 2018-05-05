package org.ql.audioeditor.common.util;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

/**
 * Utility class for the GUI.
 */
public final class ViewUtils {
    /**
     * Private constructor. May not be called.
     */
    private ViewUtils() {
        throw new AssertionError();
    }

    /**
     * Shows a message dialog with parent set to null.
     *
     * @param message Message
     */
    public static void showDialog(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Shows a message via the label specified. The message disappears after
     * some time.
     *
     * @param infoLabel Label
     * @param message   Message to show
     * @param delay     Delay in milliseconds
     */
    public static void showInfo(JLabel infoLabel, String message, int delay) {
        infoLabel.setText(message);
        Timer t = new Timer(delay, e -> infoLabel.setText(null));
        t.setRepeats(false);
        t.start();
    }

    /**
     * Applies gradient fill to a component.
     *
     * @param g      Graphics
     * @param color1 First color
     * @param color2 Second color
     * @param w      Width
     * @param h      Height
     */
    public static void fillColor(Graphics g, Color color1, Color color2, int w,
        int h) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }

    /**
     * Resizes an image icon.
     *
     * @param ii Image icon
     * @param d  Dimension
     * @return New image icon
     */
    public static ImageIcon resizeImageIcon(ImageIcon ii, Dimension d) {
        Image img = ii.getImage();
        Image newimg =
            img.getScaledInstance((int) d.getWidth(), (int) d.getHeight(),
                Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }

    /**
     * Resizes an image.
     *
     * @param i Image
     * @param d Dimension
     * @return New image
     */
    public static Image resizeImage(Image i, Dimension d) {
        return i.getScaledInstance((int) d.getWidth(), (int) d.getHeight(),
            Image.SCALE_SMOOTH);
    }
}
