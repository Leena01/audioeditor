package view.panel;

import logic.dbaccess.SongModel;
import view.core.button.OptionButton;
import view.core.label.Label;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

import static view.util.Constants.GENRE_MAX;
import static view.util.Constants.GENRE_MIN;

/**
 * Edit panel
 */
public class EditPanel extends JPanel {
    private static final String instrLabel = "Please enter the new data for the song with the following ID: %d.";
    private JLabel infoLabel;
    private JLabel titleLabel;
    private JLabel trackLabel;
    private JLabel artistLabel;
    private JLabel albumLabel;
    private JLabel yearLabel;
    private JLabel genreLabel;
    private JLabel commentLabel;
    private JTextField titleTextField;
    private JTextField trackTextField;
    private JTextField artistTextField;
    private JTextField albumTextField;
    private JTextField yearTextField;
    private JFormattedTextField genreTextField;
    private JTextField commentTextField;
    private JButton doneButton;
    private JPanel formPanel;
    private JPanel infoPanel;
    private JPanel mainPanel;
    private JPanel bottomPanel;
    private JPanel bodyPanel;

    private SongModel selectedSongModel;

    /**
     * Constructor
     * @param al action listener
     */
    public EditPanel(ActionListener al) {
        super();

        selectedSongModel = new SongModel();
        infoPanel = new JPanel(new FlowLayout());
        formPanel = new JPanel(new GridLayout(7, 2));
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
        doneButton = new OptionButton("Done", al);

        titleLabel = new Label("Title:");
        trackLabel = new Label("Track:");
        artistLabel = new Label("Artist:");
        albumLabel = new Label("Album:");
        yearLabel = new Label("Year:");
        genreLabel = new Label("Genre:");
        commentLabel = new Label("Comment:");
        titleTextField = new JTextField("", 20);
        trackTextField = new JTextField("", 20);
        artistTextField = new JTextField("", 20);
        albumTextField = new JTextField("", 20);
        yearTextField = new JTextField("", 20);

        NumberFormatter nf = new NumberFormatter();
        nf.setMinimum(GENRE_MIN);
        nf.setMaximum(GENRE_MAX);
        genreTextField = new JFormattedTextField(nf);
        commentTextField = new JTextField("", 20);

        infoPanel.add(infoLabel);
        formPanel.add(titleLabel);
        formPanel.add(titleTextField);
        formPanel.add(trackLabel);
        formPanel.add(trackTextField);
        formPanel.add(artistLabel);
        formPanel.add(artistTextField);
        formPanel.add(albumLabel);
        formPanel.add(albumTextField);
        formPanel.add(yearLabel);
        formPanel.add(yearTextField);
        formPanel.add(genreLabel);
        formPanel.add(genreTextField);
        formPanel.add(commentLabel);
        formPanel.add(commentTextField);
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

    public void setNewData() {
        selectedSongModel.setTitle(titleTextField.getText());
        selectedSongModel.setTrack(trackTextField.getText());
        selectedSongModel.setArtist(artistTextField.getText());
        selectedSongModel.setAlbum(albumTextField.getText());
        selectedSongModel.setYear(yearTextField.getText());
        selectedSongModel.setGenre(genreTextField.getText());
        selectedSongModel.setComment(commentTextField.getText());
    }

    public SongModel getSelectedSongModel() {
        return selectedSongModel;
    }

    public void clearFields() {
        titleTextField.setText("");
        trackTextField.setText("");
        artistTextField.setText("");
        albumTextField.setText("");
        yearTextField.setText("");
        genreTextField.setText("");
        commentTextField.setText("");
    }

    public void setSelectedSong(SongModel sm) {
        if (sm != null) {
            this.selectedSongModel = new SongModel(sm);
            infoLabel.setText(String.format(instrLabel, selectedSongModel.getId()));
        }
    }
}
