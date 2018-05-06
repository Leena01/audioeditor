package org.ql.audioeditor.logic.dbaccess;

import org.ql.audioeditor.database.entities.Song;
import org.ql.audioeditor.logic.dbaccess.adt.TableModel;

/**
 * Table model containing elements of type 'Song'.
 */
public final class SongTableModel extends TableModel<Song> {

    /**
     * Constructor.
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * Returns the row index of a given element.
     *
     * @param sm Song model
     * @return Row index
     */
    public int indexOf(SongModel sm) {
        return indexOf(sm.getSong());
    }
}
