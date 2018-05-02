package org.ql.audioeditor.database;

import org.ql.audioeditor.database.entities.Song;

import java.util.List;

/**
 * Persistence to store created objects in one session.
 */
public final class Persistence {

    /**
     * Database to read data from.
     */
    private final DatabaseDao database;

    /**
     * Constructor.
     *
     * @param database Database to read data from
     */
    public Persistence(DatabaseDao database) {
        this.database = database;
    }

    /**
     * Create table.
     *
     * @return Logical value (true if successful)
     */
    public boolean createTable() {
        return database.createTable();
    }

    /**
     * Get every song from the database.
     *
     * @return List of songs (null in case of SQL error)
     */
    public List<Song> getSongs() {
        return database.getSongs();
    }

    /**
     * Add a song to the database.
     *
     * @param s The actual song to add
     * @return Logical value (true if successful)
     */
    public boolean addSong(Song s) {
        return database.addSong(new Song(s));
    }

    /**
     * Delete a song from the database.
     *
     * @param s The actual song to delete
     * @return Logical value (true if successful)
     */
    public boolean deleteSong(Song s) {
        return database.deleteSong(s);
    }

    /**
     * Edit a song in the database.
     *
     * @param s The actual song to edit
     * @return Logical value (true if successful)
     */
    public boolean editSong(Song s) {
        return database.editSong(new Song(s));
    }

    /**
     * Close database connection.
     */
    public void close() {
        database.close();
    }
}
