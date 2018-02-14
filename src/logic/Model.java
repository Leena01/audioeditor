package logic;
import database.*;
import database.entities.*;
import logic.exceptions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.lang.*;

/**
 * Model
 * Backend: controls data acquired from the database
 */
public class Model {
    /**
     * Constants
     */
    private static final String SQL_ERROR = "There was a problem connecting to the database.";
    private static final String NO_SONG_LOADED_ERROR = "No song is loaded.";
    private static final String NO_SUCH_SONG_ERROR = "There is no such song in the database.";
    private static final String ALREADY_EXISTS_ERROR = "There is already a song with this path.";
    private static final String OPERATION_ERROR = "An error occurred during the execution of the operation.";

    /**
     * Private data members
     */
    private Persistence persistence;

    /**
     * Constructor
     * @param persistence Persistence
     */
    public Model(Persistence persistence) {
        this.persistence = persistence;
    }


    public void createTable() throws SQLConnectionException {
        if (!persistence.createTable())
            throw new SQLConnectionException(SQL_ERROR);
    }

    /**
     * Getter
     * @return all songs in the database if logged in
     */
    public List<Song> getSongs() throws SQLConnectionException {
        List<Song> songs = persistence.getSongs();
        if (songs == null)
            throw new SQLConnectionException(SQL_ERROR);
        return songs;
    }

    /**
     * Getter
     * @param id ID of a certain song
     * @return The actual song if logged in
     */
    private Song getSong(int id) throws SQLConnectionException {
        try {
            return getSongs().stream()
                    .filter(s -> s.getId() == id)
                    .collect(Collectors.toList()).get(0);
        } catch(IndexOutOfBoundsException e) {
            return null;
        } catch(SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    /**
     * Getter
     * @param path File path to a certain song
     * @return The actual song if logged in
     */
    private Song getSong(String path) throws SQLConnectionException {
        try {
            return getSongs().stream()
                    .filter(s -> s.getPath().equals(path))
                    .collect(Collectors.toList()).get(0);
        } catch(IndexOutOfBoundsException e) {
            return null;
        } catch(SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    public int addSong(Song s) throws InvalidOperationException, SQLConnectionException {
        if (s == null)
            throw new InvalidOperationException(NO_SONG_LOADED_ERROR);
        String path = s.getPath();
        if (getSong(path) != null) {
            throw new InvalidOperationException(ALREADY_EXISTS_ERROR);
        }
        else {
            if (!persistence.addSong(s))
                throw new SQLConnectionException(SQL_ERROR);
            try {
                Song newSong = getSong(path);
                if (newSong == null) {
                    throw new InvalidOperationException(OPERATION_ERROR);
                }
                return newSong.getId();
            } catch(SQLConnectionException e) {
                throw new SQLConnectionException(SQL_ERROR);
            }
        }

    }

    public void deleteSong(int id) throws InvalidOperationException, SQLConnectionException {
        if (getSong(id) == null)
            throw new InvalidOperationException(NO_SUCH_SONG_ERROR);
        else {
            if (!persistence.deleteSong(id))
                throw new SQLConnectionException(SQL_ERROR);
        }
    }

    public void deleteSong(Song s) throws InvalidOperationException, SQLConnectionException {
        if (s == null)
            throw new InvalidOperationException(NO_SONG_LOADED_ERROR);
        try {
            deleteSong(s.getId());
        } catch(InvalidOperationException e) {
            throw new InvalidOperationException(e.getMessage());
        } catch(SQLConnectionException e) {
            throw new SQLConnectionException(e.getMessage());
        }
    }

    public void editSong(Song s) throws InvalidOperationException, SQLConnectionException {
        if (getSong(s.getId()) == null)
            throw new InvalidOperationException(NO_SUCH_SONG_ERROR);
        else {
            if (!persistence.editSong(s))
                throw new SQLConnectionException(SQL_ERROR);
        }
    }

    public void editSong(int id, String name, String artist, String path) throws InvalidOperationException, SQLConnectionException {
        if (getSong(id) == null)
            throw new InvalidOperationException(NO_SUCH_SONG_ERROR);
        else {
            if (!persistence.editSong(new Song(id, name, artist, path)))
                throw new SQLConnectionException(SQL_ERROR);
        }
    }

    public boolean isConnected() {
        return persistence.isConnected();
    }

    public void close() {
        persistence.close();
    }
}
