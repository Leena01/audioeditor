package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.panel.BasicPanel;
import org.ql.audioeditor.view.param.Constants;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;

import static javax.swing.BoxLayout.PAGE_AXIS;
import static org.ql.audioeditor.common.util.ViewUtils.resizeImage;
import static org.ql.audioeditor.view.param.Constants.SPEC_IMAGE_SIZE;
import static org.ql.audioeditor.view.param.Constants.SPEC_IMAGE_SIZE_MAX;

/**
 * Panel for showing chroma feature.
 */
public class ChromagramPanel extends BasicPanel implements ItemListener {
    private static final GridLayout FORM_PANEL_LAYOUT = new GridLayout(6, 2,
        10, 10);
    private static final String[] WINDOW_NAMES = SongPropertiesLoader
        .getWindowNames();

    protected JButton doneButton;
    protected JButton backOptionButton;
    protected JButton clearFieldsButton;
    protected JLabel windowSizeLabel;
    protected JFormattedTextField windowSizeTextField;
    protected JLabel hopSizeLabel;
    protected JFormattedTextField hopSizeTextField;
    protected JLabel nfftLabel;
    protected JFormattedTextField nfftTextField;
    protected JLabel windowLabel;
    protected JComboBox windowComboBox;
    protected JLabel imageLabel;
    protected JPanel formPanel;
    protected JPanel outerFormPanel;
    protected JPanel imagePanel;
    protected JPanel mainPanel;
    protected JPanel bodyPanel;
    private ImageIcon specIcon;
    private ImageIcon specIconMax;

    /**
     * Constructor.
     */
    public ChromagramPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        formPanel = new JPanel();
        formPanel.setLayout(FORM_PANEL_LAYOUT);
        outerFormPanel = new JPanel();

        clearFieldsButton = new Button("Clear fields", e -> clearFields());

        windowSizeLabel = new Label("Window size:");
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(Constants.TEXT_FIELD_DIGIT_SIZE_MIN);
        nf.setMaximumIntegerDigits(Constants.TEXT_FIELD_DIGIT_SIZE_MAX);
        nf.setGroupingUsed(false);
        windowSizeTextField = new JFormattedTextField(nf);

        NumberFormat nf2 = NumberFormat.getIntegerInstance();
        nf2.setMinimumIntegerDigits(Constants.TEXT_FIELD_DIGIT_SIZE_MIN);
        nf2.setMaximumIntegerDigits(Constants.TEXT_FIELD_DIGIT_SIZE_MAX - 1);
        nf2.setGroupingUsed(false);
        hopSizeLabel = new Label("Hop size:");
        hopSizeTextField = new JFormattedTextField(nf2);

        nfftLabel = new Label("Number of FFT bins:");
        nfftTextField = new JFormattedTextField(nf);

        windowLabel = new Label("Window type:");
        windowComboBox = new JComboBox<>(WINDOW_NAMES);

        imageLabel = new Label();
        imagePanel = new JPanel();

        bodyPanel = new JPanel(new FlowLayout());
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, PAGE_AXIS));

        specIcon = new ImageIcon();
        specIconMax = new ImageIcon();
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
        backOptionButton = new Button("Back to Main Menu", b);
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
     * Clears text fields.
     */
    private void clearFields() {
        windowSizeTextField.setValue(null);
        hopSizeTextField.setValue(null);
        nfftTextField.setValue(null);
    }

    /**
     * Changes the image.
     *
     * @param image       Image
     * @param isNormal    Is the size normal
     * @param isMaximized Is the image maximized
     */
    public void changeImage(Image image, boolean isNormal, boolean
        isMaximized) {
        removeImages();
        specIconMax = new ImageIcon(resizeImage(image, SPEC_IMAGE_SIZE_MAX));
        specIcon = new ImageIcon(resizeImage(image, SPEC_IMAGE_SIZE));

        maximizeImage(isMaximized);
        if (isNormal) {
            imagePanel.setVisible(true);
        }
    }

    /**
     * Hides image.
     *
     * @param isHidden Is the image hidden
     */
    public void hideImage(boolean isHidden) {
        if (isHidden) {
            imagePanel.setVisible(false);
        } else {
            imagePanel.setVisible(true);
        }
    }

    /**
     * Removes the images.
     */
    public void removeImages() {
        if (specIcon.getImage() != null) {
            specIcon.getImage().flush();
            specIcon = new ImageIcon();
        }
        if (specIconMax.getImage() != null) {
            specIconMax.getImage().flush();
            specIconMax = new ImageIcon();
        }
        imageLabel.setIcon(new ImageIcon());
    }

    /**
     * Maximizes the image.
     *
     * @param isMaximized Is the image maximized
     */
    public void maximizeImage(boolean isMaximized) {
        if (isMaximized) {
            imageLabel.setIcon(specIconMax);
        } else {
            imageLabel.setIcon(specIcon);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemStateChanged(ItemEvent ie) {
        if (ie.getStateChange() == ItemEvent.SELECTED) {
            imageLabel.setVisible(false);
        } else if (ie.getStateChange() == ItemEvent.DESELECTED) {
            imageLabel.setVisible(true);
        }
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
}
