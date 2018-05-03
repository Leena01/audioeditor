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
import java.io.File;

/**
 * General utility class.
 */
public final class Helper {
    public static final int MILLIS_SECONDS_CONVERSION = 1000;
    private static final int HOUR_CONVERSION = 3600;
    private static final int MINUTE_CONVERSION = 60;
    private static final String PATH = new File("").getAbsolutePath();

    /**
     * Private constructor. May not be called.
     */
    private Helper() {
        throw new AssertionError();
    }

    /**
     * Returns the current path of the software.
     *
     * @return Path
     */
    public static String getPath() {
        return PATH;
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
     * Converts seconds to the following format: hh:mm:ss.
     *
     * @param duration Duration in seconds
     * @return Formatted string
     */
    public static String formatDuration(long duration) {
        long absSeconds = Math.abs(duration);
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

    /**
     * Converts frames to seconds according to the sampling frequency given.
     *
     * @param frame Number of frames
     * @param freq  Sampling frequency
     * @return Seconds
     */
    public static int framesToSeconds(double frame, double freq) {
        return (int) (frame / freq);
    }

    /**
     * Converts seconds to frames according to the sampling frequency given.
     *
     * @param seconds Seconds
     * @param freq    Sampling frequency
     * @return Number of frames
     */
    public static int secondsToFrames(int seconds, double freq) {
        return (int) (seconds * freq);
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
                java.awt.Image.SCALE_SMOOTH);
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
            java.awt.Image.SCALE_SMOOTH);
    }

    /**
     * Gets the extension of a file.
     *
     * @param file File
     * @return Extension
     */
    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Gets the absolute path of a file's directory.
     *
     * @param path File path
     * @return Absolute path of directory
     */
    public static String getDir(String path) {
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null) {
            return parent.getAbsolutePath();
        } else {
            return path;
        }
    }

    /**
     * Converts a string to a number.
     *
     * @param numberString String to convert
     * @return Resulting number
     */
    public static int convertToNumber(String numberString) {
        if (!numberString.equals("")) {
            return Integer.parseInt(numberString);
        }
        return 0;
    }
}
