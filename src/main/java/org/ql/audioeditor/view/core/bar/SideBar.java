package org.ql.audioeditor.view.core.bar;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import static org.ql.audioeditor.common.util.ViewUtils.fillColor;

/**
 * Sidebar.
 */
public class SideBar extends JPanel {
    private static final Border BORDER =
        BorderFactory.createMatteBorder(0, 0, 0, 3, Color.DARK_GRAY);

    /**
     * Constructor.
     */
    public SideBar() {
        FlowLayout layout = (FlowLayout) getLayout();
        layout.setVgap(0);
        layout.setHgap(0);
        setBorder(BORDER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        fillColor(g, Color.DARK_GRAY, Color.BLACK, getWidth(), getHeight());
    }
}
