package org.ql.audioeditor.logic.dbaccess;

import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.database.Persistence;
import org.ql.audioeditor.database.entities.Song;
import org.ql.audioeditor.logic.dbaccess.listmodel.SongListModel;
import org.ql.audioeditor.logic.exceptions.InvalidOperationException;
import org.ql.audioeditor.logic.exceptions.SQLConnectionException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DatabaseAccessModel Backend: controls data acquired from the
 * org.ql.audioeditor.org.ql.audioeditor.database
 */
public class DatabaseAccessModel {
    /**
     * Constants
     */
    private static final String ONE_OF_THE_SONGS_ERROR =
        "One of the songs has the following error: ";
    private static final String SQL_ERROR =
        "There was a problem connecting to the org.ql.audioeditor.org.ql" +
            ".audioeditor.database.";
    private static final String NO_SONG_LOADED_ERROR = "No song is loaded.";
    private static final String NO_SUCH_SONG_ERROR =
        "There is no such song in the org.ql.audioeditor.org.ql.audioeditor" +
            ".database.";
    private static final String ALREADY_EXISTS_ERROR =
        "There is already a song with this path.";
    private static final String PATH_ERROR =
        "The path specified does not represent a valid file.";

    /**
     * Private data members
     */
    private Persistence persistence;
    private boolean invalid;

    /**
     * Constructor
     *
     * @param persistence Persistence
     */
    public DatabaseAccessModel(Persistence persistence) {
        this.persistence = persistence;
    }

    public void createTable() throws SQLConnectionException {
        if (!persistence.createTable())
            throw new SQLConnectionException(SQL_ERROR);
    }

    public SongListModel getSongList() throws SQLConnectionException {
        SongListModel slm = new SongListModel();
        slm.setSongs(getSongs());
        return slm;
    }

    /**
     * Getter
     *
     * @return all songs in the org.ql.audioeditor.org.ql.audioeditor.database
     * if logged in
     */
    private List<Song> getSongs() throws SQLConnectionException {
        List<Song> songs = persistence.getSongs();
        List<Song> validSongs = new ArrayList<>();
        if (songs == null)
            throw new SQLConnectionException(SQL_ERROR);
        for (Song s : songs) {
            File f = new File(s.getPath());
            if (f.exists() && !f.isDirectory())
                validSongs.add(s);
            else
                persistence.deleteSong(s);
        }
        invalid = (validSongs.size() < songs.size());
        return validSongs;
    }

    /**
     * Getter
     *
     * @param id ID of a certain song
     * @return The actual song if logged in
     */
    private Song getSong(int id) throws SQLConnectionException {
        try {
            return getSongs().stream()
                .filter(s -> s.getId() == id)
                .collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    /**
     * Getter
     *
     * @param path File path to a certain song
     * @return The actual song if logged in
     */
    private Song getSong(String path) throws SQLConnectionException {
        try {
            return getSongs().stream()
                .filter(s -> s.getPath().equals(path))
                .collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    public boolean isSongValid(SongModel sm) throws SQLConnectionException {
        return (getSong(sm.getPath()) != null);
    }

    public void addSong(SongModel sm)
        throws InvalidOperationException, SQLConnectionException {
        Song s = sm.getSong();
        try {
            addSong(s);
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    private void addSong(Song song)
        throws InvalidOperationException, SQLConnectionException {
        if (song.getId() == SongPropertiesLoader.getEmptySongId())
            throw new InvalidOperationException(NO_SONG_LOADED_ERROR);
        else if (getId(song) != SongPropertiesLoader.getDefaultSongId()) {
            throw new InvalidOperationException(ALREADY_EXISTS_ERROR);
        }
        else {
            if (!persistence.addSong(song))
                throw new SQLConnectionException(SQL_ERROR);
            try {
                Song newSong = getSong(song.getPath());
                if (newSong == null) {
                    throw new InvalidOperationException(PATH_ERROR);
                }
                song.setId(newSong.getId());
            } catch (SQLConnectionException e) {
                throw new SQLConnectionException(SQL_ERROR);
            }
        }
    }

    public void addSongs(SongListModel slm)
        throws InvalidOperationException, SQLConnectionException {
        List<Song> songs = slm.getItems();
        try {
            for (Song s : songs) {
                addSong(s);
            }
        } catch (InvalidOperationException e) {
            throw new InvalidOperationException(
                ONE_OF_THE_SONGS_ERROR + e.getMessage());
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    public void deleteSong(SongModel sm)
        throws InvalidOperationException, SQLConnectionException {
        Song s = sm.getSong();
        try {
            deleteSong(s);
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    private void deleteSong(Song song)
        throws InvalidOperationException, SQLConnectionException {
        if (song.getId() == -1)
            throw new InvalidOperationException(NO_SONG_LOADED_ERROR);
        try {
            int id = song.getId();
            if (getSong(id) == null)
                throw new InvalidOperationException(NO_SUCH_SONG_ERROR);
            else if (!persistence.deleteSong(song))
                throw new SQLConnectionException(SQL_ERROR);
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    public void deleteSongs(SongListModel slm)
        throws InvalidOperationException, SQLConnectionException {
        List<Song> songs = slm.getItems();
        try {
            for (Song s : songs) {
                deleteSong(s);
            }
        } catch (InvalidOperationException e) {
            throw new InvalidOperationException(
                ONE_OF_THE_SONGS_ERROR + e.getMessage());
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    public void editSong(SongModel sm)
        throws InvalidOperationException, SQLConnectionException {
        Song s = sm.getSong();
        if (s.getId() == -1)
            throw new InvalidOperationException(NO_SONG_LOADED_ERROR);
        try {
            int id = s.getId();
            if (getSong(id) == null)
                throw new InvalidOperationException(NO_SUCH_SONG_ERROR);
            else if (!persistence.editSong(s))
                throw new SQLConnectionException(SQL_ERROR);
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    public int getId(SongModel sm) throws SQLConnectionException {
        try {
            return getId(sm.getSong());
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    private int getId(Song song) throws SQLConnectionException {
        String path = song.getPath();
        Song s = getSong(path);
        if (s != null)
            return s.getId();
        else
            return SongPropertiesLoader.getDefaultSongId();
    }

    public boolean isConnected() {
        return persistence.isConnected();
    }

    public boolean hasInvalid() {
        return invalid;
    }

    public void close() {
        persistence.close();
    }
}
