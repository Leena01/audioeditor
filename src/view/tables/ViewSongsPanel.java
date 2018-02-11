package view.tables;

import database.entities.Song;
import model.SongTableModel;
import model.TableModel;
import view.decorated.Panel;
import view.decorated.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * View list of books
 */
public class ViewSongsPanel extends Panel {
    private JLabel infoLabel;
    private JPanel buttonPanel;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private TableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton selectButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;

    /**
     * Constructor
     * @param tm Table model
     */
    public ViewSongsPanel(SongTableModel tm, ActionListener l, ActionListener e, ActionListener d, ActionListener b) {
        super();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        infoLabel = new JLabel();
        infoPanel = new JPanel(new FlowLayout());
        buttonPanel = new JPanel(new FlowLayout());

        tableModel = tm;
        table = new JTable(tableModel){
            //Table cell tooltips
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (Exception ignored) { }

                return tip;
            }

        };
        table.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(400, 100));
        selectButton = new Button();
        editButton = new Button();
        deleteButton = new Button();
        backButton = new Button();
        infoLabel.setText("Please choose a song from the list below:");
        infoLabel.setForeground(Color.WHITE);
        selectButton.setText("Load");
        editButton.setText("Edit");
        backButton.setText("Back to Main Menu");
        selectButton.addActionListener(l);
        editButton.addActionListener(e);
        deleteButton.addActionListener(d);
        backButton.addActionListener(b);

        buttonPanel.add(selectButton);
        buttonPanel.add(editButton);
        buttonPanel.add(backButton);
        infoPanel.add(infoLabel);
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        infoPanel.setOpaque(false);

        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 30, 30));
        add(mainPanel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Getter
     * @return Get selected books
     */
    public List<Song> getSelectedRows() {
        if (this.table.getSelectedRow() == -1)
            return null;
        List<Song> books = new ArrayList<>();
        int[] selected = this.table.getSelectedRows();
        for (int i: selected) {
            books.add((Song) this.tableModel.getRow(i));
        }
        return books;
    }

    /**
     * Set list
     * @param items Items to set
     */
    public void setList(List<Song> items) {
        int selectedRow = this.table.getSelectedRow();
        initTable(items);
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
     * @param l List to add
     */
    private void initTable(List<Song> l) {
        this.tableModel.clear();
        this.tableModel.addAll(l);
    }

    /**
     * Getter
     * @return selected items
     */
    public Song getSelected() {
        if (this.table.getSelectedRow() == -1)
            return null;
        return (Song) this.tableModel.getRow(this.table.getSelectedRow());
    }
}
