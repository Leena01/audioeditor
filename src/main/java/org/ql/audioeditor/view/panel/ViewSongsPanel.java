package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.logic.dbaccess.SongListModel;
import org.ql.audioeditor.logic.dbaccess.SongModel;
import org.ql.audioeditor.logic.dbaccess.SongTableModel;
import org.ql.audioeditor.view.core.button.TransparentButton;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.panel.BasicPanel;
import org.ql.audioeditor.view.core.table.SongTable;
import org.ql.audioeditor.view.core.textfield.TextField;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import static org.ql.audioeditor.view.param.Constants.BACK_TO_MAIN_MENU_TEXT;

/**
 * View the list of songs stored in the database.
 */
public class ViewSongsPanel extends BasicPanel {
    private static final Dimension SEARCH_TEXT_FIELD_SIZE =
        new Dimension(240, 20);
    private static final Border OUTER_SEARCH_PANEL_BORDER =
        BorderFactory.createEmptyBorder(3, 20, 0, 20);
    private static final Border SCROLL_PANE_BORDER =
        BorderFactory.createEmptyBorder(6, 20, 10, 20);
    private final JLabel searchLabel;
    private final JTextField searchTextField;
    private final JPanel innerSearchPanel;
    protected TableRowSorter<SongTableModel> sorter;
    protected JTable table;
    protected JPanel outerSearchPanel;
    protected JScrollPane scrollPane;
    protected SongTableModel tableModel;
    protected SongModel selected;
    private JPanel buttonPanel;
    private JPanel songButtonPanel;
    private JPanel importButtonPanel;
    private JButton selectOptionButton;
    private JButton addOptionButton;
    private JButton editOptionButton;
    private JButton deleteOptionButton;
    private JButton backOptionButton;

    /**
     * Constructor.
     */
    protected ViewSongsPanel() {
        super();
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        sorter = new TableRowSorter<>();
        searchLabel = new Label("Search: ");
        searchTextField = new TextField();
        searchTextField.setPreferredSize(SEARCH_TEXT_FIELD_SIZE);
        searchTextField.setMinimumSize(SEARCH_TEXT_FIELD_SIZE);
        searchTextField.getDocument()
            .addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    String text = searchTextField.getText();

                    if (text.trim().length() == 0) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(
                            RowFilter.regexFilter("(?i)" + text));
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    String text = searchTextField.getText();

                    if (text.trim().length() == 0) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(
                            RowFilter.regexFilter("(?i)" + text));
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });

        innerSearchPanel = new JPanel(new FlowLayout());
        innerSearchPanel.setOpaque(false);
        innerSearchPanel.add(searchLabel);
        innerSearchPanel.add(searchTextField);
        outerSearchPanel = new JPanel(new BorderLayout());
        outerSearchPanel.setBorder(OUTER_SEARCH_PANEL_BORDER);
        outerSearchPanel.setOpaque(false);
        outerSearchPanel.add(innerSearchPanel, BorderLayout.WEST);
    }

    /**
     * Constructor.
     *
     * @param tm Table model
     * @param l  SelectOptionButton listener
     * @param a  EditOptionButton listener
     * @param e  AddOptionButton listener
     * @param d  DeleteOptionButton listener
     * @param b  BackOptionButton listener
     */
    public ViewSongsPanel(SongTableModel tm, ActionListener l, ActionListener a,
        ActionListener e, ActionListener d, ActionListener b) {
        this();
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        songButtonPanel = new JPanel(new FlowLayout());
        importButtonPanel = new JPanel(new FlowLayout());

        tableModel = tm;
        selected = new SongModel();
        table = new SongTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter.setModel(tableModel);
        table.setRowSorter(sorter);
        scrollPane = new JScrollPane(this.table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        selectOptionButton = new TransparentButton("Load", l);
        editOptionButton = new TransparentButton("Edit", e);
        addOptionButton = new TransparentButton("Add songs", a);
        deleteOptionButton = new TransparentButton("Delete songs", d);
        backOptionButton = new TransparentButton(BACK_TO_MAIN_MENU_TEXT, b);

        setStyle();
        addPanels();
    }

    /**
     * Sets list.
     *
     * @param slm Song list model
     */
    public void setList(SongListModel slm) {
        int selectedRow = this.table.getSelectedRow();
        initTable(slm);
        this.table.setModel(this.tableModel);
        if (this.table.getRowCount() <= 0) {
            return;
        }
        if (selectedRow >= 0 && selectedRow < this.table.getRowCount()) {
            this.table.setRowSelectionInterval(selectedRow, selectedRow);
        } else {
            this.table.setRowSelectionInterval(0, 0);
        }
    }

    /**
     * Initializes table.
     *
     * @param slm Song list model
     */
    protected final void initTable(SongListModel slm) {
        this.tableModel.clear();
        this.tableModel.addAll(slm);
    }

    /**
     * Getter.
     *
     * @return selected items
     */
    public SongModel getSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }
        selectedRow = table.convertRowIndexToModel(selectedRow);
        selected = new SongModel(tableModel.getRow(selectedRow));
        return selected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setStyle() {
        scrollPane.setBorder(SCROLL_PANE_BORDER);
        buttonPanel.setOpaque(false);
        scrollPane.setOpaque(false);
        songButtonPanel.setOpaque(false);
        importButtonPanel.setOpaque(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPanels() {
        songButtonPanel.add(selectOptionButton);
        songButtonPanel.add(editOptionButton);
        importButtonPanel.add(addOptionButton);
        importButtonPanel.add(deleteOptionButton);
        importButtonPanel.add(backOptionButton);
        buttonPanel.add(songButtonPanel);
        buttonPanel.add(importButtonPanel);
        add(outerSearchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
