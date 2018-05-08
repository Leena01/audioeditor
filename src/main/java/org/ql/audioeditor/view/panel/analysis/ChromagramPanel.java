package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.panel.BasicFormPanel;
import org.ql.audioeditor.view.core.textfield.FormattedTextField;
import org.ql.audioeditor.view.param.Constants;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import static org.ql.audioeditor.view.param.Constants.BACK_TO_MAIN_MENU_TEXT;

/**
 * Panel for showing chroma feature.
 */
public class ChromagramPanel extends BasicFormPanel {
    private static final GridLayout FORM_PANEL_LAYOUT = new GridLayout(6, 2,
        10, 10);
    private static final String[] WINDOW_NAMES = SongPropertiesLoader
        .getWindowNames();

    protected JButton backOptionButton;
    protected JLabel windowSizeLabel;
    protected JFormattedTextField windowSizeTextField;
    protected JLabel hopSizeLabel;
    protected JFormattedTextField hopSizeTextField;
    protected JLabel nfftLabel;
    protected JFormattedTextField nfftTextField;
    protected JLabel windowLabel;
    protected JComboBox windowComboBox;

    /**
     * Constructor.
     */
    public ChromagramPanel() {
        super();
        formPanel.setLayout(FORM_PANEL_LAYOUT);

        windowSizeLabel = new Label("Window size:");
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(Constants.TEXT_FIELD_DIGIT_SIZE_MIN);
        nf.setMaximumIntegerDigits(Constants.TEXT_FIELD_DIGIT_SIZE_MAX);
        nf.setGroupingUsed(false);
        windowSizeTextField = new FormattedTextField(nf);

        NumberFormat nf2 = NumberFormat.getIntegerInstance();
        nf2.setMinimumIntegerDigits(Constants.TEXT_FIELD_DIGIT_SIZE_MIN);
        nf2.setMaximumIntegerDigits(Constants.TEXT_FIELD_DIGIT_SIZE_MAX - 1);
        nf2.setGroupingUsed(false);
        hopSizeLabel = new Label("Hop size:");
        hopSizeTextField = new FormattedTextField(nf2);

        nfftLabel = new Label("Number of FFT bins:");
        nfftTextField = new FormattedTextField(nf);

        windowLabel = new Label("Window type:");
        windowComboBox = new JComboBox<>(WINDOW_NAMES);
    }

    /**
     * Constructor.
     *
     * @param s DoneButton listener
     * @param b BackOptionButton listener
     */
    public ChromagramPanel(ActionListener s, ActionListener b) {
        this();
        doneButton = new Button("Done", s);
        backOptionButton = new Button(BACK_TO_MAIN_MENU_TEXT, b);
        setStyle();
        addPanels();
    }

    /**
     * Returns the window size set by the user.
     *
     * @return Window size
     */
    public String getWindowSize() {
        return windowSizeTextField.getText();
    }

    /**
     * Returns the hop size set by the user.
     *
     * @return Hop size
     */
    public String getHopSize() {
        return hopSizeTextField.getText();
    }

    /**
     * Returns the number of FFT points set by the user.
     *
     * @return Number of FFT points
     */
    public String getNfft() {
        return nfftTextField.getText();
    }

    /**
     * Returns the type of window function chosen by the user.
     *
     * @return Window type
     */
    public String getWindow() {
        return (String) windowComboBox.getSelectedItem();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setStyle() {
        setBackground(Color.BLACK);
        formPanel.setOpaque(false);
        outerFormPanel.setOpaque(false);
        imagePanel.setOpaque(false);
        bodyPanel.setOpaque(false);
        mainPanel.setOpaque(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPanels() {
        imagePanel.add(imageLabel);
        formPanel.add(windowSizeLabel);
        formPanel.add(windowSizeTextField);
        formPanel.add(hopSizeLabel);
        formPanel.add(hopSizeTextField);
        formPanel.add(nfftLabel);
        formPanel.add(nfftTextField);
        formPanel.add(windowLabel);
        formPanel.add(windowComboBox);
        formPanel.add(doneButton);
        formPanel.add(clearFieldsButton);
        formPanel.add(backOptionButton);
        outerFormPanel.add(formPanel);
        mainPanel.add(outerFormPanel);
        mainPanel.add(imagePanel);
        bodyPanel.add(mainPanel);
        add(bodyPanel);
    }

    /**
     * Clears text fields.
     */
    @Override
    protected void clearFields() {
        windowSizeTextField.setValue(null);
        hopSizeTextField.setValue(null);
        nfftTextField.setValue(null);
    }
}
