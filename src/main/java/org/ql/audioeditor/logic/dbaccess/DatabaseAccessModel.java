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
 * Backend: controls data acquired from the database.
 */
public final class DatabaseAccessModel {
    /**
     * Error messages.
     */
    private static final String ONE_OF_THE_SONGS_ERROR =
        "One of the songs has the following error: ";
    private static final String SQL_ERROR =
        "There was a problem connecting to the database.";
    private static final String NO_SONG_LOADED_ERROR = "No song is loaded.";
    private static final String NO_SUCH_SONG_ERROR =
        "There is no such song in the database.";
    private static final String ALREADY_EXISTS_ERROR =
        "There is already a song with this path.";
    private static final String PATH_ERROR =
        "The path specified does not represent a valid file.";

    /**
     * Private data members.
     */
    private final Persistence persistence;
    private boolean invalid;

    /**
     * Constructor.
     *
     * @param persistence Persistence
     */
    public DatabaseAccessModel(Persistence persistence) {
        this.persistence = persistence;
    }

    /**
     * Create table during initialization.
     *
     * @throws SQLConnectionException Exception caused by database connection
     */
    public void createTable() throws SQLConnectionException {
        if (!persistence.createTable()) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    /**
     * Get list of songs.
     *
     * @return Song list model
     *
     * @throws SQLConnectionException Exception caused by database connection
     */
    public SongListModel getSongList() throws SQLConnectionException {
        SongListModel slm = new SongListModel();
        slm.setSongs(getSongs());
        return slm;
    }

    /**
     * Get list of songs filtered by a given attribute.
     *
     * @param attribute Filter
     * @param sm Song model
     * @return Song list model
     *
     * @throws SQLConnectionException Exception caused by database connection
     */
    public SongListModel getSongList(String attribute, SongModel sm) throws
        SQLConnectionException {
        SongListModel slm = new SongListModel();
        slm.setSongs(getSongs(attribute, sm));
        return slm;
    }

    /**
     * Getter that checks the validity of every song in the database.
     *
     * @return Songs in the database
     */
    private List<Song> getSongs() throws SQLConnectionException {
        List<Song> songs = persistence.getSongs();
        List<Song> validSongs = new ArrayList<>();
        if (songs == null) {
            throw new SQLConnectionException(SQL_ERROR);
        }
        for (Song s : songs) {
            File f = new File(s.getPath());
            if (f.exists() && !f.isDirectory()) {
                validSongs.add(s);
            } else {
                persistence.deleteSong(s);
            }
        }
        invalid = (validSongs.size() < songs.size());
        return validSongs;
    }

    /**
     * Getter that filters songs according to a given attribute.
     *
     * @return Songs in the database
     */
    private List<Song> getSongs(String attribute, SongModel sm) throws
        SQLConnectionException {
        switch (attribute) {
            case "Artist":
                return getSongs().stream()
                    .filter(s -> s.getArtist().equals(sm.getArtist()))
                    .collect(Collectors.toList());
            case "Album":
                return getSongs().stream()
                    .filter(s -> s.getAlbum().equals(sm.getAlbum()))
                    .collect(Collectors.toList());
            case "Genre":
                return getSongs().stream()
                    .filter(s -> s.getGenre().equals(sm.getGenre()))
                    .collect(Collectors.toList());
            case "Year":
                return getSongs().stream()
                    .filter(s -> s.getYear().equals(sm.getYear()))
                    .collect(Collectors.toList());
            default:
                return null;
        }
    }

    /**
     * Getter.
     *
     * @param id ID of a certain song
     * @return The actual song
     */
    private Song getSong(int id) throws SQLConnectionException {
        try {
            return getSongs().stream().filter(s -> s.getId() == id)
                .collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    /**
     * Getter.
     *
     * @param path File path to a certain song
     * @return The actual song
     */
    private Song getSong(String path) throws SQLConnectionException {
        try {
            return getSongs().stream().filter(s -> s.getPath().equals(path))
                .collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    /**
     * Checks whether a certain song is still valid according to its path.
     *
     * @param sm Song model
     * @return Logical value
     *
     * @throws SQLConnectionException Exception caused by database connection
     */
    public boolean isSongValid(SongModel sm) throws SQLConnectionException {
        return (getSong(sm.getPath()) != null);
    }

    /**
     * Add song contained in the provided song model to the database.
     *
     * @param sm Song model
     * @throws InvalidOperationException Exception caused by an invalid
     *                                   operation
     * @throws SQLConnectionException    Exception caused by database
     *                                   connection
     */
    public void addSong(SongModel sm)
        throws InvalidOperationException, SQLConnectionException {
        Song s = sm.getSong();
        try {
            addSong(s);
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    /**
     * Add song to the database.
     *
     * @param song The actual song
     * @throws InvalidOperationException Exception caused by an invalid
     *                                   operation
     * @throws SQLConnectionException    Exception caused by database
     *                                   connection
     */
    private void addSong(Song song)
        throws InvalidOperationException, SQLConnectionException {
        if (song.getId() == SongPropertiesLoader.getEmptySongId()) {
            throw new InvalidOperationException(NO_SONG_LOADED_ERROR);
        } else if (getId(song) != SongPropertiesLoader.getDefaultSongId()) {
            throw new InvalidOperationException(ALREADY_EXISTS_ERROR);
        } else {
            if (!persistence.addSong(song)) {
                throw new SQLConnectionException(SQL_ERROR);
            }
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

    /**
     * Add multiple songs to the database.
     *
     * @param slm Song list model
     * @throws InvalidOperationException Exception caused by an invalid
     *                                   operation
     * @throws SQLConnectionException    Exception caused by database
     *                                   connection
     */
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

    /**
     * Delete song contained in the provided song model from database.
     *
     * @param sm Song list model
     * @throws InvalidOperationException Exception caused by an invalid
     *                                   operation
     * @throws SQLConnectionException    Exception caused by database
     *                                   connection
     */
    public void deleteSong(SongModel sm)
        throws InvalidOperationException, SQLConnectionException {
        Song s = sm.getSong();
        try {
            deleteSong(s);
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    /**
     * Delete song from the database.
     *
     * @param song The actual song
     * @throws InvalidOperationException Exception caused by an invalid
     *                                   operation
     * @throws SQLConnectionException    Exception caused by database
     *                                   connection
     */
    private void deleteSong(Song song)
        throws InvalidOperationException, SQLConnectionException {
        if (song.getId() == -1) {
            throw new InvalidOperationException(NO_SONG_LOADED_ERROR);
        }
        try {
            int id = song.getId();
            if (getSong(id) == null) {
                throw new InvalidOperationException(NO_SUCH_SONG_ERROR);
            } else if (!persistence.deleteSong(song)) {
                throw new SQLConnectionException(SQL_ERROR);
            }
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    /**
     * Delete multiple songs from the database.
     *
     * @param slm Song list model
     * @throws InvalidOperationException Exception caused by an invalid
     *                                   operation
     * @throws SQLConnectionException    Exception caused by database
     *                                   connection
     */
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

    /**
     * Edit song contained in the provided song model.
     *
     * @param sm Song list model
     * @throws InvalidOperationException Exception caused by an invalid
     *                                   operation
     * @throws SQLConnectionException    Exception caused by database
     *                                   connection
     */
    public void editSong(SongModel sm)
        throws InvalidOperationException, SQLConnectionException {
        Song s = sm.getSong();
        if (s.getId() == -1) {
            throw new InvalidOperationException(NO_SONG_LOADED_ERROR);
        }
        try {
            int id = s.getId();
            if (getSong(id) == null) {
                throw new InvalidOperationException(NO_SUCH_SONG_ERROR);
            } else if (!persistence.editSong(s)) {
                throw new SQLConnectionException(SQL_ERROR);
            }
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    /**
     * Returns the ID of the song contained in the provided song model.
     *
     * @param sm Song model
     * @return ID (default song ID if not present in the database)
     *
     * @throws SQLConnectionException Exception caused by database connection
     */
    public int getId(SongModel sm) throws SQLConnectionException {
        try {
            return getId(sm.getSong());
        } catch (SQLConnectionException e) {
            throw new SQLConnectionException(SQL_ERROR);
        }
    }

    /**
     * Returns the ID of a song according to the database.
     *
     * @param song The song
     * @return ID (default song ID if not present in the database)
     *
     * @throws SQLConnectionException Exception caused by database connection
     */
    private int getId(Song song) throws SQLConnectionException {
        String path = song.getPath();
        Song s = getSong(path);
        if (s != null) {
            return s.getId();
        } else {
            return SongPropertiesLoader.getDefaultSongId();
        }
    }

    /**
     * Checks whether there is any invalid song present.
     *
     * @return Logical value
     */
    public boolean hasInvalid() {
        return invalid;
    }

    /**
     * Close database connection.
     */
    public void close() {
        persistence.close();
    }
}
