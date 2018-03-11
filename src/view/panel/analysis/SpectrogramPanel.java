package view.panel.analysis;

import logic.matlab.MatlabHandler;
import view.element.core.button.Button;
import view.element.core.label.Label;

import javax.swing.*;
import java.text.NumberFormat;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.BoxLayout.PAGE_AXIS;
import static view.util.Constants.SPEC_IMAGE_NAME;

public class SpectrogramPanel extends JPanel implements ActionListener {
    private static int DIGIT_SIZE_MIN = 2;
    private static int DIGIT_SIZE_MAX = 5;

    private MatlabHandler matlabHandler;

    private Component glassPane;
    private JButton doneButton;
    private JButton backOptionButton;
    private JLabel windowSizeLabel;
    private JFormattedTextField windowSizeTextField;
    private JLabel hopSizeLabel;
    private JFormattedTextField hopSizeTextField;
    private JLabel nfftLabel;
    private JFormattedTextField nfftTextField;
    private JLabel imageLabel;
    private JPanel formPanel;
    private JPanel imagePanel;
    private JPanel mainPanel;
    private JPanel bodyPanel;
    private ImageIcon spec;

    public SpectrogramPanel(MatlabHandler matlabHandler, Component glassPane, ActionListener b) {
        super();
        this.matlabHandler = matlabHandler;
        setBackground(Color.BLACK);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.glassPane = glassPane;
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4,2, 10, 10));
        formPanel.setOpaque(false);
        doneButton = new JButton("Done");
        doneButton.setFocusPainted(false);
        doneButton.addActionListener(this);

        windowSizeLabel = new Label("Window size:");

        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(DIGIT_SIZE_MIN);
        nf.setMaximumIntegerDigits(DIGIT_SIZE_MAX);
        nf.setGroupingUsed(false);
        windowSizeTextField = new JFormattedTextField(nf);

        backOptionButton = new JButton();
        backOptionButton.setText("Back to Main Menu");
        backOptionButton.addActionListener(b);

        hopSizeLabel = new Label("Hop size:");
        hopSizeTextField = new JFormattedTextField(nf);

        nfftLabel = new Label("Number of FFT points:");
        nfftTextField = new JFormattedTextField(nf);

        imageLabel = new JLabel();
        imagePanel = new JPanel();
        imagePanel.add(imageLabel);

        formPanel.add(windowSizeLabel);
        formPanel.add(windowSizeTextField);
        formPanel.add(hopSizeLabel);
        formPanel.add(hopSizeTextField);
        formPanel.add(nfftLabel);
        formPanel.add(nfftTextField);
        formPanel.add(doneButton);
        formPanel.add(backOptionButton);

        bodyPanel = new JPanel(new FlowLayout());
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, PAGE_AXIS));
        mainPanel.setBackground(Color.BLACK);
        mainPanel.add(formPanel);
        mainPanel.add(imagePanel);
        bodyPanel.add(mainPanel);
        add(bodyPanel);

        imagePanel.setOpaque(false);
        bodyPanel.setOpaque(false);
        mainPanel.setOpaque(false);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(60, 15, 0, 15));
        spec = new ImageIcon();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Object source = e.getSource();
            if (source == doneButton) {
                double windowSize = Double.parseDouble(windowSizeTextField.getText());
                double hopSize = Double.parseDouble(hopSizeTextField.getText());
                double nfft = Double.parseDouble(nfftTextField.getText());
                new Thread(() -> {
                    glassPane.setVisible(true);
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    matlabHandler.analyzeSong(windowSize, hopSize, nfft, SPEC_IMAGE_NAME);
                    SwingUtilities.invokeLater(() -> {
                        spec = new ImageIcon(SPEC_IMAGE_NAME);
                        imageLabel.setIcon(spec);
                        glassPane.setVisible(false);
                        setCursor(Cursor.getDefaultCursor());
                    });
                }).start();
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        clearFields();
    }

    private void clearFields() {
        windowSizeTextField.setText("");
        hopSizeTextField.setText("");
        nfftTextField.setText("");
    }
}
