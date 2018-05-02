package org.ql.audioeditor.view.core.dialog;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Frame;

/**
 * Popup dialog.
 */
public class PopupDialog extends JDialog {
    public PopupDialog(Frame owner, String title, JPanel panel) {
        super(owner, title, true);
        getContentPane().add(panel);
        setAlwaysOnTop(true);
        pack();
    }
}
