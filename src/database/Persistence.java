package database;

import database.entities.*;

import java.util.List;

public class Persistence {

    private DatabaseDao database;

    public Persistence(DatabaseDao cache) {
        this.database = cache;
    }

    public boolean createTable() { return database.createTable(); }

    public List<Song> getSongs() {
        return database.getSongs();
    }

    public boolean addSong(Song s) {
        return database.addSong(new Song(s));
    }

    public boolean deleteSong(int id) {
        return database.deleteSong(id);
    }

    public boolean editSong(Song s) {
        return database.editSong(new Song(s));
    }

    public boolean isConnected() {
        return database.isConnected();
    }

    public void close() {
        database.close();
    }
}
