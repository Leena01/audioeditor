package view.panel.popup;

import database.entities.Song;
import logic.dbaccess.listmodel.SongListModel;
import logic.dbaccess.tablemodel.SongTableModel;
import view.core.button.TransparentButton;
import view.core.table.SongTable;
import view.panel.ViewSongsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public final class DeleteSongsPanel extends ViewSongsPanel {
    private SongListModel selectedSongs;
    private JPanel buttonPanel;
    private JButton doneButton;

    public DeleteSongsPanel(SongTableModel tm, ActionListener d) {
        super();
        buttonPanel = new JPanel(new FlowLayout());
        tableModel = tm;
        selectedSongs = new SongListModel();
        table = new SongTable(tableModel);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        sorter.setModel(tableModel);
        table.setRowSorter(sorter);
        scrollPane = new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        doneButton = new TransparentButton("Delete", d);
        setStyle();
        addPanels();
    }

    public SongListModel getSelectedRows() {
        if (this.table.getSelectedRow() == -1)
            return null;
        List<Song> songs = new ArrayList<>();
        int[] selected = this.table.getSelectedRows();
        for (int i: selected) {
            songs.add(this.tableModel.getRow(i));
        }
        selectedSongs.setSongs(songs);
        return selectedSongs;
    }

    @Override
    protected void setStyle() {
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        scrollPane.setOpaque(false);
        buttonPanel.setOpaque(false);
    }

    @Override
    protected void addPanels() {
        buttonPanel.add(doneButton);
        add(outerSearchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
