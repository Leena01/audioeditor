package view;

import static view.panel.analysis.SpectrogramPanel.DIGIT_SIZE_MIN;
import static view.util.Constants.*;
import static view.util.Helper.*;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import logic.dbaccess.listmodel.SongListModel;
import logic.dbaccess.SongModel;
import logic.dbaccess.DatabaseAccessModel;
import logic.exceptions.InvalidOperationException;
import logic.exceptions.MatlabEngineException;
import logic.exceptions.SQLConnectionException;
import logic.matlab.MatlabHandler;
import properties.ConfigPropertiesLoader;
import properties.SongPropertiesLoader;
import view.core.bar.HorizontalBar;
import view.core.dialog.PopupDialog;
import view.core.listener.EmptyMouseListener;
import view.panel.*;
import logic.dbaccess.tablemodel.SongTableModel;
import view.core.bar.SideBar;
import view.core.window.Window;
import view.core.label.Label;
import view.panel.analysis.AnalysisPanel;
import view.panel.analysis.SpectrogramPanel;
import view.panel.popup.DeleteSongsPanel;
import view.panel.popup.EditPanel;

import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MainWindow extends Window {
    /**
     * Constants
     */
    private static final String SAVE_QUESTION = "Do you want to save the new file?";
    private static final String FILE_BROWSER_INFO = "Specify the directory and the file name";
    private static final String FILE_ALREADY_EXISTS_ERROR = "The path specified already exists.";
    private static final String COVER_ERROR = "Could not load cover.";
    private static final String WRONG_INPUT_ERROR = "Wrong input data.";
    private static final String FILE_TYPE_ERROR = "This file type is not supported.";
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
    private DatabaseAccessModel databaseAccessModel;
    private MatlabHandler matlabHandler;
    private SongTableModel viewSongTableModel;
    private SongTableModel deleteSongTableModel;
    private SongModel currentSongModel;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private ViewSongsPanel viewSongsPanel;
    private AnalysisPanel analysisPanel;
    private SpectrogramPanel spectrogramPanel;
    private ChangePitchPanel changePitchPanel;
    private JDialog editDialog;
    private JDialog deleteSongsDialog;
    private JDialog changePitchDialog;
    private EditPanel editPanel;
    private DeleteSongsPanel deleteSongsPanel;
    private DataPanel dataPanel;
    private CutSongPanel cutSongPanel;
    private OptionPanel optionPanel;
    private JPanel sideBar;
    private HorizontalBar bottomPanel;
    private CardLayout cardLayout;
    private JLabel currentSongTitle;
    private JLabel infoLabel;
    private String defaultOpenDir;
    private BufferedImage plot;
    private Component glassPane;

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
    private ActionListener cpPreviewListener;
    private ActionListener cpSaveListener;
    private ActionListener cutFileListener;
    private ActionListener cutDoneListener;
    private ActionListener viewSpecListener;
    private ActionListener showSpecListener;
    private ActionListener analyzeSongListener;
    private ActionListener loadSongListener;
    private ActionListener editSongListener;
    private ActionListener addSongListener;
    private ActionListener deleteSongListener;
    private ActionListener editDoneListener;
    private ActionListener deleteDoneListener;

    /**
     * Constructor
     * @param databaseAccessModel Database access model
     * @param matlabHandler Matlab handler
     */
    public MainWindow(DatabaseAccessModel databaseAccessModel, MatlabHandler matlabHandler) {
        super();
        this.databaseAccessModel = databaseAccessModel;
        this.matlabHandler = matlabHandler;
        this.viewSongTableModel = new SongTableModel();
        this.deleteSongTableModel = new SongTableModel();
        this.currentSongModel = new SongModel();
        this.cardLayout = new CardLayout();
        this.glassPane = getGlassPane();
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        initializeListeners();
        addListeners();
        initializeLabels();
        initializePanels();
        addPanels();
        getDefaultSong();
        setTimer();
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
        System.out.println(currentSongModel.getId());
        if (currentSongModel.isEmpty()) {
            optionPanel.showOptions(false);
            menuPanel.hideImage(true);
        }
        // pack();
        setMinimumSize(WIN_MIN_SIZE);
        setSize(WIN_MIN_SIZE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    protected void hideImage(boolean isHidden) {
        if (!currentSongModel.isEmpty()) {
            menuPanel.hideImage(isHidden);
            spectrogramPanel.hideImage(isHidden);
        }
    }

    protected void maximizeImage(boolean isMaximized) {
        menuPanel.maximizeImage(isMaximized);
        spectrogramPanel.maximizeImage(isMaximized);
        cutSongPanel.maximizeImage(isMaximized);
    }

    private void addListeners() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                glassPane.setVisible(true);
                try {
                    matlabHandler.close();
                } catch (MatlabEngineException mee) {
                    showDialog(mee.getMessage());
                }
                databaseAccessModel.close();
            }
        });
        this.glassPane.addMouseListener(emptyMouseListener);
    }

    private void getDefaultSong() {
        defaultOpenDir = getDir(currentSongModel.getPath());
        getSavedInstance();
    }

    private void getSavedInstance() {
        if (!currentSongModel.isEmpty()) {
            try {
                int id = databaseAccessModel.getId(currentSongModel);
                currentSongModel.setId(id);
                if (currentSongModel.isDefault())
                    menuPanel.setFavorite(false);
                else
                    menuPanel.setFavorite(true);
            } catch (SQLConnectionException e) {
                showDialog(e.getMessage());
                menuPanel.setFavorite(false);
            }
        }
    }

    private void addPanels() {
        mainPanel.add(menuPanel, MENU_PANEL);
        mainPanel.add(viewSongsPanel, VIEW_SONGS_PANEL);
        mainPanel.add(dataPanel, DATA_PANEL);
        mainPanel.add(analysisPanel, ANALYSIS_PANEL);
        mainPanel.add(spectrogramPanel, SPECTROGRAM_PANEL);
        mainPanel.add(cutSongPanel, CUT_SONG_PANEL);
        add(mainPanel, BorderLayout.CENTER);
        add(sideBar, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void refreshCache() {
        try {
            viewSongsPanel.setList(databaseAccessModel.getSongList());
            deleteSongsPanel.setList(databaseAccessModel.getSongList());
            if (databaseAccessModel.hasInvalid()) {
                showInfo(infoLabel, SONGS_DELETED_INFO, INFO_LABEL_DELAY);
                if (!databaseAccessModel.isSongValid(currentSongModel)) {
                    setFavorite(false);
                    currentSongModel.setPath(SongPropertiesLoader.getDefaultPath());
                }
            }
            dataPanel.setSongData(currentSongModel);
        } catch (SQLConnectionException sqe) {
            showInfo(infoLabel, sqe.getMessage(), INFO_LABEL_DELAY);
        }
    }

    private void setTimer() {
        Timer t = new Timer(ConfigPropertiesLoader.getRefreshMillis(), e -> refreshCache());
        t.setRepeats(true);
        t.start();
    }

    private File openFile() {
        JFileChooser fileChooser;

        if (defaultOpenDir != null && !defaultOpenDir.equals(""))
            fileChooser = new JFileChooser(defaultOpenDir);
        else
            fileChooser = new JFileChooser();

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile();
        return null;
    }

    private File[] openFiles() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.showOpenDialog(this);
        return chooser.getSelectedFiles();
    }

    private Image getCover() {
        try {
            Image cover = ImageIO.read(new File(COVER_NAME));
            try {
                Mp3File song = new Mp3File(currentSongModel.getPath());
                if (song.hasId3v2Tag()) {
                    ID3v2 id3v2tag = song.getId3v2Tag();
                    byte[] imageData = id3v2tag.getAlbumImage();
                    cover = ImageIO.read(new ByteArrayInputStream(imageData));
                }
            } catch (Exception ignored) { }
            return cover;
        } catch (Exception ex) {
            showDialog(COVER_ERROR);
        }
        return null;
    }

    private void setFavorite(boolean isFavorite) {
        menuPanel.setFavorite(isFavorite);
        if (isFavorite)
            dataPanel.setSongData(currentSongModel);
        else
            currentSongModel.setDefault();
    }

    private void openSong() {
        final Image cover = getCover();
        currentSongTitle.setText(currentSongModel.getTitle());
        getSavedInstance();

        new Thread(() -> {
            glassPane.setVisible(true);
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            try {
                matlabHandler.openSong(currentSongModel.getPath());
                matlabHandler.plotSong(PLOT_IMAGE_NAME);
                SwingUtilities.invokeLater(() -> {
                    matlabHandler.passData(currentSongModel);
                    changePitchPanel.setFreq(currentSongModel.getFreq());
                    try {
                        plot = ImageIO.read(new File(PLOT_IMAGE_NAME));
                        menuPanel.setCurrentSong(currentSongModel.getTotalSamples(), currentSongModel.getFreq(), plot, cover,
                                isNormal, getExtendedState() == MAXIMIZED_BOTH);
                    } catch (IOException ignored) { }
                    optionPanel.showOptions(true);
                    cardLayout.show(mainPanel, MENU_PANEL);
                });
            } catch (MatlabEngineException mee) {
                showDialog(mee.getMessage());
            } finally {
                setCursor(Cursor.getDefaultCursor());
                glassPane.setVisible(false);
            }
        }).start();
    }

    private void initializeLabels() {
        currentSongTitle = new Label();
        currentSongTitle.setSize(BOTTOM_FIELD_SIZE);
        currentSongTitle.setOpaque(false);
        currentSongTitle.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        infoLabel = new Label();
        infoLabel.setSize(BOTTOM_FIELD_SIZE);
        infoLabel.setOpaque(false);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
    }

    private void initializePanels() {
        mainPanel = new JPanel(cardLayout);
        mainPanel.setVisible(true);

        menuPanel = new MenuPanel(matlabHandler, glassPane, favoriteButtonListener, unfavoriteButtonListener);
        viewSongsPanel = new ViewSongsPanel(viewSongTableModel, loadSongListener, addSongListener, editSongListener,
                deleteSongListener, backToMenuListener);

        editPanel = new EditPanel(editDoneListener);
        editDialog = new PopupDialog(this, "Edit song", editPanel);
        editDialog.setVisible(false);

        deleteSongsPanel = new DeleteSongsPanel(deleteSongTableModel, deleteDoneListener);
        deleteSongsDialog = new PopupDialog(this, "Delete songs", deleteSongsPanel);
        deleteSongsDialog.setVisible(false);

        dataPanel = new DataPanel(currentSongModel, backToMenuListener);
        cutSongPanel = new CutSongPanel(cutDoneListener, backToMenuListener);

        optionPanel = new OptionPanel(backToMenuListener, openFileListener, viewSongsListener, showDataListener,
                changePitchListener, cutFileListener, viewSpecListener, analyzeSongListener);
        sideBar = new SideBar();
        sideBar.add(optionPanel);

        analysisPanel = new AnalysisPanel(matlabHandler);

        spectrogramPanel = new SpectrogramPanel(showSpecListener, backToMenuListener);

        changePitchPanel = new ChangePitchPanel(cpPreviewListener, cpSaveListener);
        changePitchDialog = new PopupDialog(this, "Change pitch", changePitchPanel);
        changePitchDialog.setVisible(false);

        bottomPanel = new HorizontalBar();
        bottomPanel.add(currentSongTitle, BorderLayout.WEST);
        bottomPanel.add(infoLabel, BorderLayout.EAST);
        bottomPanel.setHeight(BOTTOM_PANEL_HEIGHT);
    }

    private void initializeListeners() {
        emptyMouseListener = new EmptyMouseListener();

        backToMenuListener = ae -> cardLayout.show(mainPanel, MENU_PANEL);

        openFileListener = ae -> {
            File f = openFile();
            if (f != null) {
                defaultOpenDir = f.getParent();
                currentSongModel = new SongModel(f);
                openSong();
            }
        };

        viewSongsListener = ae -> {
            if (!currentSongModel.isEmpty())
                menuPanel.pauseSong();
            cardLayout.show(mainPanel, VIEW_SONGS_PANEL);
        };

        showDataListener = ae -> {
            refreshCache();
            if (!currentSongModel.isEmpty())
                menuPanel.pauseSong();
            cardLayout.show(mainPanel, DATA_PANEL);
        };

        favoriteButtonListener = ae -> {
            refreshCache();
            try {
                databaseAccessModel.addSong(currentSongModel);
                setFavorite(true);
            } catch(InvalidOperationException | SQLConnectionException e) {
                showDialog(e.getMessage());
            }
        };

        unfavoriteButtonListener = ae -> {
            refreshCache();
            try {
                databaseAccessModel.deleteSong(currentSongModel);
                setFavorite(false);
            } catch(InvalidOperationException | SQLConnectionException e) {
                showDialog(e.getMessage());
            }
        };

        changePitchListener = ae -> changePitchDialog.setVisible(true);

        cpPreviewListener = ae -> {
            new Thread(() -> {
                glassPane.setVisible(true);
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                matlabHandler.changePitch(changePitchPanel.getFreq());
                SwingUtilities.invokeLater(() -> {
                    glassPane.setVisible(false);
                    setCursor(Cursor.getDefaultCursor());
                });
            }).start();
        };

        cpSaveListener = ae -> {

        };

        // TODO savebutton listener

        cutFileListener = ae -> {
            if (!currentSongModel.isEmpty())
                menuPanel.pauseSong();
            cutSongPanel.setCurrentSong(currentSongModel.getTotalSamples(), currentSongModel.getFreq(),
                    plot, getExtendedState() == MAXIMIZED_BOTH);
            cardLayout.show(mainPanel, CUT_SONG_PANEL);
        };

        cutDoneListener = ae -> new Thread(() -> {
            glassPane.setVisible(true);
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            try {
                matlabHandler.cutSong(cutSongPanel.getFrom(), cutSongPanel.getTo());
            } catch (MatlabEngineException e) {
                showDialog(e.getMessage());
                cutSongPanel.setDefaultValues();
            }

            int result = JOptionPane.showOptionDialog(this,
                    SAVE_QUESTION, null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, OPTIONS, OPTIONS[0]);

            switch (result) {
                case JOptionPane.YES_OPTION:
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle(FILE_BROWSER_INFO);
                    int userSelection = fileChooser.showSaveDialog(this);
                    try {
                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            File fileToSave = fileChooser.getSelectedFile();
                            if (fileToSave.exists())
                                showDialog(FILE_ALREADY_EXISTS_ERROR);
                            else
                                matlabHandler.saveSong(fileToSave.getAbsolutePath());
                        }
                    } catch (MatlabEngineException mee) {
                        showDialog(mee.getMessage());
                    }
                    break;
                case JOptionPane.NO_OPTION:
                    try {
                        matlabHandler.saveSong(currentSongModel.getPath());
                        openSong();
                    } catch (MatlabEngineException mee) {
                        showDialog(mee.getMessage());
                    }
            }

            SwingUtilities.invokeLater(() -> {
                glassPane.setVisible(false);
                setCursor(Cursor.getDefaultCursor());
            });
        }).start();

        viewSpecListener = ae -> {
            if (!currentSongModel.isEmpty())
                menuPanel.pauseSong();
            cardLayout.show(mainPanel, SPECTROGRAM_PANEL);
        };

        showSpecListener = ae -> {
            String windowSizeString = spectrogramPanel.getWindowSize();
            String hopSizeString = spectrogramPanel.getHopSize();
            String nfftString = spectrogramPanel.getNfft();
            String window = spectrogramPanel.getWindow();
            int windowSize = (!windowSizeString.equals("") ? Integer.parseInt(windowSizeString) : 0);
            int hopSize = (!hopSizeString.equals("") ? Integer.parseInt(hopSizeString) : 0);
            int nfft = (!nfftString.equals("") ? Integer.parseInt(nfftString) : 0);
            if (windowSize < Math.pow(10, DIGIT_SIZE_MIN - 1) ||
                    hopSize < Math.pow(10, DIGIT_SIZE_MIN - 1) ||
                    nfft < Math.pow(10, DIGIT_SIZE_MIN - 1) ||
                    windowSize <= hopSize) {
                showInfo(infoLabel, WRONG_INPUT_ERROR, INFO_LABEL_DELAY);
            }
            else {
                new Thread(() -> {
                    glassPane.setVisible(true);
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    try {
                        matlabHandler.showSpectrogram(windowSize, hopSize, nfft, window,
                                SPEC_IMAGE_NAME, SPEC_3D_IMAGE_NAME);
                        SwingUtilities.invokeLater(() -> {
                            glassPane.setVisible(false);
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
                        glassPane.setVisible(false);
                    }
                }).start();
            }
        };

        analyzeSongListener = ae -> {
            if (!currentSongModel.isEmpty())
                menuPanel.pauseSong();
            cardLayout.show(mainPanel, ANALYSIS_PANEL);
            // TODO
        };

        loadSongListener = ae -> {
            SongModel sm = viewSongsPanel.getSelectedRow();
            if (sm != null) {
                currentSongModel = new SongModel(sm);
                defaultOpenDir = getDir(currentSongModel.getPath());
                openSong();
            }
        };

        editSongListener = ae -> {
            SongModel sm = viewSongsPanel.getSelectedRow();
            if (sm != null) {
                editPanel.setSelectedSong(sm);
                editDialog.setVisible(true);
            }
        };

        editDoneListener = ae -> {
            try {
                editPanel.setNewData();
                SongModel sm = editPanel.getSelectedSongModel();
                if (sm.getId() == currentSongModel.getId())
                    currentSongModel = sm;
                sm.setTags();
                databaseAccessModel.editSong(editPanel.getSelectedSongModel());
                showDialog(SUCCESSFUL_OPERATION_INFO);
            } catch (Exception e) {
                showDialog(e.getMessage());
            }
            editDialog.setVisible(false);
        };

        addSongListener = ae -> {
            File[] files = openFiles();
            List<File> validFiles = new ArrayList<>();
            for (File f: files) {
                if (!Arrays.asList(SongPropertiesLoader.getExtensionNames()).contains(getFileExtension(f)))
                    showInfo(infoLabel, FILE_TYPE_ERROR, INFO_LABEL_DELAY);
                else {
                    validFiles.add(f);
                }
            }
            SongListModel songListModel = new SongListModel(validFiles);
            try {
                databaseAccessModel.addSongs(songListModel);
                showDialog(SUCCESSFUL_OPERATION_INFO);
            } catch (InvalidOperationException | SQLConnectionException e) {
                showDialog(e.getMessage());
            }
        };

        deleteSongListener = ae -> deleteSongsDialog.setVisible(true);

        deleteDoneListener = ae -> {
            deleteSongsDialog.setVisible(false);
            try {
                databaseAccessModel.deleteSongs(deleteSongsPanel.getSelectedRows());
                showDialog(SUCCESSFUL_OPERATION_INFO);
            } catch(InvalidOperationException | SQLConnectionException e) {
                showDialog(e.getMessage());
            }
        };
    }
}
