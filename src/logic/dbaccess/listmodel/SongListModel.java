package logic.dbaccess;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import database.entities.Song;
import logic.dbaccess.adt.ListModel;

import java.io.File;
import java.util.List;

import static util.Constants.DEFAULT;

public class SongListModel extends ListModel<Song> {

    public SongListModel() {
        super();
    }

    public SongListModel(List<File> files) {
        super();
        for (File f: files) {
            String path = f.getPath();
            String title = f.getName();
            String track = DEFAULT;
            String artist = DEFAULT;
            String album = DEFAULT;
            String year = DEFAULT;
            String genre = DEFAULT;
            String comment = DEFAULT;

            try {
                Mp3File song = new Mp3File(path);
                if (song.hasId3v1Tag()) {
                    ID3v1 id3v1Tag = song.getId3v1Tag();
                    title = id3v1Tag.getTitle();
                    track = id3v1Tag.getTrack();
                    artist = id3v1Tag.getArtist();
                    album = id3v1Tag.getAlbum();
                    year = id3v1Tag.getYear();
                    genre = id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")";
                    comment = id3v1Tag.getComment();
                }
            } catch (Exception ignored) { }
            items.add(new Song(title, track, artist, album, year, genre, comment, path));
        }
    }
}
