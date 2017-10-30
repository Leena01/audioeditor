package view;

import model.exceptions.InvalidOperationException;
import model.exceptions.SQLConnectionException;
import util.MatlabHandler;
import view.tables.ViewSongsPanel;
import database.entities.Song;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import static util.Utils.fillColor;
import static util.Utils.showDialog;

public class MainWindow extends JFrame {
    /**
     * Private data members
     */
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private ViewSongsPanel viewSongsPanel;
    private JDialog editFrame;
    private EditPanel editPanel;

    private SongTableModel songTableModel;
    private CardLayout cardLayout;

    private Song currentSong;
    private double totalSamples;
    private double freq;

    private JLabel currentSongName;
    private JLabel currentSongArtist;
    private String lastOpenPath;

    /**
     * Bottom panel
     */
    private JPanel bottomPanel;

    /**
     * Listeners
     */
    private ActionListener backToMenuListener;
    private ActionListener of;
    private ActionListener vs;
    private ActionListener fb;
    private ActionListener ufb;
    private ActionListener cp;
    private ActionListener cf;
    private ActionListener fft;
    private ActionListener as;
    private ActionListener loadSongListener;
    private ActionListener editSongListener;
    private ActionListener deleteSongListener;
    private ActionListener editDoneListener;

    /**
     * Constants
     */
    private final static String SUCCESSFUL_OPERATION = "Successful operation.";
    private final static String MENU_PANEL = "Menu Panel";
    private final static String VIEW_SONGS_PANEL = "View Songs Panel";

    public MainWindow(Model model, MatlabHandler matlabHandler) {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        this.songTableModel = new SongTableModel();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setVisible(true);
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        bottomPanel = new JPanel(new FlowLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                fillColor(g, Color.GRAY, Color.BLACK, getWidth(), getHeight());
            }
        };

        currentSong = null;
        totalSamples = 0.0;
        freq = 0.0;
        Dimension fieldDimension = new Dimension(250, 10);
        currentSongName = new JLabel();
        currentSongName.setPreferredSize(fieldDimension);
        currentSongName.setForeground(Color.WHITE);
        currentSongName.setOpaque(false);
        bottomPanel.add(currentSongName);
        currentSongArtist = new JLabel();
        currentSongArtist.setPreferredSize(fieldDimension);
        currentSongArtist.setForeground(Color.WHITE);
        currentSongArtist.setOpaque(false);
        bottomPanel.add(currentSongArtist);
        lastOpenPath = null;

        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setOpaque(false);

        /**
         * Back to Main Menu
         */
        backToMenuListener = ae -> cardLayout.show(mainPanel, MENU_PANEL);

        /**
         * Open File Button Listener
         */
        of = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    File f = openFile();
                    if (f != null) {
                        lastOpenPath = f.getParent();
                        String p = f.getPath();
                        currentSong = new Song(f.getName(), "-", p);
                        currentSongArtist.setText(currentSong.getArtist());
                        currentSongName.setText(currentSong.getName());
                        // currentSongArtist.setText("Name: " + currentSong.getArtist()); // TODO
                        // currentSongName.setText("Artist: " + currentSong.getName());

                        new Thread(() -> {
                            matlabHandler.openSong(p);
                            totalSamples = matlabHandler.getTotalSamples();
                            freq = matlabHandler.getFreq();
                            System.out.println(totalSamples);
                            SwingUtilities.invokeLater(() -> {
                                menuPanel.setCurrentSong(totalSamples, freq);pack();
                                setMinimumSize(getSize());
                            });
                        }).start();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        /**
         * View Songs Button Listener
         */
        vs = ae -> {
            try {
                viewSongsPanel.setList(model.getSongs());
                cardLayout.show(mainPanel, VIEW_SONGS_PANEL);
            } catch (SQLConnectionException sqe) {
                showDialog(sqe.getMessage());
            }
        };

        /**
         * Favorite Button Listener
         */
        fb = ae -> {
            try {
                int newId = model.addSong(currentSong);
                currentSong.setId(newId);
            } catch(InvalidOperationException | SQLConnectionException ex) {
                showDialog(ex.getMessage());
            }
        };

        /**
         * Unfavorite Button Listener
         */
        ufb = ae -> {
            try {
                model.deleteSong(currentSong);
            } catch(InvalidOperationException | SQLConnectionException ex) {
                showDialog(ex.getMessage());
            }
        };

        /**
         * Change Pitch Button Listener
         */
        cp = ae -> {
            // TODO
        };

        /**
         * Cut File Button Listener
         */
        cf = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // TODO
            }
        };

        /**
         * View Spectrogram Button Listener
         */
        fft = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // TODO
            }
        };

        /**
         * Analyze Song Button Listener
         */
        as = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // TODO
            }
        };

        /**
         * Load Song Button Listener
         */
        loadSongListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // TODO
            }
        };

        /**
         * Edit Song Button Listener
         */
        editSongListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                editPanel.setSelectedSong(viewSongsPanel.getSelected());
                editFrame.setVisible(true);
            }
        };

        /**
         * Delete Song Button Listener
         */
        deleteSongListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    model.deleteSong(viewSongsPanel.getSelected());
                    showDialog(SUCCESSFUL_OPERATION);
                } catch(InvalidOperationException | SQLConnectionException ex) {
                    showDialog(ex.getMessage());
                }
            }
        };

        /**
         * Edit Done Button Listener
         */
        editDoneListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                editFrame.setVisible(false);
                try {
                    model.editSong(editPanel.getSelectedId(), editPanel.getNameField(), editPanel.getArtistField(), editPanel.getSelectedPath());
                    showDialog(SUCCESSFUL_OPERATION);
                } catch(InvalidOperationException | SQLConnectionException ex) {
                    showDialog(ex.getMessage());
                }
                editPanel.clearFields();
            }
        };

        menuPanel = new MenuPanel(matlabHandler, of, vs, fb, ufb, cp, cf, fft, as); // TODO
        viewSongsPanel = new ViewSongsPanel(songTableModel, loadSongListener, editSongListener, deleteSongListener, backToMenuListener); // TODO
        editFrame = new JDialog(this, "Edit song", true);
        editPanel = new EditPanel(editDoneListener);
        editFrame.getContentPane().add(editPanel);
        editFrame.pack();
        editFrame.setVisible(false);

        mainPanel.add(menuPanel, MENU_PANEL);
        mainPanel.add(viewSongsPanel, VIEW_SONGS_PANEL);
    }

    public void run() {
        cardLayout.show(mainPanel, MENU_PANEL);
        pack();
        setMinimumSize(getSize());
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        Dimension d = getSize();
        Dimension m = getMinimumSize();
        if (d.width < m.width || d.height < m.height)
            pack();
        super.paint(g);
    }

    private File openFile() {
        JFileChooser fileChooser;

        if (lastOpenPath != null && !lastOpenPath.equals("")) {
            fileChooser = new JFileChooser(lastOpenPath);
        } else {
            fileChooser = new JFileChooser();
        }

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}
