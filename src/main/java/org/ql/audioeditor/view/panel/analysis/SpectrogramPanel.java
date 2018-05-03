package org.ql.audioeditor.view.panel.analysis;

import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.label.Label;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static org.ql.audioeditor.common.util.Helper.resizeImage;
import static org.ql.audioeditor.view.param.Constants.SPEC_IMAGE_SIZE;
import static org.ql.audioeditor.view.param.Constants.SPEC_IMAGE_SIZE_MAX;

/**
 * Panel for showing spectrograms.
 */
public final class SpectrogramPanel extends ChromagramPanel {
    private final JToggleButton toggleButton;
    private final JLabel image3dLabel;
    private ImageIcon specIcon3d;
    private ImageIcon specIcon3dMax;

    public SpectrogramPanel(ActionListener s, ActionListener b) {
        super();
        doneButton = new Button("Done", s);
        backOptionButton = new Button("Back to Main Menu", b);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        toggleButton = new JToggleButton("3D");
        toggleButton.addItemListener(new ToggleButtonListener());

        image3dLabel = new Label();

        specIcon3d = new ImageIcon();
        specIcon3dMax = new ImageIcon();

        setStyle();
        addPanels();
    }

    public void changeImage(Image image, Image image3d, boolean isNormal,
        boolean isMaximized) {
        if (specIcon3d != null) {
            specIcon3d.getImage().flush();
        }
        if (specIcon3dMax != null) {
            specIcon3dMax.getImage().flush();
        }

        specIcon3dMax =
            new ImageIcon(resizeImage(image3d, SPEC_IMAGE_SIZE_MAX));
        specIcon3d = new ImageIcon(resizeImage(image3d, SPEC_IMAGE_SIZE));
        super.changeImage(image, isNormal, isMaximized);
    }

    @Override
    public void maximizeImage(boolean isMaximized) {
        super.maximizeImage(isMaximized);
        if (isMaximized) {
            image3dLabel.setIcon(specIcon3dMax);
        } else {
            image3dLabel.setIcon(specIcon3d);
        }
    }

    @Override
    protected void setStyle() {
        super.setStyle();
        toggleButton.setFocusPainted(false);
        image3dLabel.setVisible(false);
    }

    @Override
    protected void addPanels() {
        imagePanel.add(imageLabel);
        imagePanel.add(image3dLabel);
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
        mainPanel.add(outerFormPanel);
        mainPanel.add(imagePanel);
        bodyPanel.add(mainPanel);
        add(bodyPanel);
    }

    /**
     * Item listener for the toggle button.
     */
    private final class ToggleButtonListener implements
        ItemListener {
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
}
