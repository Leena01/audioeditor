package database.entities;

/**
 * Created by Livia on 2017.08.13..
 */
import java.util.Objects;

public class Song {
    private int id;
    private String name;
    private String artist;
    private String path;

    public Song(int id, String name, String artist, String path) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.path = path;
    }

    public Song(String name, String artist, String path) {
        this(0, name, artist, path);
    }

    public Song(Song other) {
        this.id = other.id;
        this.name = other.name;
        this.artist = other.artist;
        this.path = other.path;
    }

    public Song() {
        this(0, "", "", "");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "[" + id + ", " + name + ", " + artist + ", " + path + "]";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + this.id;
        hash = 13 * hash + Objects.hashCode(this.name);
        hash = 13 * hash + Objects.hashCode(this.artist);
        hash = 13 * hash + Objects.hashCode(this.path);
        return hash;
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
