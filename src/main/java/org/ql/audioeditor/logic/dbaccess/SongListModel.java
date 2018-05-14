package org.ql.audioeditor.logic.dbaccess;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.database.entities.Song;
import org.ql.audioeditor.logic.dbaccess.adt.ListModel;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Model class designed to encapsulate a list of songs.
 */
public final class SongListModel extends ListModel<Song> {

    /**
     * Constructor.
     */
    public SongListModel() {
        super();
    }

    /**
     * Constructor.
     *
     * @param files List of files to save as songs
     */
    public SongListModel(List<File> files) {
        super();
        for (File f : files) {
            String path = f.getPath();
            String title = f.getName();
            String track = SongPropertiesLoader.getDefaultTrack();
            String artist = SongPropertiesLoader.getDefaultArtist();
            String album = SongPropertiesLoader.getDefaultAlbum();
            String year = SongPropertiesLoader.getDefaultYear();
            String genre = SongPropertiesLoader.getDefaultGenre();
            String comment = SongPropertiesLoader.getDefaultComment();

            try {
                Mp3File song = new Mp3File(path);
                if (song.hasId3v1Tag()) {
                    ID3v1 id3v1Tag = song.getId3v1Tag();
                    title = id3v1Tag.getTitle();
                    track = id3v1Tag.getTrack();
                    artist = id3v1Tag.getArtist();
                    album = id3v1Tag.getAlbum();
                    year = id3v1Tag.getYear();
                    genre = id3v1Tag.getGenre() + " ("
                        + id3v1Tag.getGenreDescription() + ")";
                    comment = id3v1Tag.getComment();
                }
            } catch (Exception ignored) {
            }
            items.add(
                new Song(title, track, artist, album, year, genre, comment,
                    path));
        }
    }

    /**
     * Returns the previous song in the list.
     *
     * @param sm Song model
     * @return The previous song
     */
    public SongModel getPreviousSong(SongModel sm) {
        int index = currentSongPos(sm);
        if (index <= 0) {
            return null;
        } else {
            return new SongModel(items.get(index - 1));
        }
    }

    /**
     * Returns the next song in the list.
     *
     * @param sm Song model
     * @return The next song
     */
    public SongModel getNextSong(SongModel sm) {
        int index = currentSongPos(sm);
        if (index < 0 || index >= items.size() - 1) {
            return null;
        } else {
            return new SongModel(items.get(index + 1));
        }
    }

    /**
     * Returns the current song's position in the list.
     *
     * @param sm Song model
     * @return Position
     */
    private int currentSongPos(SongModel sm) {
        return items.indexOf(sm.getSong());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final SongListModel other = (SongListModel) obj;

        return Objects.equals(this.items, other.items);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public int hashCode() {
        return items.hashCode();
    }

    /**
     * Determine field hashcode.
     *
     * @param field The field
     * @return Hashcode
     */
    private int addHashNum(String field) {
        if (field != null) {
            return field.hashCode();
        }
        return 0;
    }
}
