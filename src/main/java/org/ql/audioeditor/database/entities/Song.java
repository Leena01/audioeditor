package org.ql.audioeditor.database.entities;

import org.ql.audioeditor.common.properties.SongPropertiesLoader;

import java.util.Objects;

/**
 * Song object.
 */
public final class Song {
    private static final int RAND_HASH_NUM = 31;
    private int id;
    private String title;
    private String track;
    private String artist;
    private String album;
    private String year;
    private String genre;
    private String comment;
    private String path;

    /**
     * Constructor with ID.
     *
     * @param id      ID
     * @param title   Title
     * @param track   Track number
     * @param artist  Artist
     * @param album   Album
     * @param year    Year
     * @param genre   Genre
     * @param comment Comment
     * @param path    Path
     */
    public Song(int id, String title, String track, String artist, String album,
        String year, String genre, String comment, String path) {
        this.id = id;
        this.title = title;
        this.track = track;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.genre = genre;
        this.comment = comment;
        this.path = path;
    }

    /**
     * Constructor without ID.
     *
     * @param title   Title
     * @param track   Track number
     * @param artist  Artist
     * @param album   Album
     * @param year    Year
     * @param genre   Genre
     * @param comment Comment
     * @param path    Path
     */
    public Song(String title, String track, String artist, String album,
        String year, String genre, String comment, String path) {
        this(SongPropertiesLoader.getDefaultSongId(), title, track, artist,
            album, year, genre, comment, path);
    }

    /**
     * Constructor.
     */
    public Song() {
        this(SongPropertiesLoader.getEmptySongId(),
            SongPropertiesLoader.getDefaultTitle(),
            SongPropertiesLoader.getDefaultTrack(),
            SongPropertiesLoader.getDefaultArtist(),
            SongPropertiesLoader.getDefaultAlbum(),
            SongPropertiesLoader.getDefaultYear(),
            SongPropertiesLoader.getDefaultGenre(),
            SongPropertiesLoader.getDefaultComment(),
            SongPropertiesLoader.getDefaultPath());
    }

    /**
     * Copy constructor.
     *
     * @param other The other song
     */
    public Song(Song other) {
        this.id = other.id;
        this.title = other.title;
        this.track = other.track;
        this.artist = other.artist;
        this.album = other.album;
        this.year = other.year;
        this.genre = other.genre;
        this.comment = other.comment;
        this.path = other.path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Song{"
            + "id=" + id
            + ", title='" + title + '\''
            + ", track='" + track + '\''
            + ", artist='" + artist + '\''
            + ", album='" + album + '\''
            + ", year='" + year + '\''
            + ", genre='" + genre + '\''
            + ", comment='" + comment + '\''
            + ", path='" + path + '\'' + '}';
    }

    @Override
    public int hashCode() {
        int result = id;
        result = RAND_HASH_NUM * result + addHashNum(title);
        result = RAND_HASH_NUM * result + addHashNum(track);
        result = RAND_HASH_NUM * result + addHashNum(artist);
        result = RAND_HASH_NUM * result + addHashNum(album);
        result = RAND_HASH_NUM * result + addHashNum(year);
        result = RAND_HASH_NUM * result + addHashNum(genre);
        result = RAND_HASH_NUM * result + addHashNum(comment);
        result = RAND_HASH_NUM * result + addHashNum(path);
        return result;
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

        final Song other = (Song) obj;

        return Objects.equals(this.id, other.id);
    }

    /**
     * Determine field hashcode.
     *
     * @param field The field
     * @return Hashcode
     */
    private int addHashNum(String field) {
        if (field != null) {
            return field.hashCode();
        }
        return 0;
    }
}
