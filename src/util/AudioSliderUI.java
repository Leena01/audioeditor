package util;

import view.elements.playerpanel.TrackSlider;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.MouseEvent;

import static javax.swing.border.BevelBorder.LOWERED;

public class AudioSliderUI extends BasicSliderUI {

    public AudioSliderUI(TrackSlider b) {
        super(b);
        b.setPreferredSize(new Dimension(500, 65));
        b.setPaintTicks(false);
        b.setPaintLabels(false);
        b.setPaintLabels(false);
        b.setPaintTrack(true);
        b.setOpaque(false);
        b.setBorder(new CompoundBorder(
                BorderFactory.createEmptyBorder(0, 10, 10, 10),
                BorderFactory.createBevelBorder(LOWERED, Color.GRAY, Color.DARK_GRAY)));
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g, c);
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(3, focusRect.height);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Image img = ((TrackSlider)slider).getImage();
        g2d.drawImage(img, focusRect.x,focusRect.y, focusRect.width, focusRect.height, slider);

    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int w = thumbRect.width;
        int h = thumbRect.height;
        // int newStarty = thumbRect.height / 2 - h / 2;
        g2d.translate(thumbRect.x, thumbRect.y);
        GradientPaint bgPaint = new GradientPaint(0, 0, new Color(0,0,169),
                w, 0, new Color(169,169,169));
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
                                    "orientation must be one of: VERTICAL, HORIZONTAL");
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