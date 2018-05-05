package org.ql.audioeditor.logic.dbaccess.adt;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * General table model.
 *
 * @param <E> Row type
 */
public abstract class TableModel<E> extends AbstractTableModel {
    protected final List<String> columnNames = new ArrayList<>();
    protected List<E> entries = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (!entries.isEmpty()) {
            return getValueAt(0, columnIndex).getClass();
        }
        return Object.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        return entries.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Object getValueAt(int rowIndex, int columnIndex);

    /**
     * Return the item selected.
     *
     * @param rowIndex Index of row selected
     * @return The item selected
     */
    public E getRow(int rowIndex) {
        return entries.get(rowIndex);
    }

    /**
     * Add items to list.
     *
     * @param listModel List model
     */
    public void addAll(ListModel<E> listModel) {
        List<E> items = listModel.getItems();
        int firstRow = entries.size();
        entries.addAll(items);
        int lastRow = entries.size() - 1;
        if (firstRow <= lastRow) {
            fireTableRowsInserted(firstRow, lastRow);
        }
    }

    /**
     * Clear rows.
     */
    public void clear() {
        if (entries.size() == 0) {
            return;
        }
        int lastRow = entries.size() - 1;
        entries.clear();
        fireTableRowsDeleted(0, lastRow);
    }
}

