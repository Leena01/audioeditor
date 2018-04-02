package view.core.dialog;

import javax.swing.*;
import java.awt.*;

public class PopupDialog extends JDialog {
    public PopupDialog(Frame owner, String title, JPanel panel) {
        super(owner, title, true);
        getContentPane().add(panel);
        setAlwaysOnTop(true);
        pack();
    }
}
