package org.ql.audioeditor.view.core.dialog;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.event.WindowAdapter;

/**
 * Popup dialog.
 */
public class PopupDialog extends JDialog {
    /**
     * Constructor.
     *
     * @param owner Owner
     * @param title Title
     * @param panel Panel
     */
    public PopupDialog(Frame owner, String title, JPanel panel) {
        super(owner, title, true);
        getContentPane().add(panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        pack();
    }

    /**
     * Constructor.
     *
     * @param owner         Owner
     * @param title         Title
     * @param panel         Panel
     * @param windowAdapter Window adapter
     */
    public PopupDialog(Frame owner, String title, JPanel panel, WindowAdapter
        windowAdapter) {
        this(owner, title, panel);
        addWindowListener(windowAdapter);
    }
}
