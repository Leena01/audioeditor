package view.tables;

import database.entities.Song;
import model.SongTableModel;
import model.TableModel;
import view.decorated.Label;
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
public class ViewSongsPanel extends JPanel {
    private JLabel infoLabel;
    private JPanel buttonPanel;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private TableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private Button selectOptionButton;
    private Button editOptionButton;
    private Button deleteOptionButton;
    private Button backOptionButton;

    /**
     * Constructor
     * @param tm Table model
     */
    public ViewSongsPanel(SongTableModel tm, ActionListener l, ActionListener e, ActionListener d, ActionListener b) {
        super();
        setBackground(Color.BLACK);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        infoLabel = new Label();
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
        selectOptionButton = new Button();
        selectOptionButton.addMouseListener();
        editOptionButton = new Button();
        editOptionButton.addMouseListener();
        deleteOptionButton = new Button();
        deleteOptionButton.addMouseListener();
        backOptionButton = new Button();
        backOptionButton.addMouseListener();
        infoLabel.setText("Please choose a song from the list below:");
        selectOptionButton.setText("Load");
        editOptionButton.setText("Edit");
        backOptionButton.setText("Back to Main Menu");
        selectOptionButton.addActionListener(l);
        editOptionButton.addActionListener(e);
        deleteOptionButton.addActionListener(d);
        backOptionButton.addActionListener(b);

        buttonPanel.add(selectOptionButton);
        buttonPanel.add(editOptionButton);
        buttonPanel.add(backOptionButton);
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
