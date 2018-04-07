package view.panel.analysis;

import properties.SongPropertiesLoader;
import view.core.label.Label;
import view.core.button.Button;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.awt.*;
import java.awt.event.ActionListener;

import static view.util.Helper.resizeImage;
import static javax.swing.BoxLayout.PAGE_AXIS;
import static view.util.Constants.SPEC_IMAGE_SIZE;
import static view.util.Constants.SPEC_IMAGE_SIZE_MAX;

public class SpectrogramPanel extends JPanel implements ItemListener {
    private static String[] windowNames = SongPropertiesLoader.getWindowNames();
    public static int DIGIT_SIZE_MIN = 1;
    private static int DIGIT_SIZE_MAX = 5;

    private JButton doneButton;
    private JButton backOptionButton;
    private JButton clearFieldsButton;
    private JToggleButton toggleButton;
    private JLabel windowSizeLabel;
    private JFormattedTextField windowSizeTextField;
    private JLabel hopSizeLabel;
    private JFormattedTextField hopSizeTextField;
    private JLabel nfftLabel;
    private JFormattedTextField nfftTextField;
    private JLabel windowLabel;
    private JComboBox windowComboBox;
    private JLabel imageLabel;
    private JLabel image3dLabel;
    private JPanel formPanel;
    private JPanel outerFormPanel;
    private JPanel imagePanel;
    private JPanel mainPanel;
    private JPanel bodyPanel;
    private ImageIcon specIcon;
    private ImageIcon specIconMax;
    private ImageIcon specIcon3d;
    private ImageIcon specIcon3dMax;

    public SpectrogramPanel(ActionListener s, ActionListener b) {
        super();
        setBackground(Color.BLACK);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6,2, 10, 10));
        formPanel.setOpaque(false);
        outerFormPanel = new JPanel();
        outerFormPanel.setOpaque(false);

        doneButton = new Button("Done", s);
        backOptionButton = new Button("Back to Main Menu", b);
        clearFieldsButton = new Button("Clear fields", e -> clearFields());

        windowSizeLabel = new Label("Window size:");
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(DIGIT_SIZE_MIN);
        nf.setMaximumIntegerDigits(DIGIT_SIZE_MAX);
        nf.setGroupingUsed(false);
        windowSizeTextField = new JFormattedTextField(nf);

        NumberFormat nf2 = NumberFormat.getIntegerInstance();
        nf2.setMinimumIntegerDigits(DIGIT_SIZE_MIN);
        nf2.setMaximumIntegerDigits(DIGIT_SIZE_MAX - 1);
        nf2.setGroupingUsed(false);
        hopSizeLabel = new Label("Hop size:");
        hopSizeTextField = new JFormattedTextField(nf2);

        nfftLabel = new Label("Number of FFT bins:");
        nfftTextField = new JFormattedTextField(nf);

        windowLabel = new Label("Window type:");
        windowComboBox = new JComboBox<>(windowNames);

        toggleButton = new JToggleButton("3D");
        toggleButton.setFocusPainted(false);
        toggleButton.addItemListener(this);

        imageLabel = new Label();
        image3dLabel = new Label();
        imagePanel = new JPanel();
        imagePanel.add(imageLabel);
        imagePanel.add(image3dLabel);
        image3dLabel.setVisible(false);

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
        formPanel.add(toggleButton);
        formPanel.add(backOptionButton);
        outerFormPanel.add(formPanel);

        bodyPanel = new JPanel(new FlowLayout());
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, PAGE_AXIS));
        mainPanel.setBackground(Color.BLACK);
        mainPanel.add(outerFormPanel);
        mainPanel.add(imagePanel);
        bodyPanel.add(mainPanel);
        add(bodyPanel);

        imagePanel.setOpaque(false);
        bodyPanel.setOpaque(false);
        mainPanel.setOpaque(false);

        specIcon = new ImageIcon();
        specIconMax = new ImageIcon();
        specIcon3d = new ImageIcon();
        specIcon3dMax = new ImageIcon();
    }

    public String getWindowSize() {
        return windowSizeTextField.getText();
    }

    public String getHopSize() {
        return hopSizeTextField.getText();
    }

    public String getNfft() {
        return nfftTextField.getText();
    }

    public String getWindow() {
        return (String) windowComboBox.getSelectedItem();
    }

    private void clearFields() {
        windowSizeTextField.setValue(null);
        hopSizeTextField.setValue(null);
        nfftTextField.setValue(null);
    }

    public void changeImage(Image image, Image image3d, boolean isNormal, boolean isMaximized) {
        if (specIcon.getImage() != null)
            specIcon.getImage().flush();
        if (specIconMax.getImage() != null)
            specIconMax.getImage().flush();
        if (specIcon3d.getImage() != null)
            specIcon3d.getImage().flush();
        if (specIcon3dMax.getImage() != null)
            specIcon3dMax.getImage().flush();

        specIconMax = new ImageIcon(resizeImage(image, SPEC_IMAGE_SIZE_MAX));
        specIcon = new ImageIcon(resizeImage(image, SPEC_IMAGE_SIZE));
        specIcon3dMax = new ImageIcon(resizeImage(image3d, SPEC_IMAGE_SIZE_MAX));
        specIcon3d = new ImageIcon(resizeImage(image3d, SPEC_IMAGE_SIZE));

        maximizeImage(isMaximized);
        if (isNormal)
            imagePanel.setVisible(true);
    }

    public void hideImage(boolean isHidden) {
        if (isHidden)
            imagePanel.setVisible(false);
        else
            imagePanel.setVisible(true);
    }

    public void maximizeImage(boolean isMaximized) {
        if (isMaximized) {
            imageLabel.setIcon(specIconMax);
            image3dLabel.setIcon(specIcon3dMax);
        }
        else {
            imageLabel.setIcon(specIcon);
            image3dLabel.setIcon(specIcon3d);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {
        if (ie.getStateChange() == ItemEvent.SELECTED) {
            image3dLabel.setVisible(true);
            imageLabel.setVisible(false);
        } else if (ie.getStateChange() == ItemEvent.DESELECTED) {
            image3dLabel.setVisible(false);
            imageLabel.setVisible(true);
        }
    }
}
