package view;

import static util.Utils.showDialog;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import logic.dbaccess.SongModel;
import logic.dbaccess.DatabaseAccessModel;
import logic.exceptions.InvalidOperationException;
import logic.exceptions.SQLConnectionException;
import logic.matlab.MatlabHandler;
import view.element.core.bar.HorizontalBar;
import view.panel.EditPanel;
import view.panel.MenuPanel;
import view.panel.OptionPanel;
import logic.dbaccess.tablemodel.SongTableModel;
import view.element.core.bar.SideBar;
import view.element.core.window.Window;
import view.element.core.label.Label;
import view.panel.ViewSongsPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class MainWindow extends Window {
    /**
     * Constants
     */
    private static final String IMAGE_NAME = "temp.png";
    private static final String COVER_NAME = "resources/images/default-artwork.png";
    private static final String SUCCESSFUL_OPERATION = "Successful operation.";
    private static final String MENU_PANEL = "Menu Panel";
    private static final String VIEW_SONGS_PANEL = "View Songs Panel";
    private static final String LAST_OPEN_PATH = null;
    private static final String DEFAULT_FIELD = "-";
    private static final int BOTTOM_PANEL_HEIGHT = 12;
    private static final Dimension WIN_MIN_SIZE = new Dimension(800, 400);
    private static final Dimension FIELD_SIZE = new Dimension(250, 10);

    /**
     * Private data members
     */
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private ViewSongsPanel viewSongsPanel;
    private JDialog editFrame;
    private EditPanel editPanel;
    private OptionPanel optionPanel;
    private JPanel sideBar;
    private HorizontalBar bottomPanel;
    private CardLayout cardLayout;

    private SongTableModel songTableModel;
    private SongModel currentSongModel;

    private JLabel currentSongTitle;
    private JLabel currentSongArtist;
    private String lastOpenPath;

    /**
     * Listeners
     */
    private ActionListener backToMenuListener;
    private ActionListener openFileListener;
    private ActionListener viewSongsListener;
    private ActionListener favoriteButtonListener;
    private ActionListener unfavoriteButtonListener;
    private ActionListener changePitchListener;
    private ActionListener cutFileListener;
    private ActionListener viewFFTListener;
    private ActionListener analyzeSongListener;
    private ActionListener loadSongListener;
    private ActionListener editSongListener;
    private ActionListener deleteSongListener;
    private ActionListener editDoneListener;

    public MainWindow(DatabaseAccessModel databaseAccessModel, MatlabHandler matlabHandler) {
        super();
        this.songTableModel = new SongTableModel();
        this.currentSongModel = new SongModel();
        initializeListeners(databaseAccessModel, matlabHandler);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setVisible(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        menuPanel = new MenuPanel(matlabHandler, favoriteButtonListener, unfavoriteButtonListener);
        viewSongsPanel = new ViewSongsPanel(songTableModel, loadSongListener, editSongListener,
                deleteSongListener, backToMenuListener); // TODO

        editFrame = new JDialog(this, "Edit song", true);
        editPanel = new EditPanel(editDoneListener);
        editFrame.getContentPane().add(editPanel);
        editFrame.pack();
        editFrame.setVisible(false);

        optionPanel = new OptionPanel(openFileListener, viewSongsListener, changePitchListener, cutFileListener,
                viewFFTListener, analyzeSongListener);
        sideBar = new SideBar();
        sideBar.add(optionPanel);

        bottomPanel = new HorizontalBar();

        Dimension fieldDimension = FIELD_SIZE;
        currentSongTitle = new Label();
        currentSongTitle.setPreferredSize(fieldDimension);
        currentSongTitle.setOpaque(false);
        bottomPanel.add(currentSongTitle);
        currentSongArtist = new Label();
        currentSongArtist.setPreferredSize(fieldDimension);
        currentSongArtist.setOpaque(false);
        bottomPanel.add(currentSongArtist);
        lastOpenPath = LAST_OPEN_PATH;

        mainPanel.add(menuPanel, MENU_PANEL);
        mainPanel.add(viewSongsPanel, VIEW_SONGS_PANEL);
        add(sideBar, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setHeight(BOTTOM_PANEL_HEIGHT);
    }

    @Override
    public void paint(Graphics g) {
        Dimension d = getSize();
        Dimension m = getMinimumSize();
        if (d.width < m.width || d.height < m.height)
            pack();
        super.paint(g);
    }

    public void run() {
        cardLayout.show(mainPanel, MENU_PANEL);
        // pack();
        setMinimumSize(WIN_MIN_SIZE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private File openFile() {
        JFileChooser fileChooser;

        if (lastOpenPath != null && !lastOpenPath.equals(""))
            fileChooser = new JFileChooser(lastOpenPath);
        else
            fileChooser = new JFileChooser();

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile();
        return null;
    }

    private void initializeListeners(DatabaseAccessModel databaseAccessModel, MatlabHandler matlabHandler) {
        backToMenuListener = ae -> cardLayout.show(mainPanel, MENU_PANEL);

        openFileListener = ae -> {
            try {
                File f = openFile();
                if (f != null) {
                        lastOpenPath = f.getParent();
                        String path = f.getPath();
                        BufferedImage cover = ImageIO.read(new File(COVER_NAME));
                        String title = f.getName();
                        String track = DEFAULT_FIELD;
                        String artist = DEFAULT_FIELD;
                        String album = DEFAULT_FIELD;
                        String year = DEFAULT_FIELD;
                        String genre = DEFAULT_FIELD;
                        String comment = DEFAULT_FIELD;
                        try {
                            Mp3File song = new Mp3File(path);
                            if (song.hasId3v2Tag()) {
                                ID3v2 id3v2tag = song.getId3v2Tag();
                                byte[] imageData = id3v2tag.getAlbumImage();
                                cover = ImageIO.read(new ByteArrayInputStream(imageData));
                            }
                            if (song.hasId3v1Tag()) {
                                ID3v1 id3v1Tag = song.getId3v1Tag();
                                title = id3v1Tag.getTitle();
                                track = id3v1Tag.getTrack();
                                artist = id3v1Tag.getArtist();
                                album = id3v1Tag.getAlbum();
                                year = id3v1Tag.getYear();
                                genre = id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")";
                                comment = id3v1Tag.getComment();
                            }
                        } catch (Exception ignored) {}
                        currentSongModel = new SongModel(title, track, artist, album, year, genre, comment, path);
                        currentSongArtist.setText(currentSongModel.getArtist());
                        currentSongTitle.setText(currentSongModel.getTitle());
                        final BufferedImage artwork = cover;

                    new Thread(() -> {
                        matlabHandler.openSong(path);
                        matlabHandler.plotSong(IMAGE_NAME);
                        matlabHandler.passData(currentSongModel);
                        SwingUtilities.invokeLater(() -> {
                            try {
                                BufferedImage plot = ImageIO.read(new File(IMAGE_NAME));
                                menuPanel.setCurrentSong(currentSongModel.getTotalSamples(),
                                        currentSongModel.getFreq(), plot, artwork);
                                optionPanel.showOptions(true);
                                cardLayout.show(mainPanel, MENU_PANEL);
                            } catch  (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        });
                    }).start();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        viewSongsListener = ae -> {
            try {
                viewSongsPanel.setList(databaseAccessModel.getSongs());
                cardLayout.show(mainPanel, VIEW_SONGS_PANEL);
            } catch (SQLConnectionException sqe) {
                showDialog(sqe.getMessage());
            }
        };

        favoriteButtonListener = ae -> {
            try {
                databaseAccessModel.addSong(currentSongModel);
            } catch(InvalidOperationException | SQLConnectionException ex) {
                showDialog(ex.getMessage());
            }
        };

        unfavoriteButtonListener = ae -> {
            try {
                databaseAccessModel.deleteSong(currentSongModel);
            } catch(InvalidOperationException | SQLConnectionException ex) {
                showDialog(ex.getMessage());
            }
        };

        changePitchListener = ae -> {
            // TODO
        };

        cutFileListener = ae -> {
            // TODO
        };

        viewFFTListener = ae -> {
            // TODO
        };

        analyzeSongListener = ae -> {
            // TODO
        };

        loadSongListener = ae -> {
            // TODO
        };

        editSongListener = ae -> {
            editPanel.setSelectedSong(viewSongsPanel.getSelectedSongModel());
            editFrame.setVisible(true);
        };

        deleteSongListener = ae -> {
            try {
                databaseAccessModel.deleteSong(viewSongsPanel.getSelectedSongModel());
                showDialog(SUCCESSFUL_OPERATION);
            } catch(InvalidOperationException | SQLConnectionException ex) {
                showDialog(ex.getMessage());
            }
        };

        editDoneListener = ae -> {
            editFrame.setVisible(false);
            try {
                databaseAccessModel.editSong(editPanel.getSelectedSongModel());
                showDialog(SUCCESSFUL_OPERATION);
            } catch(InvalidOperationException | SQLConnectionException ex) {
                showDialog(ex.getMessage());
            }
            editPanel.clearFields();
        };
    }
}
