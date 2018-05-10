package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.panel.BasicFormPanel;
import org.ql.audioeditor.view.core.textfield.FormattedTextField;
import org.ql.audioeditor.view.enums.NoteValue;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.text.NumberFormatter;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * Panel for detection.
 */
public abstract class DetPanel extends BasicFormPanel {
    private static final GridLayout FORM_PANEL_LAYOUT = new GridLayout(5, 2,
        10, 10);
    private static final int BPM_MIN = SongPropertiesLoader.getBpmMin();
    private static final int BPM_MAX = SongPropertiesLoader.getBpmMax();
    private static final String BPM_LIMIT = String.format(
        "The minimum BPM is %d and the maximum is %d.", BPM_MIN, BPM_MAX);
    private static final Object[] NOTE_VALUES = NoteValue.getValues().toArray();

    private final JLabel bpmLabel;
    private final JFormattedTextField bpmTextField;
    private final JLabel smoothnessLabel;
    private final JFormattedTextField smoothnessTextField;
    private final JLabel baseLabel;
    private final JComboBox baseComboBox;
    private final JLabel smallestLabel;
    private final JComboBox smallestComboBox;

    /**
     * Constructor.
     *
     * @param d DoneButton listener
     */
    public DetPanel(ActionListener d) {
        super();
        formPanel.setLayout(FORM_PANEL_LAYOUT);
        doneButton = new Button("Done", d);

        bpmLabel = new Label("BPM:");
        NumberFormatter nf = new NumberFormatter();
        nf.setMinimum(BPM_MIN);
        nf.setMaximum(BPM_MAX);
        bpmTextField = new FormattedTextField(nf);
        bpmTextField.setToolTipText(BPM_LIMIT);

        smoothnessLabel = new Label("Smoothness:");
        NumberFormat nf2 = NumberFormat.getIntegerInstance();
        nf2.setGroupingUsed(false);
        smoothnessTextField = new FormattedTextField(nf2);

        baseLabel = new Label("Base note value:");
        baseComboBox = new JComboBox<>(NOTE_VALUES);

        smallestLabel = new Label("Smallest note value:");
        smallestComboBox = new JComboBox<>(NOTE_VALUES);

        setStyle();
        addPanels();
    }

    /**
     * Returns the BPM.
     *
     * @return BPM
     */
    public String getBpm() {
        return bpmTextField.getText();
    }

    /**
     * Returns the filter size.
     *
     * @return Filter size
     */
    public String getSmoothness() {
        return smoothnessTextField.getText();
    }

    /**
     * Returns the base note value.
     *
     * @return Base note value
     */
    public Integer getBase() {
        return (Integer) baseComboBox.getSelectedItem();
    }

    /**
     * Returns the smallest note value.
     *
     * @return Smallest note value
     */
    public Integer getSmallest() {
        return (Integer) smallestComboBox.getSelectedItem();
    }

    /**
     * Resets the fields to their default values.
     */
    public void resetFields() {
        bpmTextField.setText("");
        smoothnessTextField.setText("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setStyle() {
        setOpaque(false);
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
        formPanel.add(bpmLabel);
        formPanel.add(bpmTextField);
        formPanel.add(smoothnessLabel);
        formPanel.add(smoothnessTextField);
        formPanel.add(baseLabel);
        formPanel.add(baseComboBox);
        formPanel.add(smallestLabel);
        formPanel.add(smallestComboBox);
        formPanel.add(doneButton);
        formPanel.add(clearFieldsButton);
        outerFormPanel.add(formPanel);
        mainPanel.add(outerFormPanel);
        bodyPanel.add(mainPanel);
        add(bodyPanel);
    }

    /**
     * Clears text fields.
     */
    @Override
    protected void clearFields() {
        bpmTextField.setValue(null);
        smoothnessTextField.setValue(null);
    }
}
