package org.ql.audioeditor.logic.dbaccess.tablemodel;
import org.ql.audioeditor.database.entities.Song;
import org.ql.audioeditor.logic.dbaccess.adt.TableModel;

/**
 * Book Table DatabaseAccessModel
 */
public class SongTableModel extends TableModel<Song> {

    public SongTableModel() {
        columnNames.add("ID");
        columnNames.add("Title");
        columnNames.add("Track");
        columnNames.add("Artist");
        columnNames.add("Album");
        columnNames.add("Year");
        columnNames.add("Genre");
        columnNames.add("Comment");
        columnNames.add("Path");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String column = columnNames.get(columnIndex);
        Song s = entries.get(rowIndex);
        switch (column) {
            case "ID":
                return s.getId();
            case "Title":
                return s.getTitle();
            case "Track":
                return s.getTrack();
            case "Artist":
                return s.getArtist();
            case "Album":
                return s.getAlbum();
            case "Year":
                return s.getYear();
            case "Genre":
                return s.getGenre();
            case "Comment":
                return s.getComment();
            case "Path":
                return s.getPath();
            default:
                throw new IllegalArgumentException(column + "......");
        }
    }
}
