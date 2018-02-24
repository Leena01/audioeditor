package view.panel;

import logic.dbaccess.SongModel;

import static view.util.Constants.*;
import view.element.core.label.Label;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DataPanel extends JPanel {
    private JLabel idLabel;
    private JLabel titleLabel;
    private JLabel trackLabel;
    private JLabel artistLabel;
    private JLabel albumLabel;
    private JLabel yearLabel;
    private JLabel genreLabel;
    private JLabel commentLabel;
    private JLabel pathLabel;
    private JLabel id;
    private JLabel title;
    private JLabel track;
    private JLabel artist;
    private JLabel album;
    private JLabel year;
    private JLabel genre;
    private JLabel comment;
    private JLabel path;
    private JButton backOptionButton;
    private JPanel labelPanel;
    private JPanel buttonPanel;
    private JPanel mainPanel;

    public DataPanel(SongModel currentSongModel, ActionListener b) {
        setBackground(Color.BLACK);
        mainPanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new FlowLayout());
        labelPanel = new JPanel(new GridLayout(9,2));
        idLabel = new Label("ID:", DATA_LABEL_SIZE);
        titleLabel = new Label("Title:", DATA_LABEL_SIZE);
        trackLabel = new Label("Track:", DATA_LABEL_SIZE);
        artistLabel = new Label("Artist:", DATA_LABEL_SIZE);
        albumLabel = new Label("Album:", DATA_LABEL_SIZE);
        yearLabel = new Label("Year:", DATA_LABEL_SIZE);
        genreLabel = new Label("Genre:", DATA_LABEL_SIZE);
        commentLabel = new Label("Comment:", DATA_LABEL_SIZE);
        pathLabel = new Label("Path:", DATA_LABEL_SIZE);
        id = new Label(Integer.toString(currentSongModel.getId()), DATA_LABEL_SIZE);
        title = new Label(currentSongModel.getTitle(), DATA_LABEL_SIZE);
        track = new Label(currentSongModel.getTrack(), DATA_LABEL_SIZE);
        artist = new Label(currentSongModel.getArtist(), DATA_LABEL_SIZE);
        album = new Label(currentSongModel.getAlbum(), DATA_LABEL_SIZE);
        year = new Label(currentSongModel.getYear(), DATA_LABEL_SIZE);
        genre = new Label(currentSongModel.getGenre(), DATA_LABEL_SIZE);
        comment = new Label(currentSongModel.getComment(), DATA_LABEL_SIZE);
        path = new Label(currentSongModel.getPath(), DATA_LABEL_SIZE);
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
        backOptionButton = new JButton();
        backOptionButton.setText("Back to Main Menu");
        backOptionButton.addActionListener(b);
        backOptionButton.setFocusPainted(false);
        buttonPanel.add(backOptionButton, BorderLayout.NORTH);
        mainPanel.add(labelPanel, BorderLayout.WEST);
        mainPanel.add(buttonPanel, BorderLayout.EAST);
        add(mainPanel);
        mainPanel.setOpaque(false);
        labelPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
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
    }
}
