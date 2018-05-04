package org.ql.audioeditor.view.core.dialog;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.event.WindowAdapter;

/**
 * Popup dialog.
 */
public class PopupDialog extends JDialog {
    public PopupDialog(Frame owner, String title, JPanel panel) {
        super(owner, title, true);
        getContentPane().add(panel);
        setLocationRelativeTo(null);
        pack();
    }

    public PopupDialog(Frame owner, String title, JPanel panel, WindowAdapter
        windowAdapter) {
        this(owner, title, panel);
        addWindowListener(windowAdapter);
    }
}
