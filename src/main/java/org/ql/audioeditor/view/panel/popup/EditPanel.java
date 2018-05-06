package org.ql.audioeditor.view.panel.popup;

import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.logic.dbaccess.SongModel;
import org.ql.audioeditor.view.core.button.OptionButton;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.panel.BasicPanel;
import org.ql.audioeditor.view.param.Constants;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * Panel for user-induced modification of the data related to the current song.
 */
public final class EditPanel extends BasicPanel {
    private static final int FIELD_LENGTH = 20;
    private static final String TRACK_LIMIT = "Only numbers are allowed.";
    private static final String YEAR_LIMIT =
        "Only four-digit numbers are allowed.";
    private static final String GENRE_LIMIT = "Only numbers between -1 and "
        + "191 are allowed (see genre list in ID3v1).";
    private static final String INSTR_TEXT =
        "Please enter the new data for the song with the following ID: %d.";
    private static final GridLayout FORM_PANEL_LAYOUT = new GridLayout(7, 2);
    private static final Border INFO_PANEL_BORDER =
        BorderFactory.createEmptyBorder(0, 0, 15, 0);
    private static final Border BODY_PANEL_BORDER =
        BorderFactory.createEmptyBorder(20, 30, 30, 30);
    private static final Border BOTTOM_PANEL_BORDER =
        BorderFactory.createEmptyBorder(15, 0, 0, 0);
    private final JLabel infoLabel;
    private final JLabel titleLabel;
    private final JLabel trackLabel;
    private final JLabel artistLabel;
    private final JLabel albumLabel;
    private final JLabel yearLabel;
    private final JLabel genreLabel;
    private final JLabel commentLabel;
    private final JTextField titleTextField;
    private final JTextField trackTextField;
    private final JTextField artistTextField;
    private final JTextField albumTextField;
    private final JTextField yearTextField;
    private final JFormattedTextField genreTextField;
    private final JTextField commentTextField;
    private final JButton doneButton;
    private final JPanel formPanel;
    private final JPanel infoPanel;
    private final JPanel mainPanel;
    private final JPanel bottomPanel;
    private final JPanel bodyPanel;

    private SongModel selectedSongModel;

    /**
     * Constructor.
     *
     * @param al Action listener
     */
    public EditPanel(ActionListener al) {
        super();
        selectedSongModel = new SongModel();
        infoPanel = new JPanel(new FlowLayout());
        formPanel = new JPanel(FORM_PANEL_LAYOUT);
        bottomPanel = new JPanel(new FlowLayout());
        bodyPanel = new JPanel(new FlowLayout());
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        infoLabel = new Label();
        doneButton = new OptionButton("Done", al);

        titleLabel = new Label("Title:");
        trackLabel = new Label("Track:");
        trackLabel.setToolTipText(TRACK_LIMIT);
        artistLabel = new Label("Artist:");
        albumLabel = new Label("Album:");
        yearLabel = new Label("Year:");
        yearLabel.setToolTipText(YEAR_LIMIT);
        genreLabel = new Label("Genre:");
        genreLabel.setToolTipText(GENRE_LIMIT);
        commentLabel = new Label("Comment:");
        titleTextField = new JTextField("", FIELD_LENGTH);

        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(Constants.TEXT_FIELD_DIGIT_SIZE_MIN);
        nf.setMaximumIntegerDigits(Constants.TEXT_FIELD_DIGIT_SIZE_MAX);
        nf.setGroupingUsed(false);
        trackTextField = new JTextField("", FIELD_LENGTH);
        trackTextField.setToolTipText(TRACK_LIMIT);

        artistTextField = new JTextField("", FIELD_LENGTH);
        albumTextField = new JTextField("", FIELD_LENGTH);

        NumberFormat nf2 = NumberFormat.getIntegerInstance();
        nf2.setMinimumIntegerDigits(Constants.YEAR_DIGIT_SIZE);
        nf2.setMaximumIntegerDigits(Constants.YEAR_DIGIT_SIZE);
        nf2.setGroupingUsed(false);
        yearTextField = new JFormattedTextField(nf2);
        yearTextField.setToolTipText(YEAR_LIMIT);

        NumberFormatter nf3 = new NumberFormatter();
        nf3.setMinimum(SongPropertiesLoader.getGenreMin());
        nf3.setMaximum(SongPropertiesLoader.getGenreMax());
        genreTextField = new JFormattedTextField(nf3);
        genreTextField.setToolTipText(GENRE_LIMIT);

        commentTextField = new JTextField("", FIELD_LENGTH);

        setStyle();
        addPanels();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
        GradientPaint gp =
            new GradientPaint(0, 0, Color.DARK_GRAY, 0, h, Color.BLACK);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }

    /**
     * Fetches the data entered by the user and changes the current song
     * according to it.
     */
    public void setNewData() {
        selectedSongModel.setTitle(titleTextField.getText());
        selectedSongModel.setTrack(trackTextField.getText());
        selectedSongModel.setArtist(artistTextField.getText());
        selectedSongModel.setAlbum(albumTextField.getText());
        selectedSongModel.setYear(yearTextField.getText());
        selectedSongModel.setGenre(genreTextField.getText());
        selectedSongModel.setComment(commentTextField.getText());
    }

    /**
     * Returns the selected song's model.
     *
     * @return Song model
     */
    public SongModel getSelectedSongModel() {
        return selectedSongModel;
    }

    /**
     * Sets the song selected.
     *
     * @param sm Song model
     */
    public void setSelectedSong(SongModel sm) {
        if (sm != null) {
            this.selectedSongModel = new SongModel(sm);
            infoLabel
                .setText(String.format(INSTR_TEXT, selectedSongModel.getId()));
            titleTextField.setText(selectedSongModel.getTitle());
            trackTextField.setText(selectedSongModel.getTrack());
            artistTextField.setText(selectedSongModel.getArtist());
            albumTextField.setText(selectedSongModel.getAlbum());
            yearTextField.setText(selectedSongModel.getYear());
            genreTextField
                .setText(selectedSongModel.getGenre().split("\\s+")[0]);
            commentTextField.setText(selectedSongModel.getComment());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setStyle() {
        infoPanel.setOpaque(false);
        formPanel.setOpaque(false);
        bottomPanel.setOpaque(false);
        bodyPanel.setOpaque(false);
        mainPanel.setOpaque(false);
        infoPanel.setBorder(INFO_PANEL_BORDER);
        bodyPanel.setBorder(BODY_PANEL_BORDER);
        bottomPanel.setBorder(BOTTOM_PANEL_BORDER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPanels() {
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
        add(bodyPanel, BorderLayout.CENTER);
    }
}
