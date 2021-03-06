package org.ql.audioeditor.view.core.slider.ui;

import org.ql.audioeditor.view.core.slider.TrackSlider;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import static org.ql.audioeditor.view.param.Constants.AUDIO_SLIDER_SIZE;

/**
 * AudioSlider UI.
 */
public class AudioSliderUI extends BasicSliderUI {
    private static final int KNOB_WIDTH = 3;

    /**
     * Constructor.
     *
     * @param b Track slider
     */
    public AudioSliderUI(TrackSlider b) {
        super(b);
        b.setPreferredSize(AUDIO_SLIDER_SIZE);
        b.setPaintTicks(false);
        b.setPaintLabels(false);
        b.setPaintLabels(false);
        b.setPaintTrack(true);
        b.setOpaque(false);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g, c);
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(KNOB_WIDTH, focusRect.height);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Image img = ((TrackSlider) slider).getImage();
        g2d.drawImage(img, focusRect.x, focusRect.y, focusRect.width,
            focusRect.height, slider);
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int w = thumbRect.width;
        int h = thumbRect.height;
        g2d.translate(thumbRect.x, thumbRect.y);
        GradientPaint bgPaint = new GradientPaint(0, 0, Color.DARK_GRAY,
            w, 0, Color.LIGHT_GRAY);
        g2d.setPaint(bgPaint);
        g2d.fillRect(0, 0, w, h);
    }

    @Override
    public void paintFocus(Graphics g) {
        // Do nothing.
    }

    @Override
    protected TrackListener createTrackListener(JSlider slider) {
        return new TrackListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (UIManager.getBoolean("TrackSlider.onlyLeftMouseButtonDrag")
                    && SwingUtilities.isLeftMouseButton(e)) {
                    JSlider slider = (JSlider) e.getComponent();
                    switch (slider.getOrientation()) {
                        case SwingConstants.VERTICAL:
                            slider.setValue(valueForYPosition(e.getY()));
                            break;
                        case SwingConstants.HORIZONTAL:
                            slider.setValue(valueForXPosition(e.getX()));
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "orientation must be one of: VERTICAL, "
                                    + "HORIZONTAL");
                    }
                    super.mousePressed(e); // isDragging = true;
                    super.mouseDragged(e);
                } else {
                    super.mousePressed(e);
                }
            }

            @Override
            public boolean shouldScroll(int direction) {
                return false;
            }
        };
    }
}
