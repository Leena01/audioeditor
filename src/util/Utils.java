package util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class Utils {
    public static void showDialog(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public static void fillColor(Graphics g, Color color1, Color color2, int w, int h) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }

    public static String formatDuration(long duration) {
        long absSeconds = Math.abs(duration / 1000);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return duration < 0 ? "-" + positive : positive;
    }

    public static ImageIcon resizeImageIcon(ImageIcon ii, Dimension d) {
        Image img = ii.getImage() ;
        Image newimg = img.getScaledInstance((int)d.getWidth(), (int)d.getHeight(), java.awt.Image.SCALE_SMOOTH) ;
        return new ImageIcon(newimg);
    }
}
