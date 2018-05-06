package org.ql.audioeditor.view.panel.popup;

import org.ql.audioeditor.logic.dbaccess.SongModel;
import org.ql.audioeditor.logic.dbaccess.SongTableModel;
import org.ql.audioeditor.view.core.button.TransparentButton;
import org.ql.audioeditor.view.core.table.SongTable;
import org.ql.audioeditor.view.panel.ViewSongsPanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

/**
 * Panel for showing related songs.
 */
public final class SimilarSongsPanel extends ViewSongsPanel {
    private static final Border HIGHLIGHT = new MatteBorder(1, 0, 1, 0,
        new Color(22, 145, 217));
    private static final Border SCROLL_PANE_BORDER =
        BorderFactory.createEmptyBorder(15, 20, 10, 20);
    private final JPanel buttonPanel;
    private final JButton doneButton;
    private int currentSongIndex;

    /**
     * Constructor.
     *
     * @param tm Table model
     * @param l  DoneButton listener
     */
    public SimilarSongsPanel(SongTableModel tm, ActionListener l) {
        super();
        currentSongIndex = -1;
        buttonPanel = new JPanel(new FlowLayout());

        tableModel = tm;
        selected = new SongModel();
        table = new SongTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer,
                int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent) c;
                if (row == currentSongIndex) {
                    jc.setBorder(HIGHLIGHT);
                }

                return c;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setEnabled(false);
        scrollPane = new JScrollPane(this.table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        doneButton = new TransparentButton("Load", l);
        outerSearchPanel.setVisible(false);
        setStyle();
        addPanels();
    }

    /**
     * Sets the current song's row index.
     * @param index Row index
     */
    public void setCurrentSongIndex(int index) {
        currentSongIndex = index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setStyle() {
        scrollPane.setBorder(SCROLL_PANE_BORDER);
        scrollPane.setOpaque(false);
        buttonPanel.setOpaque(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPanels() {
        buttonPanel.add(doneButton);
        add(outerSearchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
