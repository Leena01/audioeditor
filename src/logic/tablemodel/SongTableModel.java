package logic.tablemodel;
import database.entities.Song;

/**
 * Book Table logic
 */
public class SongTableModel extends TableModel<Song> {

    public SongTableModel() {
        columnNames.add("ID");
        columnNames.add("Name");
        columnNames.add("Artist");
        columnNames.add("File path");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String column = columnNames.get(columnIndex);
        Song s = entries.get(rowIndex);
        switch (column) {
            case "ID":
                return s.getId();
            case "Name":
                return s.getName();
            case "Artist":
                return s.getArtist();
            case "File path":
                return s.getPath();
            default:
                throw new IllegalArgumentException(column + "......");
        }
    }
}
