package org.ql.audioeditor.view.panel.popup;

import org.ql.audioeditor.database.entities.Song;
import org.ql.audioeditor.logic.dbaccess.SongListModel;
import org.ql.audioeditor.logic.dbaccess.SongTableModel;
import org.ql.audioeditor.view.core.button.TransparentButton;
import org.ql.audioeditor.view.core.table.SongTable;
import org.ql.audioeditor.view.panel.ViewSongsPanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for user-induced song deletion.
 */
public final class DeleteSongsPanel extends ViewSongsPanel {
    private static final Border SCROLL_PANE_BORDER =
        BorderFactory.createEmptyBorder(15, 20, 10, 20);
    private final SongListModel selectedSongs;
    private final JPanel buttonPanel;
    private final JButton doneButton;

    /**
     * Constructor.
     *
     * @param tm Table model
     * @param d  doneButton listener
     */
    public DeleteSongsPanel(SongTableModel tm, ActionListener d) {
        super();
        buttonPanel = new JPanel(new FlowLayout());

        tableModel = tm;
        selectedSongs = new SongListModel();
        table = new SongTable(tableModel);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        sorter.setModel(tableModel);
        table.setRowSorter(sorter);
        scrollPane = new JScrollPane(this.table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        doneButton = new TransparentButton("Delete", d);

        setStyle();
        addPanels();
    }

    /**
     * Sets list.
     *
     * @param slm Song list model
     */
    @Override
    public void setList(SongListModel slm) {
        int[] selectedRows = this.table.getSelectedRows();
        initTable(slm);
        this.table.setModel(this.tableModel);
        if (this.table.getRowCount() <= 0) {
            return;
        }
        if (selectedRows.length != 0) {
            for (int i : selectedRows) {
                this.table.addRowSelectionInterval(i, i);
            }
        }
    }

    /**
     * Returns selected songs.
     *
     * @return Selected songs
     */
    public SongListModel getSelectedRows() {
        int[] selectedRows = this.table.getSelectedRows();
        if (selectedRows.length == 0) {
            return null;
        }
        List<Song> songs = new ArrayList<>();

        for (int i : selectedRows) {
            int selectedRow = this.table.convertRowIndexToModel(i);
            songs.add(this.tableModel.getRow(selectedRow));
        }
        selectedSongs.setSongs(songs);
        return selectedSongs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setStyle() {
        scrollPane.setBorder(SCROLL_PANE_BORDER);
        scrollPane.setOpaque(false);
        buttonPanel.setOpaque(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPanels() {
        buttonPanel.add(doneButton);
        add(outerSearchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
