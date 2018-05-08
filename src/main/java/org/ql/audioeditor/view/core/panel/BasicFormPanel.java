package org.ql.audioeditor.view.core.panel;

import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.label.Label;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Image;

import static javax.swing.BoxLayout.PAGE_AXIS;
import static org.ql.audioeditor.common.util.ViewUtils.resizeImage;
import static org.ql.audioeditor.view.param.Constants.IMAGE_SIZE;
import static org.ql.audioeditor.view.param.Constants.IMAGE_SIZE_MAX;

/**
 * Basic panel with form.
 */
public abstract class BasicFormPanel extends BasicPanel {
    protected final JPanel formPanel;
    protected final JButton clearFieldsButton;
    protected final JLabel imageLabel;
    protected final JPanel outerFormPanel;
    protected final JPanel imagePanel;
    protected final JPanel mainPanel;
    protected final JPanel bodyPanel;
    protected JButton doneButton;
    private ImageIcon imageIcon;
    private ImageIcon imageIconMax;

    /**
     * Constructor.
     */
    public BasicFormPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        formPanel = new JPanel();
        outerFormPanel = new JPanel();

        clearFieldsButton = new Button("Clear fields", e -> clearFields());

        imageLabel = new Label();
        imagePanel = new JPanel();

        bodyPanel = new JPanel(new FlowLayout());
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, PAGE_AXIS));

        imageIcon = new ImageIcon();
        imageIconMax = new ImageIcon();
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
        imageIcon = new ImageIcon(resizeImage(image, IMAGE_SIZE_MAX));
        imageIconMax = new ImageIcon(resizeImage(image, IMAGE_SIZE));

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
        if (imageIcon.getImage() != null) {
            imageIcon.getImage().flush();
            imageIcon = new ImageIcon();
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
            imageLabel.setIcon(imageIconMax);
        } else {
            imageLabel.setIcon(imageIcon);
        }
    }

    /**
     * Clear text fields.
     */
    protected abstract void clearFields();
}
