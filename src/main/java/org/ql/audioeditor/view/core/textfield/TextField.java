package org.ql.audioeditor.view.core.textfield;

import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Text field.
 */
public class TextField extends JTextField {
    /**
     * Constructor.
     */
    public TextField() {
        super();
        addFocusListener(new InnerFocusListener());
    }

    /**
     * Constructor.
     *
     * @param text                Text
     * @param d                   Dimension
     * @param horizontalAlignment Horinzontal alignment
     */
    public TextField(String text, Dimension d, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setPreferredSize(d);
        addFocusListener(new InnerFocusListener());
    }

    /**
     * Constructor.
     *
     * @param text   Text
     * @param length Length
     */
    public TextField(String text, int length) {
        super(text, length);
        addFocusListener(new InnerFocusListener());
    }

    /**
     * Focus listener to place the cursor at the end of the string when
     * focused.
     */
    private final class InnerFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            TextField tf = TextField.this;
            int end = tf.getText().length();
            tf.setCaretPosition(end);
        }

        @Override
        public void focusLost(FocusEvent e) {
        }
    }
}
