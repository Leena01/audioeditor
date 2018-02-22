package view.panel;

import database.entities.Song;
import logic.dbaccess.SongModel;
import logic.dbaccess.tablemodel.SongTableModel;
import logic.dbaccess.tablemodel.TableModel;
import org.jetbrains.annotations.NotNull;
import view.element.core.label.Label;
import view.element.core.button.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * View list of songs
 */
public class ViewSongsPanel extends JPanel {
    private static final Dimension TABLE_SIZE = new Dimension(400, 100);

    private JLabel infoLabel;
    private JPanel buttonPanel;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private TableModel<Song> tableModel;
    private SongModel selectedSongModel;
    private JTable table;
    private JScrollPane scrollPane;
    private Button selectOptionButton;
    private Button editOptionButton;
    private Button deleteOptionButton;
    private Button backOptionButton;

    /**
     * Constructor
     * @param tm Table logic
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
        selectedSongModel = new SongModel();
        table = new JTable(tableModel) {
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
        };

        table.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(TABLE_SIZE);
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
    public SongModel getSelectedSongModel() {
        if (table.getSelectedRow() == -1)
            return null;
        selectedSongModel = new SongModel(tableModel.getRow(table.getSelectedRow()));
        return selectedSongModel;
    }
}
