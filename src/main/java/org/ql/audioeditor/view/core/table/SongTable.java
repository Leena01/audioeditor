package org.ql.audioeditor.view.core.table;

import org.jetbrains.annotations.NotNull;
import org.ql.audioeditor.logic.dbaccess.adt.TableModel;

import javax.swing.JTable;
import java.awt.event.MouseEvent;

/**
 * Table with entries of type 'Song'.
 */
public class SongTable extends JTable {
    public SongTable(TableModel tm) {
        super(tm);
        setAutoCreateRowSorter(true);
        setFillsViewportHeight(true);
        setRowSelectionAllowed(true);
    }

    /**
     * Table cell tooltips.
     */
    @Override
    public String getToolTipText(@NotNull MouseEvent e) {
        String tip = null;
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        try {
            tip = getValueAt(rowIndex, colIndex).toString();
        } catch (Exception ignored) {
        }
        return tip;
    }
}
