package view;

import static util.Constants.DEFAULT;
import static util.Constants.DEFAULT_SONG_ID;
import static util.Constants.REFRESH_MILLIS;
import static util.Utils.showDialog;
import static view.panel.analysis.SpectrogramPanel.DIGIT_SIZE_MIN;
import static view.util.Constants.*;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import logic.dbaccess.SongModel;
import logic.dbaccess.DatabaseAccessModel;
import logic.exceptions.InvalidOperationException;
import logic.exceptions.MatlabEngineException;
import logic.exceptions.SQLConnectionException;
import logic.matlab.MatlabHandler;
import view.core.bar.HorizontalBar;
import view.core.listener.EmptyMouseListener;
import view.panel.*;
import logic.dbaccess.tablemodel.SongTableModel;
import view.core.bar.SideBar;
import view.core.window.Window;
import view.core.label.Label;
import view.panel.analysis.AnalysisPanel;
import view.panel.analysis.SpectrogramPanel;

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
    private static final String SAVE_QUESTION = "Do you want to save the new file?";
    private static final String FILE_BROWSER_INFO = "Specify the directory and the file name";
    private static final String COVER_ERROR = "Could not load cover.";
    private static final String WRONG_INPUT_ERROR = "Wrong input data.";
    private static final String SONGS_DELETED_INFO = "Songs deleted.";
    private static final String SUCCESSFUL_OPERATION_INFO = "Successful operation.";
    private static final String MENU_PANEL = "Menu Panel";
    private static final String VIEW_SONGS_PANEL = "View Songs Panel";
    private static final String DATA_PANEL = "Data Panel";
    private static final String ANALYSIS_PANEL = "Analysis Panel";
    private static final String SPECTROGRAM_PANEL = "Spectrogram Panel";
    private static final String CUT_SONG_PANEL = "Cut Song Panel";
    private static final int INFO_LABEL_DELAY = 5000;

    /**
     * Private data members
     */
    private SongTableModel songTableModel;
    private SongModel currentSongModel;

    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private ViewSongsPanel viewSongsPanel;
    private AnalysisPanel analysisPanel;
    private SpectrogramPanel spectrogramPanel;
    private JDialog editFrame;
    private EditPanel editPanel;
    private DataPanel dataPanel;
    private CutSongPanel cutSongPanel;
    private OptionPanel optionPanel;
    private JPanel sideBar;
    private HorizontalBar bottomPanel;
    private CardLayout cardLayout;

    private JLabel currentSongTitle;
    private JLabel infoLabel;
    private String path;
    private String lastOpenDir;
    private BufferedImage plot;

    /**
     * Listeners
     */
    private MouseListener emptyMouseListener;
    private ActionListener backToMenuListener;
    private ActionListener openFileListener;
    private ActionListener viewSongsListener;
    private ActionListener showDataListener;
    private ActionListener favoriteButtonListener;
    private ActionListener unfavoriteButtonListener;
    private ActionListener changePitchListener;
    private ActionListener cutFileListener;
    private ActionListener cutDoneListener;
    private ActionListener viewSpecListener;
    private ActionListener showSpecListener;
    private ActionListener analyzeSongListener;
    private ActionListener loadSongListener;
    private ActionListener addSongListener;
    private ActionListener editSongListener;
    private ActionListener deleteSongListener;
    private ActionListener editDoneListener;

    public MainWindow(DatabaseAccessModel databaseAccessModel, MatlabHandler matlabHandler) {
        super();
        this.songTableModel = new SongTableModel();
        this.currentSongModel = new SongModel();
        initializeListeners(databaseAccessModel, matlabHandler);

        getGlassPane().addMouseListener(emptyMouseListener);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setVisible(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        menuPanel = new MenuPanel(matlabHandler, getGlassPane(), favoriteButtonListener, unfavoriteButtonListener);
        viewSongsPanel = new ViewSongsPanel(songTableModel, loadSongListener, addSongListener, editSongListener,
                deleteSongListener, backToMenuListener); // TODO

        editPanel = new EditPanel(editDoneListener);
        editFrame = new JDialog(this, "Edit song", true);
        editFrame.getContentPane().add(editPanel);
        editFrame.pack();
        editFrame.setVisible(false);

        dataPanel = new DataPanel(currentSongModel, backToMenuListener);
        cutSongPanel = new CutSongPanel(cutDoneListener, backToMenuListener);

        optionPanel = new OptionPanel(backToMenuListener, openFileListener, viewSongsListener, showDataListener, changePitchListener,
                cutFileListener, viewSpecListener, analyzeSongListener);
        sideBar = new SideBar();
        sideBar.add(optionPanel);

        bottomPanel = new HorizontalBar();

        analysisPanel = new AnalysisPanel(matlabHandler);

        spectrogramPanel = new SpectrogramPanel(showSpecListener, backToMenuListener);

        currentSongTitle = new Label();
        currentSongTitle.setSize(BOTTOM_FIELD_SIZE);
        currentSongTitle.setOpaque(false);
        currentSongTitle.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        bottomPanel.add(currentSongTitle, BorderLayout.WEST);
        infoLabel = new Label();
        infoLabel.setSize(BOTTOM_FIELD_SIZE);
        infoLabel.setOpaque(false);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        bottomPanel.add(infoLabel, BorderLayout.EAST);

        path = currentSongModel.getPath();
        int pos = Math.max(path.lastIndexOf("\\"),
                path.lastIndexOf("/"));
        if (pos != -1)
            lastOpenDir = path.substring(pos);
        else
            lastOpenDir = path;
        getSaved(databaseAccessModel);

        mainPanel.add(menuPanel, MENU_PANEL);
        mainPanel.add(viewSongsPanel, VIEW_SONGS_PANEL);
        mainPanel.add(dataPanel, DATA_PANEL);
        mainPanel.add(analysisPanel, ANALYSIS_PANEL);
        mainPanel.add(spectrogramPanel, SPECTROGRAM_PANEL);
        mainPanel.add(cutSongPanel, CUT_SONG_PANEL);
        add(sideBar, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setHeight(BOTTOM_PANEL_HEIGHT);

        Timer t = new Timer(REFRESH_MILLIS, e -> refreshCache(databaseAccessModel));
        t.setRepeats(true);
        t.start();
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
        if (currentSongModel.getId() == -1)
            optionPanel.showOptions(false);
        // pack();
        setMinimumSize(WIN_MIN_SIZE);
        setSize(WIN_MIN_SIZE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private File openFile() {
        JFileChooser fileChooser;

        if (lastOpenDir != null && !lastOpenDir.equals(""))
            fileChooser = new JFileChooser(lastOpenDir);
        else
            fileChooser = new JFileChooser();

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile();
        return null;
    }

    protected void hideImage(boolean isHidden) {
        if (currentSongModel.getId() != -1) {
            menuPanel.hideImage(isHidden);
            spectrogramPanel.hideImage(isHidden);
        }
    }

    protected void maximizeImage(boolean isMaximized) {
        menuPanel.maximizeImage(isMaximized);
        spectrogramPanel.maximizeImage(isMaximized);
        cutSongPanel.maximizeImage(isMaximized);
    }

    private void initializeListeners(DatabaseAccessModel databaseAccessModel, MatlabHandler matlabHandler) {
        backToMenuListener = ae -> cardLayout.show(mainPanel, MENU_PANEL);

        openFileListener = ae -> {
            File f = openFile();
            if (f != null) {
                    lastOpenDir = f.getParent();
                    path = f.getPath();
                    String title = f.getName();
                    final Image cover = getTags(title);
                    currentSongTitle.setText(currentSongModel.getTitle());
                    getSaved(databaseAccessModel);

                new Thread(() -> {
                    getGlassPane().setVisible(true);
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    try {
                        matlabHandler.openSong(path);
                        matlabHandler.plotSong(PLOT_IMAGE_NAME);
                        matlabHandler.passData(currentSongModel);
                        SwingUtilities.invokeLater(() -> {
                            try {
                                plot = ImageIO.read(new File(PLOT_IMAGE_NAME));
                                menuPanel.setCurrentSong(currentSongModel.getTotalSamples(),
                                        currentSongModel.getFreq(), plot, cover, isNormal,
                                        getExtendedState() == MAXIMIZED_BOTH);
                            } catch (IOException ignored) { }
                            optionPanel.showOptions(true);
                            cardLayout.show(mainPanel, MENU_PANEL);
                        });
                    } catch (MatlabEngineException mee) {
                        showDialog(mee.getMessage());
                    } finally {
                        setCursor(Cursor.getDefaultCursor());
                        getGlassPane().setVisible(false);
                    }
                }).start();
            }
        };

        viewSongsListener = ae -> {
            if (currentSongModel.getId() != -1)
                menuPanel.pauseSong();
            cardLayout.show(mainPanel, VIEW_SONGS_PANEL);
        };

        showDataListener = ae -> {
            if (currentSongModel.getId() != -1)
                menuPanel.pauseSong();
            cardLayout.show(mainPanel, DATA_PANEL);
        };

        favoriteButtonListener = ae -> {
            try {
                databaseAccessModel.addSong(currentSongModel);
                setFavorite(true);
            } catch(InvalidOperationException | SQLConnectionException ex) {
                showDialog(ex.getMessage());
            }
        };

        unfavoriteButtonListener = ae -> {
            try {
                databaseAccessModel.deleteSong(currentSongModel);
                setFavorite(false);
            } catch(InvalidOperationException | SQLConnectionException ex) {
                showDialog(ex.getMessage());
            }
        };

        changePitchListener = ae -> {
            // TODO
        };

        cutFileListener = ae -> {
            if (currentSongModel.getId() != -1)
                menuPanel.pauseSong();
            cutSongPanel.setCurrentSong(currentSongModel.getTotalSamples(), currentSongModel.getFreq(),
                    plot, getExtendedState() == MAXIMIZED_BOTH);
            cardLayout.show(mainPanel, CUT_SONG_PANEL);
        };

        cutDoneListener = e -> {
            new Thread(() -> {
                getGlassPane().setVisible(true);
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                matlabHandler.cutSong(cutSongPanel.getFrom(), cutSongPanel.getTo());
                int result = JOptionPane.showConfirmDialog(null,
                        SAVE_QUESTION, null, JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle(FILE_BROWSER_INFO);
                    int userSelection = fileChooser.showSaveDialog(this);
                    try {
                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            File fileToSave = fileChooser.getSelectedFile();
                            matlabHandler.saveSong(fileToSave.getAbsolutePath());
                            // TODO check if path already exists
                        }
                    } catch (MatlabEngineException mee) {
                        showDialog(mee.getMessage());
                    }
                }
                SwingUtilities.invokeLater(() -> {
                    getGlassPane().setVisible(false);
                    setCursor(Cursor.getDefaultCursor());
                });
            }).start();
        };

        viewSpecListener = ae -> {
            if (currentSongModel.getId() != -1)
                menuPanel.pauseSong();
            cardLayout.show(mainPanel, SPECTROGRAM_PANEL);
        };

        showSpecListener = ae -> {
            String windowSizeString = spectrogramPanel.getWindowSize();
            String hopSizeString = spectrogramPanel.getHopSize();
            String nfftString = spectrogramPanel.getNfft();
            String window = spectrogramPanel.getWindow();
            double windowSize = (!windowSizeString.equals("") ? Double.parseDouble(windowSizeString) : 0.0);
            double hopSize = (!hopSizeString.equals("") ? Double.parseDouble(hopSizeString) : 0.0);
            double nfft = (!nfftString.equals("") ? Double.parseDouble(nfftString) : 0.0);
            if (windowSize < Math.pow(10, DIGIT_SIZE_MIN - 1) ||
                    hopSize < Math.pow(10, DIGIT_SIZE_MIN - 1) ||
                    nfft < Math.pow(10, DIGIT_SIZE_MIN - 1) ||
                    windowSize <= hopSize) {
                infoLabel.setText(WRONG_INPUT_ERROR);
                Timer t = new Timer(INFO_LABEL_DELAY, e -> infoLabel.setText(null));
                t.setRepeats(false);
                t.start();
            }
            else {
                new Thread(() -> {
                    getGlassPane().setVisible(true);
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    try {
                        matlabHandler.showSpectrogram(windowSize, hopSize, nfft, window,
                            SPEC_IMAGE_NAME, SPEC_3D_IMAGE_NAME);
                        SwingUtilities.invokeLater(() -> {
                            getGlassPane().setVisible(false);
                            setCursor(Cursor.getDefaultCursor());
                            try {
                                Image spec = ImageIO.read(new File(SPEC_IMAGE_NAME));
                                Image spec3d = ImageIO.read(new File(SPEC_3D_IMAGE_NAME));
                                spectrogramPanel.changeImage(spec, spec3d, isNormal,
                                        getExtendedState() == MAXIMIZED_BOTH);
                            } catch (IOException ignored) { }
                        });
                    } catch (MatlabEngineException mee) {
                        showDialog(mee.getMessage());
                    } finally {
                        setCursor(Cursor.getDefaultCursor());
                        getGlassPane().setVisible(false);
                    }
                }).start();
            }
        };

        analyzeSongListener = ae -> {
            if (currentSongModel.getId() != -1)
                menuPanel.pauseSong();
            cardLayout.show(mainPanel, ANALYSIS_PANEL);
            // TODO
        };

        loadSongListener = ae -> {
            // TODO
        };

        addSongListener = ae -> {
            // TODO
        };

        editSongListener = ae -> {
            SongModel sm = viewSongsPanel.getSelectedSongModel();
            if (sm != null) {
                editPanel.setSelectedSong(sm);
                editFrame.setVisible(true);
            }
        };

        deleteSongListener = ae -> {
            try {
                databaseAccessModel.deleteSong(viewSongsPanel.getSelectedSongModel());
                showDialog(SUCCESSFUL_OPERATION_INFO);
            } catch(InvalidOperationException | SQLConnectionException ex) {
                showDialog(ex.getMessage());
            }
        };

        editDoneListener = ae -> {
            editFrame.setVisible(false);
            try {
                editPanel.setNewData();
                SongModel sm = editPanel.getSelectedSongModel();
                setTags(sm);
                databaseAccessModel.editSong(editPanel.getSelectedSongModel());
                showDialog(SUCCESSFUL_OPERATION_INFO);
            } catch(InvalidOperationException | SQLConnectionException ex) {
                showDialog(ex.getMessage());
            }
            editPanel.clearFields();
        };

        emptyMouseListener = new EmptyMouseListener();
    }

    private void getSaved(DatabaseAccessModel databaseAccessModel) {
        try {
            int id = databaseAccessModel.getId(currentSongModel);
            if (id != DEFAULT_SONG_ID) {
                menuPanel.setFavorite(true);
                currentSongModel.setId(id);
            }
            else
                menuPanel.setFavorite(false);
        } catch (SQLConnectionException e) {
            showDialog(e.getMessage());
            menuPanel.setFavorite(false);
        }
    }

    private void refreshCache(DatabaseAccessModel databaseAccessModel) {
        dataPanel.setSongData(currentSongModel);
        try {
            viewSongsPanel.setList(databaseAccessModel.getSongs());
            if (databaseAccessModel.hasInvalid()) {
                infoLabel.setText(SONGS_DELETED_INFO);
                Timer t = new Timer(INFO_LABEL_DELAY, e -> infoLabel.setText(null));
                t.setRepeats(false);
                t.start();
            }
            if (!databaseAccessModel.isSongValid(currentSongModel)) {
                setFavorite(false);
                currentSongModel.setPath(DEFAULT);
            }
        } catch (SQLConnectionException sqe) {
            showDialog(sqe.getMessage());
        }
    }

    private Image getTags(String title) {
        try {
            Image cover = ImageIO.read(new File(COVER_NAME));
            String track = DEFAULT;
            String artist = DEFAULT;
            String album = DEFAULT;
            String year = DEFAULT;
            String genre = DEFAULT;
            String comment = DEFAULT;
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
                currentSongModel = new SongModel(title, track, artist, album, year, genre, comment, path);
                return cover;
            } catch (Exception ignored) { }
            return cover;
        } catch (Exception ex) {
            showDialog(COVER_ERROR);
        }
        return null;
    }

    private void setTags(SongModel sm) {
        try {
            Mp3File song = new Mp3File(sm.getPath());
            ID3v1 id3v1Tag;
            if (song.hasId3v1Tag())
                id3v1Tag = song.getId3v1Tag();
            else {
                id3v1Tag = new ID3v1Tag();
                song.setId3v1Tag(id3v1Tag);
            }
            id3v1Tag.setTitle(sm.getTitle());
            id3v1Tag.setTrack(sm.getTrack());
            id3v1Tag.setArtist(sm.getArtist());
            id3v1Tag.setAlbum(sm.getAlbum());
            id3v1Tag.setYear(sm.getYear());
            id3v1Tag.setGenre(Integer.parseInt(sm.getGenre()));
            id3v1Tag.setComment(sm.getComment());
            song.save(sm.getPath());
            sm.setGenre(id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
        } catch (Exception e) {
            showDialog(e.getMessage());
        }
    }

    private void setFavorite(boolean isFavorite) {
        menuPanel.setFavorite(isFavorite);
        if (isFavorite)
            dataPanel.setSongData(currentSongModel);
        else
            currentSongModel.setId(DEFAULT_SONG_ID);
    }
}
