package org.ql.audioeditor.database;

import org.ql.audioeditor.database.entities.Song;

import java.util.List;

/**
 * Database interface.
 */
public interface DatabaseDao {
    /**
     * Creates tables.
     *
     * @return Logical value (true if successful)
     */
    boolean createTable();

    /**
     * Returns every song from the database.
     *
     * @return List of songs (null in case of SQL error)
     */
    List<Song> getSongs();

    /**
     * Adds a song to the database.
     *
     * @param s The actual song to add
     * @return Logical value (true if successful)
     */
    boolean addSong(Song s);

    /**
     * Deletes a song from the database.
     *
     * @param s The actual song to delete
     * @return Logical value (true if successful)
     */
    boolean deleteSong(Song s);

    /**
     * Edits a song in the database.
     *
     * @param s The actual song to edit
     * @return Logical value (true if successful)
     */
    boolean editSong(Song s);

    /**
     * Commits changes.
     */
    void commit();

    /**
     * Rolls back changes.
     */
    void rollback();

    /**
     * Closes connection.
     */
    void close();
}
