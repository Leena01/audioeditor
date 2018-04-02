package view.panel;

import database.entities.Song;
import logic.dbaccess.listmodel.SongListModel;

import java.util.ArrayList;
import java.util.List;

public class DeleteSongsPanel extends ViewSongsPanel {

    SongListModel getSelectedRows() {
        if (this.table.getSelectedRow() == -1)
            return null;
        List<Song> songs = new ArrayList<>();
        int[] selected = this.table.getSelectedRows();
        for (int i: selected) {
            songs.add((Song) this.tableModel.getRow(i));
        }
        return new SongListModel(songs);
    }
}
