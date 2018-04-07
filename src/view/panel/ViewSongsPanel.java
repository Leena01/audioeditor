package view.panel;

import logic.dbaccess.listmodel.SongListModel;
import logic.dbaccess.SongModel;
import logic.dbaccess.tablemodel.SongTableModel;
import view.core.button.TransparentButton;
import view.core.label.Label;
import view.core.table.SongTable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
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
    private JLabel searchLabel;
    private JTextField searchTextField;
    private JPanel innerSearchPanel;
    protected TableRowSorter<SongTableModel> sorter;
    protected JTable table;
    protected JPanel outerSearchPanel;
    protected JScrollPane scrollPane;
    protected SongTableModel tableModel;

    /**
     * Default constructor
     */
    protected ViewSongsPanel() {
        super(new BorderLayout());
        setBackground(Color.BLACK);
        sorter = new TableRowSorter<>();
        searchLabel = new Label("Search: ");
        searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(240, 20));
        searchTextField.setMinimumSize(new Dimension(240, 20));
        searchTextField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = searchTextField.getText();

                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = searchTextField.getText();

                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        innerSearchPanel = new JPanel(new FlowLayout());
        innerSearchPanel.setOpaque(false);
        innerSearchPanel.add(searchLabel);
        innerSearchPanel.add(searchTextField);
        outerSearchPanel = new JPanel(new BorderLayout());
        outerSearchPanel.setBorder(BorderFactory.createEmptyBorder(3, 20, 0, 20));
        outerSearchPanel.setOpaque(false);
        outerSearchPanel.add(innerSearchPanel, BorderLayout.WEST);
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
        sorter.setModel(tableModel);
        table.setRowSorter(sorter);
        scrollPane = new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(6, 20, 10, 20));
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
        add(outerSearchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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
