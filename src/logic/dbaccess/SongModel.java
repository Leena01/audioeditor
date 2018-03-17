package logic.dbaccess;

import static util.Constants.LAST_OPEN_SONG;
import database.entities.Song;
import org.jetbrains.annotations.NotNull;

public class SongModel {
    @NotNull
    private Song song;
    private double totalSamples = 0.0;
    private double freq = 0.0;

    public SongModel() {
        this.song = LAST_OPEN_SONG;
    }

    public SongModel(String title, String track, String artist, String album, String year,
                               String genre, String comment, String path) {
        this.song = new Song(title, track, artist, album, year, genre, comment, path);
    }

    public SongModel(Song song) {
        this.song = new Song(song);
    }

    public SongModel(SongModel other) {
        this.song = other.song;
        this.totalSamples = other.totalSamples;
        this.freq = other.freq;
    }

    @NotNull Song getSong() {
        return song;
    }

    public int getId() {
        return song.getId();
    }

    public String getTitle() {
        return song.getTitle();
    }

    public String getTrack() {
        return song.getTrack();
    }

    public String getArtist() {
        return song.getArtist();
    }

    public String getAlbum() {
        return song.getAlbum();
    }

    public String getYear() {
        return song.getYear();
    }

    public String getGenre() {
        return song.getGenre();
    }

    public String getComment() {
        return song.getComment();
    }

    public String getPath() {
        return song.getPath();
    }

    public double getTotalSamples() {
        return totalSamples;
    }

    public double getFreq() {
        return freq;
    }

    public void setId(int id) { song.setId(id); }

    public void setTitle(String title) {
        song.setTitle(title);
    }

    public void setTrack(String track) {
        song.setTrack(track);
    }

    public void setArtist(String artist) {
        song.setArtist(artist);
    }

    public void setAlbum(String album) {
        song.setAlbum(album);
    }

    public void setYear(String year) {
        song.setYear(year);
    }

    public void setGenre(String genre) {
        song.setGenre(genre);
    }

    public void setComment(String comment) {
        song.setComment(comment);
    }

    public void setTotalSamples(double totalSamples) {
        this.totalSamples = totalSamples;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }
}
