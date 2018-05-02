package org.ql.audioeditor.database;

import org.ql.audioeditor.database.entities.Song;

import java.util.List;

/**
 * Database interface.
 */
public interface DatabaseDao {
    /**
     * Create table.
     *
     * @return Logical value (true if successful)
     */
    boolean createTable();

    /**
     * Get every song from the database.
     *
     * @return List of songs (null in case of SQL error)
     */
    List<Song> getSongs();

    /**
     * Add a song to the database.
     *
     * @param s The actual song to add
     * @return Logical value (true if successful)
     */
    boolean addSong(Song s);

    /**
     * Delete a song from the database.
     *
     * @param s The actual song to delete
     * @return Logical value (true if successful)
     */
    boolean deleteSong(Song s);

    /**
     * Edit a song in the database.
     *
     * @param s The actual song to edit
     * @return Logical value (true if successful)
     */
    boolean editSong(Song s);

    /**
     * Commit changes.
     */
    void commit();

    /**
     * Rollback changes.
     */
    void rollback();

    /**
     * Close connection.
     */
    void close();
}
