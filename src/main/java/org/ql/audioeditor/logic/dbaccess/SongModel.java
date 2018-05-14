package org.ql.audioeditor.logic.dbaccess;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.database.entities.Song;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

/**
 * Model class to encapsulate a song. The ID of the song contained in this class
 * may not be changed.
 */
public final class SongModel {
    private static final int DEFAULT_ID
        = SongPropertiesLoader.getDefaultSongId();
    private static final int EMPTY_ID
        = SongPropertiesLoader.getEmptySongId();
    private static final Song EMPTY_SONG = new Song();
    private Song song;
    private double totalSamples = 0.0;
    private double freq = 0.0;
    private boolean saved = false;
    private Image cover = ImageLoader.getCover();
    private BufferedImage plot = null;

    /**
     * Constructor.
     */
    public SongModel() {
        this.song = EMPTY_SONG;
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
        this.saved = other.saved;
        this.cover = other.cover;
        this.plot = other.plot;
    }

    /**
     * Returns the song.
     *
     * @return Song
     */
    Song getSong() {
        return song;
    }

    /**
     * Sets the song.
     *
     * @param song Song
     */
    public void setSong(Song song) {
        this.song = new Song(song);
    }

    /**
     * Returns the ID of the song.
     *
     * @return ID
     */
    public int getId() {
        return song.getId();
    }

    /**
     * Sets the ID of the song.
     *
     * @param id ID
     */
    public void setId(int id) {
        song.setId(id);
    }

    /**
     * Returns the title of the song.
     *
     * @return Title
     */
    public String getTitle() {
        return song.getTitle();
    }

    /**
     * Sets the title of the song.
     *
     * @param title Title
     */
    public void setTitle(String title) {
        song.setTitle(title);
    }

    /**
     * Returns the track number.
     *
     * @return Track number
     */
    public String getTrack() {
        return song.getTrack();
    }

    /**
     * Sets the track number.
     *
     * @param track Track number
     */
    public void setTrack(String track) {
        song.setTrack(track);
    }

    /**
     * Returns the artist of the song.
     *
     * @return Artist
     */
    public String getArtist() {
        return song.getArtist();
    }

    /**
     * Sets the artist of the song.
     *
     * @param artist Artist
     */
    public void setArtist(String artist) {
        song.setArtist(artist);
    }

    /**
     * Returns the album of the song.
     *
     * @return Album
     */
    public String getAlbum() {
        return song.getAlbum();
    }

    /**
     * Sets the album of the song.
     *
     * @param album Album
     */
    public void setAlbum(String album) {
        song.setAlbum(album);
    }

    /**
     * Returns the year.
     *
     * @return Year
     */
    public String getYear() {
        return song.getYear();
    }

    /**
     * Sets the year.
     *
     * @param year Year
     */
    public void setYear(String year) {
        song.setYear(year);
    }

    /**
     * Returns the genre of the song.
     *
     * @return Genre
     */
    public String getGenre() {
        return song.getGenre();
    }

    /**
     * Sets the genre of the song.
     *
     * @param genre Genre
     */
    public void setGenre(String genre) {
        song.setGenre(genre);
    }

    /**
     * Returns the comment that belongs to the song.
     *
     * @return Comment
     */
    public String getComment() {
        return song.getComment();
    }

    /**
     * Sets the comment that belongs to the song.
     *
     * @param comment Comment
     */
    public void setComment(String comment) {
        song.setComment(comment);
    }

    /**
     * Returns the song's path.
     *
     * @return Path
     */
    public String getPath() {
        return song.getPath();
    }

    /**
     * Sets the song's path.
     *
     * @param path Path
     */
    public void setPath(String path) {
        song.setPath(path);
    }

    /**
     * Returns the total number of samples.
     *
     * @return Number of samples
     */
    public double getTotalSamples() {
        return totalSamples;
    }

    /**
     * Sets the total number of samples.
     *
     * @param totalSamples Number of samples
     */
    public void setTotalSamples(double totalSamples) {
        this.totalSamples = totalSamples;
    }

    /**
     * Returns the sample rate.
     *
     * @return Sample rate
     */
    public double getFreq() {
        return freq;
    }

    /**
     * Sets the sample rate.
     *
     * @param freq Sample rate
     */
    public void setFreq(double freq) {
        this.freq = freq;
    }

    /**
     * Returns whether the current song is stored in the database.
     *
     * @return Logical value (true if stored)
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * Sets the current song's save status.
     *
     * @param isSaved Shows whether the current song is saved in the database
     */
    public void setSaved(boolean isSaved) {
        this.saved = isSaved;
    }

    /**
     * Returns the cover that belongs to the song.
     *
     * @return Cover
     */
    public Image getCover() {
        return cover;
    }

    /**
     * Sets the cover that belongs to the song.
     *
     * @param cover Cover
     */
    public void setCover(Image cover) {
        this.cover = cover;
    }

    /**
     * Returns the plot that belongs to the song.
     *
     * @return Plot
     */
    public BufferedImage getPlot() {
        return plot;
    }

    /**
     * Sets the plot that belongs to the song.
     *
     * @param plot Plot
     */
    public void setPlot(BufferedImage plot) {
        this.plot = plot;
    }

    /**
     * Returns whether there is a song loaded.
     *
     * @return Logical value (true if not loaded)
     */
    public boolean isEmpty() {
        return getId() == EMPTY_ID;
    }

    /**
     * Returns whether the song is the default song.
     *
     * @return Logical value
     */
    public boolean isDefault() {
        return getId() == DEFAULT_ID;
    }

    /**
     * Sets the ID of the song to the default.
     */
    public void setDefault() {
        song.setId(DEFAULT_ID);
    }

    /**
     * Returns the MP3 tags if present.
     */
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final SongModel other = (SongModel) obj;

        return Objects.equals(this.song, other.song);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public int hashCode() {
        return song.hashCode();
    }
}
