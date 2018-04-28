package org.ql.audioeditor.database;

import org.ql.audioeditor.database.entities.Song;

import java.util.List;

public interface DatabaseDao {
    boolean createTable();

    List<Song> getSongs();

    boolean addSong(Song s);

    boolean deleteSong(Song s);

    boolean editSong(Song s);

    boolean isConnected();

    void commit();

    void rollback();

    void close();
}
