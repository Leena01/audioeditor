package model;
import java.util.*;
import javax.swing.table.AbstractTableModel;

/**
 * General table model
 */
public abstract class TableModel<T> extends AbstractTableModel {
    List<T> entries = new ArrayList<>();
    final List<String> columnNames = new ArrayList<>();

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (!entries.isEmpty()) {
            return getValueAt(0, columnIndex).getClass();
        }
        return Object.class;
    }

    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public abstract Object getValueAt(int rowIndex, int columnIndex);

    /**
     * Return the item selected
     * @param rowIndex Index of row selected
     * @return the item selected
     */
    public T getRow(int rowIndex) {
        return entries.get(rowIndex);
    }

    /**
     * Add items to list
     * @param items rows to add
     */
    public void addAll(List<T> items) {
        int firstRow = entries.size();
        entries.addAll(items);
        int lastRow = entries.size() - 1;
        fireTableRowsInserted(firstRow, lastRow);
    }

    /**
     * Clear rows
     */
    public void clear() {
        if (entries.size() == 0)
            return;
        int lastRow = entries.size() - 1;
        entries.clear();
        fireTableRowsDeleted(0, lastRow);
    }
}
