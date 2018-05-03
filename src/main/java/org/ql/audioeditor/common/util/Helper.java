package org.ql.audioeditor.common.util;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
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
import java.io.File;

/**
 * General utility class.
 */
public final class Helper {
    public static final int MILLIS_SECONDS_CONVERSION = 1000;
    private static final int HOUR_CONVERSION = 3600;
    private static final int MINUTE_CONVERSION = 60;
    private static final String PATH = new File("").getAbsolutePath();

    private Helper() {
        throw new AssertionError();
    }

    public static String getPath() {
        return PATH;
    }

    public static void showDialog(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public static void showInfo(JLabel infoLabel, String message, int delay) {
        infoLabel.setText(message);
        Timer t = new Timer(delay, e -> infoLabel.setText(null));
        t.setRepeats(false);
        t.start();
    }

    public static void fillColor(Graphics g, Color color1, Color color2, int w,
        int h) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }

    public static String formatDuration(long duration) {
        long absSeconds = Math.abs(duration / MILLIS_SECONDS_CONVERSION);
        String positive = String.format(
            "%d:%02d:%02d",
            absSeconds / HOUR_CONVERSION,
            (absSeconds % HOUR_CONVERSION) / MINUTE_CONVERSION,
            absSeconds % MINUTE_CONVERSION);
        if (duration < 0) {
            return "-" + positive;
        }
        return positive;
    }

    public static int framesToMillis(double frame, double freq) {
        return (int) (frame / freq) * MILLIS_SECONDS_CONVERSION;
    }

    public static int secondsToFrames(int seconds, double freq) {
        return (int) (seconds * freq);
    }

    public static ImageIcon resizeImageIcon(ImageIcon ii, Dimension d) {
        Image img = ii.getImage();
        Image newimg =
            img.getScaledInstance((int) d.getWidth(), (int) d.getHeight(),
                java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }

    public static Image resizeImage(Image ii, Dimension d) {
        return ii.getScaledInstance((int) d.getWidth(), (int) d.getHeight(),
            java.awt.Image.SCALE_SMOOTH);
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDir(String path) {
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null) {
            return parent.getAbsolutePath();
        } else {
            return path;
        }
    }

    public static int convertToNumber(String numberString) {
        if (!numberString.equals("")) {
            return Integer.parseInt(numberString);
        }
        return 0;
    }
}
