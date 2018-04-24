package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.logic.dbaccess.SongModel;

import static org.ql.audioeditor.view.param.Constants.*;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.button.Button;
import org.ql.audioeditor.view.core.panel.BasicPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public final class DataPanel extends BasicPanel {
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
        mainPanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new BorderLayout());
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
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
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
