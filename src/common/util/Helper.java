package common.util;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public final class Helper {
    public static void showDialog(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public static void showInfo(JLabel infoLabel, String message, int delay) {
        infoLabel.setText(message);
        Timer t = new Timer(delay, e -> infoLabel.setText(null));
        t.setRepeats(false);
        t.start();
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

    public static long framesToMillis(double frame, double freq){
        return (long)(frame / freq) * 1000;
    }

    public static ImageIcon resizeImageIcon(ImageIcon ii, Dimension d) {
        Image img = ii.getImage() ;
        Image newimg = img.getScaledInstance((int)d.getWidth(), (int)d.getHeight(), java.awt.Image.SCALE_SMOOTH) ;
        return new ImageIcon(newimg);
    }

    public static Image resizeImage(Image ii, Dimension d) {
        return ii.getScaledInstance((int)d.getWidth(), (int)d.getHeight(), java.awt.Image.SCALE_SMOOTH);
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
        if (parent != null)
            return parent.getAbsolutePath();
        else
            return path;
    }

    public static String getAbsolutePath(URL url) {
        File f;
        try {
            f = new File(url.toURI());
        } catch(URISyntaxException e) {
            f = new File(url.getPath());
        }
        return f.getAbsolutePath();
    }
}
