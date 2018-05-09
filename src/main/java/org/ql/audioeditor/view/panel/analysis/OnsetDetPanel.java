package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.core.bar.InverseHorizontalBar;
import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.panel.BasicFormPanel;
import org.ql.audioeditor.view.core.textfield.FormattedTextField;
import org.ql.audioeditor.view.enums.NoteValue;
import org.ql.audioeditor.view.panel.SimplePlayerPanel;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.text.NumberFormatter;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;

import static org.ql.audioeditor.view.param.Constants.MEDIA_CONTROL_PANEL_HEIGHT;


import static org.ql.audioeditor.view.param.Constants.PLAYER_PANEL_BORDER;

/**
 * Panel for onset detection.
 */
public final class OnsetDetPanel extends BasicFormPanel {
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
    private final HorizontalBar mediaControlPanel;
    private final SimplePlayerPanel simplePlayerPanel;

    /**
     * Constructor.
     * @param matlabHandler MATLAB handler
     * @param d DoneButton listener
     */
    public OnsetDetPanel(MatlabHandler matlabHandler, ActionListener d) {
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

        mediaControlPanel = new InverseHorizontalBar(new BorderLayout());
        mediaControlPanel.setHeight(MEDIA_CONTROL_PANEL_HEIGHT);
        simplePlayerPanel = new SimplePlayerPanel(matlabHandler,
            mediaControlPanel);

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
     * Sets image.
     * @param totalSamples Total number of samples
     * @param freq         Sampling rate
     * @param plot         Plot
     */
    public void setOnsetImage(double totalSamples, double freq, BufferedImage
        plot) {
        simplePlayerPanel.setCurrentSong(totalSamples, freq, plot);
        simplePlayerPanel.setVisible(true);
        mediaControlPanel.setVisible(true);
    }

    /**
     * Hides the current song's settings.
     */
    public void removeSong() {
        simplePlayerPanel.setVisible(false);
        mediaControlPanel.setVisible(false);
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
        simplePlayerPanel.setBorder(PLAYER_PANEL_BORDER);
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
        mainPanel.add(simplePlayerPanel);
        mainPanel.add(mediaControlPanel);
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
