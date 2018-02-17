package database;

import database.entities.*;
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
