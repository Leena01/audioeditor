package org.ql.audioeditor.logic.dbaccess;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.database.entities.Song;

import java.io.File;
import java.io.IOException;

/**
 * Model class to encapsulate a song. The ID of the song contained in this class
 * may not be changed.
 */
public final class SongModel {
    private static final Song DEFAULT_SONG = new Song();
    private Song song;
    private double totalSamples = 0.0;
    private double freq = 0.0;
    private boolean isSaved = false;

    /**
     * Default constructor.
     */
    public SongModel() {
        this.song = DEFAULT_SONG;
    }

    /**
     * Constructor using a file to open a song.
     *
     * @param f File to open
     */
    public SongModel(File f) {
        String path = f.getPath();
        String title = f.getName();
        getTags(title, path);
    }

    /**
     * Constructor having a song to enclose.
     *
     * @param song Song to encapsulate.
     */
    public SongModel(Song song) {
        this.song = new Song(song);
    }

    /**
     * Copy constructor.
     *
     * @param other The other song model.
     */
    public SongModel(SongModel other) {
        this.song = other.song;
        this.totalSamples = other.totalSamples;
        this.freq = other.freq;
    }

    Song getSong() {
        return song;
    }

    public int getId() {
        return song.getId();
    }

    public void setId(int id) {
        song.setId(id);
    }

    public String getTitle() {
        return song.getTitle();
    }

    public void setTitle(String title) {
        song.setTitle(title);
    }

    public String getTrack() {
        return song.getTrack();
    }

    public void setTrack(String track) {
        song.setTrack(track);
    }

    public String getArtist() {
        return song.getArtist();
    }

    public void setArtist(String artist) {
        song.setArtist(artist);
    }

    public String getAlbum() {
        return song.getAlbum();
    }

    public void setAlbum(String album) {
        song.setAlbum(album);
    }

    public String getYear() {
        return song.getYear();
    }

    public void setYear(String year) {
        song.setYear(year);
    }

    public String getGenre() {
        return song.getGenre();
    }

    public void setGenre(String genre) {
        song.setGenre(genre);
    }

    public String getComment() {
        return song.getComment();
    }

    public void setComment(String comment) {
        song.setComment(comment);
    }

    public String getPath() {
        return song.getPath();
    }

    public void setPath(String path) {
        song.setPath(path);
    }

    public double getTotalSamples() {
        return totalSamples;
    }

    public void setTotalSamples(double totalSamples) {
        this.totalSamples = totalSamples;
    }

    public double getFreq() {
        return freq;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public boolean isEmpty() {
        return getId() == SongPropertiesLoader.getEmptySongId();
    }

    public boolean isDefault() {
        return getId() == SongPropertiesLoader.getDefaultSongId();
    }

    public void setDefault() {
        song.setId(SongPropertiesLoader.getDefaultSongId());
    }

    private void getTags(String title, String path) {
        String track = SongPropertiesLoader.getDefaultTrack();
        String artist = SongPropertiesLoader.getDefaultArtist();
        String album = SongPropertiesLoader.getDefaultAlbum();
        String year = SongPropertiesLoader.getDefaultYear();
        String genre = SongPropertiesLoader.getDefaultGenre();
        String comment = SongPropertiesLoader.getDefaultComment();
        try {
            Mp3File song = new Mp3File(path);
            if (song.hasId3v1Tag()) {
                ID3v1 id3v1Tag = song.getId3v1Tag();
                title = id3v1Tag.getTitle();
                track = id3v1Tag.getTrack();
                artist = id3v1Tag.getArtist();
                album = id3v1Tag.getAlbum();
                year = id3v1Tag.getYear();
                genre = id3v1Tag.getGenre() + " ("
                    + id3v1Tag.getGenreDescription() + ")";
                comment = id3v1Tag.getComment();
            }
        } catch (Exception ignored) {
        }
        this.song =
            new Song(title, track, artist, album, year, genre, comment, path);
    }

    public void setTags()
        throws UnsupportedTagException, NotSupportedException,
        InvalidDataException, IOException {
        Mp3File song = new Mp3File(getPath());
        ID3v1 id3v1Tag;
        if (song.hasId3v1Tag()) {
            id3v1Tag = song.getId3v1Tag();
        } else {
            id3v1Tag = new ID3v1Tag();
            song.setId3v1Tag(id3v1Tag);
        }
        id3v1Tag.setTitle(getTitle());
        id3v1Tag.setTrack(getTrack());
        id3v1Tag.setArtist(getArtist());
        id3v1Tag.setAlbum(getAlbum());
        id3v1Tag.setYear(getYear());
        id3v1Tag.setGenre(Integer.parseInt(getGenre()));
        id3v1Tag.setComment(getComment());
        song.save(getPath());
        setGenre(
            id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
    }
}
