package view.panel;

import logic.matlab.MatlabHandler;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionListener;

public class AnalysisPanel extends JPanel {
    private MatlabHandler matlabHandler;

    private JButton sendButton;
    private JTextField windowSizeField;
    private ImageIcon plotIcon;
    private JLabel imageLabel;
    private JPanel buttonPanel;
    private GridBagConstraints c;
    private JComboBox<String> channelComboBox;
    private JFormattedTextField windowSizeTextField;

    public AnalysisPanel(MatlabHandler matlabHandler, ActionListener cw) {
        super();
        setBackground(Color.BLACK);
        GridBagLayout gridbag = new GridBagLayout();
        setLayout(gridbag);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        gridbag.setConstraints(this, c);
        this.matlabHandler = matlabHandler;

        String[] channels = {"left", "right"};
        channelComboBox = new JComboBox<>(channels);
        add(channelComboBox);

        NumberFormatter nf = new NumberFormatter();
        nf.setMinimum(new Integer(30));
        nf.setMaximum(new Integer(70));
        windowSizeTextField = new JFormattedTextField(nf);
    }
}
