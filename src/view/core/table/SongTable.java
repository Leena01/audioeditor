package view.core.table;

import logic.dbaccess.adt.TableModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class SongTable extends JTable {
    public SongTable(TableModel tm) {
        super(tm);
        setAutoCreateRowSorter(true);
        setFillsViewportHeight(true);
        setRowSelectionAllowed(true);
    }

    /**
     * Table cell tooltips
     */
    public String getToolTipText(@NotNull MouseEvent e) {
        String tip = null;
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        try {
            tip = getValueAt(rowIndex, colIndex).toString();
        } catch (Exception ignored) { }
        return tip;
    }
}
