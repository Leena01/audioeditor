package view;

import database.entities.Song;
import view.decorated.OptionButton;
import view.decorated.Label;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Edit panel
 */
class EditPanel extends JPanel {

    private JLabel infoLabel;
    private JLabel nameLabel;
    private JLabel artistLabel;
    private JTextField nameTextField;
    private JTextField artistTextField;
    private JButton doneButton;
    private JPanel formPanel;
    private JPanel infoPanel;
    private JPanel mainPanel;
    private JPanel bottomPanel;
    private JPanel bodyPanel;

    private Song selectedSong;

    /**
     * Constructor
     * @param al action listener
     */
    EditPanel(ActionListener al) {
        super();

        selectedSong = null;
        infoPanel = new JPanel(new FlowLayout());
        formPanel = new JPanel(new GridLayout(2, 2));
        bottomPanel = new JPanel(new FlowLayout());
        bodyPanel = new JPanel(new FlowLayout());
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        infoPanel.setOpaque(false);
        formPanel.setOpaque(false);
        bottomPanel.setOpaque(false);
        bodyPanel.setOpaque(false);
        mainPanel.setOpaque(false);

        infoLabel = new Label();
        doneButton = new OptionButton("Done");

        nameLabel = new Label("Song name:");
        artistLabel = new Label("Artist: ");
        nameTextField = new JTextField("", 20);
        artistTextField = new JTextField("", 20);
        doneButton.addActionListener(al);

        infoPanel.add(infoLabel);
        formPanel.add(nameLabel);
        formPanel.add(nameTextField);
        formPanel.add(artistLabel);
        formPanel.add(artistTextField);
        bottomPanel.add(doneButton);

        mainPanel.add(infoPanel);
        mainPanel.add(formPanel);
        mainPanel.add(bottomPanel);
        bodyPanel.add(mainPanel);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        add(bodyPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        GradientPaint gp = new GradientPaint(0, 0, Color.DARK_GRAY, 0, h, Color.BLACK);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }

    /**
     * Getter
     * @return Return input data entered (name)
     */
    String getNameField() {
        return nameTextField.getText();
    }

    /**
     * Getter
     * @return Return input data entered (password)
     */
    String getArtistField() {
        return artistTextField.getText();
    }


    int getSelectedId() {
        if (selectedSong != null)
            return selectedSong.getId();
        return 0;
    }

    String getSelectedPath() {
        if (selectedSong != null)
            return selectedSong.getPath();
        return "";
    }

    /**
     * Clear all fields
     */
    void clearFields() {
        nameTextField.setText("");
        artistTextField.setText("");
    }

    void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
        infoLabel.setText("Please enter the new data for the song with the following ID: " + selectedSong.getId() + ".");
    }
}
