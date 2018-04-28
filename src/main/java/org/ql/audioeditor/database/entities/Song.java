package org.ql.audioeditor.database.entities;

import org.ql.audioeditor.common.properties.SongPropertiesLoader;

import java.util.Objects;

public class Song {

    private int id;
    private String title;
    private String track;
    private String artist;
    private String album;
    private String year;
    private String genre;
    private String comment;
    private String path;

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

    public Song(String title, String track, String artist, String album,
        String year, String genre, String comment, String path) {
        this(SongPropertiesLoader.getDefaultSongId(), title, track, artist,
            album, year, genre, comment, path);
    }

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
        return "Song{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", track='" + track + '\'' +
            ", artist='" + artist + '\'' +
            ", album='" + album + '\'' +
            ", year='" + year + '\'' +
            ", genre='" + genre + '\'' +
            ", comment='" + comment + '\'' +
            ", path='" + path + '\'' +
            '}';
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (track != null ? track.hashCode() : 0);
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + (album != null ? album.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        final Song other = (Song) obj;

        return Objects.equals(this.id, other.id);
    }
}