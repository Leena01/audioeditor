package view.panel;

import logic.dbaccess.listmodel.SongListModel;
import logic.dbaccess.SongModel;
import logic.dbaccess.tablemodel.SongTableModel;
import view.core.button.TransparentButton;
import view.core.table.SongTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * View list of songs
 */
public class ViewSongsPanel extends JPanel {
    private JPanel buttonPanel;
    private JPanel songButtonPanel;
    private JPanel importButtonPanel;
    private SongModel selected;
    private JButton selectOptionButton;
    private JButton addOptionButton;
    private JButton editOptionButton;
    private JButton deleteOptionButton;
    private JButton backOptionButton;
    public JTable table;
    protected JScrollPane scrollPane;
    protected SongTableModel tableModel;

    /**
     * Default constructor
     */
    protected ViewSongsPanel() {
        super();
        setBackground(Color.BLACK);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    /**
     * Constructor
     * @param tm Table logic
     */
    public ViewSongsPanel(SongTableModel tm, ActionListener l, ActionListener a, ActionListener e, ActionListener d, ActionListener b) {
        this();
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.setOpaque(false);
        songButtonPanel = new JPanel(new FlowLayout());
        importButtonPanel = new JPanel(new FlowLayout());

        tableModel = tm;
        selected = new SongModel();
        table = new SongTable(tableModel);
        scrollPane = new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        scrollPane.setOpaque(false);
        selectOptionButton = new TransparentButton("Load", l);
        editOptionButton = new TransparentButton("Edit", e);
        addOptionButton = new TransparentButton("Add songs", a);
        deleteOptionButton = new TransparentButton("Delete songs", d);
        backOptionButton = new TransparentButton("Back to Main Menu", b);

        songButtonPanel.add(selectOptionButton);
        songButtonPanel.add(editOptionButton);
        importButtonPanel.add(addOptionButton);
        importButtonPanel.add(deleteOptionButton);
        importButtonPanel.add(backOptionButton);
        songButtonPanel.setOpaque(false);
        importButtonPanel.setOpaque(false);

        buttonPanel.add(songButtonPanel);
        buttonPanel.add(importButtonPanel);
        add(scrollPane);
        add(buttonPanel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Set list
     * @param slm Song list model
     */
    public void setList(SongListModel slm) {
        int selectedRow = this.table.getSelectedRow();
        initTable(slm);
        this.table.setModel(this.tableModel);
        if (this.table.getRowCount() <= 0)
            return;
        if (selectedRow >= 0 && selectedRow < this.table.getRowCount())
            this.table.setRowSelectionInterval(selectedRow, selectedRow);
        else
            this.table.setRowSelectionInterval(0, 0);
    }

    /**
     * Initialize table
     * @param slm Song list model
     */
    private void initTable(SongListModel slm) {
        this.tableModel.clear();
        this.tableModel.addAll(slm);
    }

    /**
     * Getter
     * @return selected items
     */
    public SongModel getSelectedRow() {
        if (table.getSelectedRow() == -1)
            return null;
        selected = new SongModel(tableModel.getRow(table.getSelectedRow()));
        return selected;
    }
}
