package org.ql.audioeditor.view.core.textfield;

import javax.swing.*;
import java.awt.*;

public class TextField extends JTextField {
    public TextField(String text, Dimension d, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setPreferredSize(d);
    }
}
