package view.panel.analysis;

import logic.matlab.MatlabHandler;
import view.element.core.label.Label;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpectrogramPanel extends JPanel implements ActionListener {
    private static int WINDOW_SIZE_MIN = 32;
    private static int WINDOW_SIZE_MAX = 262144;
    private static int HOP_SIZE_MIN = 2;
    private static int HOP_SIZE_MAX = 262144;
    private static int NFFT_MIN = 1;
    private static int NFFT_MAX = 32768;

    private MatlabHandler matlabHandler;

    private Component glassPane;
    private JButton doneButton;
    private JLabel windowSizeLabel;
    private JFormattedTextField windowSizeTextField;
    private JLabel hopSizeLabel;
    private JFormattedTextField hopSizeTextField;
    private JLabel nfftLabel;
    private JFormattedTextField nfftTextField;
    private JPanel formPanel;
    private JPanel mainPanel;

    public SpectrogramPanel(MatlabHandler matlabHandler, Component glassPane) {
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

        NumberFormatter nf = new NumberFormatter();
        nf.setMinimum(WINDOW_SIZE_MIN);
        nf.setMaximum(WINDOW_SIZE_MAX);
        windowSizeTextField = new JFormattedTextField(nf);

        hopSizeLabel = new Label("Hop size:");
        nf = new NumberFormatter();
        nf.setMinimum(HOP_SIZE_MIN);
        nf.setMaximum(HOP_SIZE_MAX);
        hopSizeTextField = new JFormattedTextField(nf);

        nfftLabel = new Label("Number of FFT points:");
        nf = new NumberFormatter();
        nf.setMinimum(NFFT_MIN);
        nf.setMaximum(NFFT_MAX);
        nfftTextField = new JFormattedTextField(nf);

        formPanel.add(windowSizeLabel);
        formPanel.add(windowSizeTextField);
        formPanel.add(hopSizeLabel);
        formPanel.add(hopSizeTextField);
        formPanel.add(nfftLabel);
        formPanel.add(nfftTextField);
        formPanel.add(doneButton);
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.BLACK);
        mainPanel.add(formPanel);
        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Object source = e.getSource();
            if (source == doneButton) {
                int windowSize = Integer.parseInt(windowSizeTextField.getText());
                System.out.println(windowSize);
                int hopSize = Integer.parseInt(hopSizeTextField.getText());
                System.out.println(hopSize);
                int nfft = Integer.parseInt(nfftTextField.getText());
                new Thread(() -> {
                    glassPane.setVisible(true);
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    matlabHandler.analyzeSong(windowSize, hopSize, nfft);
                    SwingUtilities.invokeLater(() -> {
                        glassPane.setVisible(false);
                        setCursor(Cursor.getDefaultCursor());
                    });
                }).start();
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        windowSizeTextField.setText(Integer.toString(WINDOW_SIZE_MIN));
        hopSizeTextField.setText(Integer.toString(HOP_SIZE_MIN));
        nfftTextField.setText(Integer.toString(NFFT_MIN));
    }
}
