package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.logic.dbaccess.SongModel;
import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.panel.BasicPanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import static org.ql.audioeditor.view.param.Constants.DATA_LABEL_SIZE;

/**
 * Panel showing the data related to the current song.
 */
public final class DataPanel extends BasicPanel {
    private static final GridLayout LABEL_PANEL_LAYOUT = new GridLayout(9, 2);
    private static final Border BUTTON_PANEL_BORDER =
        BorderFactory.createEmptyBorder(15, 0, 0, 0);
    private final JLabel idLabel;
    private final JLabel titleLabel;
    private final JLabel trackLabel;
    private final JLabel artistLabel;
    private final JLabel albumLabel;
    private final JLabel yearLabel;
    private final JLabel genreLabel;
    private final JLabel commentLabel;
    private final JLabel pathLabel;
    private final JLabel id;
    private final JLabel title;
    private final JLabel track;
    private final JLabel artist;
    private final JLabel album;
    private final JLabel year;
    private final JLabel genre;
    private final JLabel comment;
    private final JLabel path;
    private final JButton backOptionButton;
    private final JPanel labelPanel;
    private final JPanel buttonPanel;
    private final JPanel mainPanel;

    public DataPanel(SongModel currentSongModel, ActionListener b) {
        mainPanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new BorderLayout());
        labelPanel = new JPanel(LABEL_PANEL_LAYOUT);
        idLabel = new Label("ID:", DATA_LABEL_SIZE);
        titleLabel = new Label("Title:", DATA_LABEL_SIZE);
        trackLabel = new Label("Track:", DATA_LABEL_SIZE);
        artistLabel = new Label("Artist:", DATA_LABEL_SIZE);
        albumLabel = new Label("Album:", DATA_LABEL_SIZE);
        yearLabel = new Label("Year:", DATA_LABEL_SIZE);
        genreLabel = new Label("Genre:", DATA_LABEL_SIZE);
        commentLabel = new Label("Comment:", DATA_LABEL_SIZE);
        pathLabel = new Label("Path:", DATA_LABEL_SIZE);
        id = new Label(Integer.toString(currentSongModel.getId()),
            DATA_LABEL_SIZE);
        title = new Label(currentSongModel.getTitle(), DATA_LABEL_SIZE);
        track = new Label(currentSongModel.getTrack(), DATA_LABEL_SIZE);
        artist = new Label(currentSongModel.getArtist(), DATA_LABEL_SIZE);
        album = new Label(currentSongModel.getAlbum(), DATA_LABEL_SIZE);
        year = new Label(currentSongModel.getYear(), DATA_LABEL_SIZE);
        genre = new Label(currentSongModel.getGenre(), DATA_LABEL_SIZE);
        comment = new Label(currentSongModel.getComment(), DATA_LABEL_SIZE);
        path = new Label(currentSongModel.getPath(), DATA_LABEL_SIZE);
        backOptionButton = new Button("Back to Main Menu", b);
        setToolTipTexts();
        setStyle();
        addPanels();
    }

    public void setSongData(SongModel currentSongModel) {
        id.setText(Integer.toString(currentSongModel.getId()));
        title.setText(currentSongModel.getTitle());
        track.setText(currentSongModel.getTrack());
        artist.setText(currentSongModel.getArtist());
        album.setText(currentSongModel.getAlbum());
        year.setText(currentSongModel.getYear());
        genre.setText(currentSongModel.getGenre());
        comment.setText(currentSongModel.getComment());
        path.setText(currentSongModel.getPath());
        setToolTipTexts();
    }

    private void setToolTipTexts() {
        id.setToolTipText(id.getText());
        title.setToolTipText(title.getText());
        track.setToolTipText(track.getText());
        artist.setToolTipText(artist.getText());
        album.setToolTipText(album.getText());
        year.setToolTipText(year.getText());
        genre.setToolTipText(genre.getText());
        comment.setToolTipText(comment.getText());
        path.setToolTipText(path.getText());
    }

    @Override
    protected void setStyle() {
        setBackground(Color.BLACK);
        mainPanel.setOpaque(false);
        labelPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BUTTON_PANEL_BORDER);
    }

    @Override
    protected void addPanels() {
        labelPanel.add(idLabel);
        labelPanel.add(id);
        labelPanel.add(titleLabel);
        labelPanel.add(title);
        labelPanel.add(trackLabel);
        labelPanel.add(track);
        labelPanel.add(artistLabel);
        labelPanel.add(artist);
        labelPanel.add(albumLabel);
        labelPanel.add(album);
        labelPanel.add(yearLabel);
        labelPanel.add(year);
        labelPanel.add(genreLabel);
        labelPanel.add(genre);
        labelPanel.add(commentLabel);
        labelPanel.add(comment);
        labelPanel.add(pathLabel);
        labelPanel.add(path);
        buttonPanel.add(backOptionButton, BorderLayout.NORTH);
        mainPanel.add(labelPanel, BorderLayout.WEST);
        mainPanel.add(buttonPanel, BorderLayout.EAST);
        add(mainPanel);
    }
}
