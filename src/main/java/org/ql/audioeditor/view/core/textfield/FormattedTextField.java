package org.ql.audioeditor.view.core.textfield;

import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;

/**
 * Formatted text field.
 */
public class FormattedTextField extends JFormattedTextField {
    /**
     * Constructor.
     *
     * @param nf Number formatter
     */
    public FormattedTextField(NumberFormatter nf) {
        super(nf);
        addFocusListener(new FormattedTextField.InnerFocusListener());

    }

    /**
     * Constructor.
     *
     * @param nf Number format
     */
    public FormattedTextField(NumberFormat nf) {
        super(nf);
        addFocusListener(new FormattedTextField.InnerFocusListener());
    }

    /**
     * Focus listener to place the cursor at the end of the string when
     * focused.
     */
    private final class InnerFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            FormattedTextField tf = FormattedTextField.this;
            int end = tf.getText().length();
            tf.setCaretPosition(end);
        }

        @Override
        public void focusLost(FocusEvent e) {
        }
    }
}
