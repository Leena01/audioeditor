package org.ql.audioeditor.view;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import org.ql.audioeditor.common.properties.ConfigPropertiesLoader;
import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.logic.dbaccess.DatabaseAccessModel;
import org.ql.audioeditor.logic.dbaccess.SongModel;
import org.ql.audioeditor.logic.dbaccess.listmodel.SongListModel;
import org.ql.audioeditor.logic.dbaccess.tablemodel.SongTableModel;
import org.ql.audioeditor.logic.exceptions.InvalidOperationException;
import org.ql.audioeditor.logic.exceptions.MatlabEngineException;
import org.ql.audioeditor.logic.exceptions.SQLConnectionException;
import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.core.bar.HorizontalBar;
import org.ql.audioeditor.view.core.bar.InverseHorizontalBar;
import org.ql.audioeditor.view.core.bar.SideBar;
import org.ql.audioeditor.view.core.dialog.PopupDialog;
import org.ql.audioeditor.view.core.label.Label;
import org.ql.audioeditor.view.core.listener.EmptyMouseListener;
import org.ql.audioeditor.view.core.window.Window;
import org.ql.audioeditor.view.panel.ChangePitchPanel;
import org.ql.audioeditor.view.panel.CutSongPanel;
import org.ql.audioeditor.view.panel.DataPanel;
import org.ql.audioeditor.view.panel.MenuPanel;
import org.ql.audioeditor.view.panel.OptionPanel;
import org.ql.audioeditor.view.panel.ViewSongsPanel;
import org.ql.audioeditor.view.panel.analysis.AnalysisPanel;
import org.ql.audioeditor.view.panel.analysis.ChromagramPanel;
import org.ql.audioeditor.view.panel.analysis.SpectrogramPanel;
import org.ql.audioeditor.view.panel.popup.DeleteSongsPanel;
import org.ql.audioeditor.view.panel.popup.EditPanel;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.ql.audioeditor.common.util.Helper.convertToNumber;
import static org.ql.audioeditor.common.util.Helper.getDir;
import static org.ql.audioeditor.common.util.Helper.getFileExtension;
import static org.ql.audioeditor.common.util.Helper.showDialog;
import static org.ql.audioeditor.common.util.Helper.showInfo;
import static org.ql.audioeditor.view.param.Constants.BOTTOM_FIELD_SIZE;
import static org.ql.audioeditor.view.param.Constants.BOTTOM_PANEL_HEIGHT;
import static org.ql.audioeditor.view.param.Constants
    .MEDIA_CONTROL_PANEL_HEIGHT;
import static org.ql.audioeditor.view.param.Constants.OPTIONS;
import static org.ql.audioeditor.view.param.Constants.TEXT_FIELD_DIGIT_SIZE_MIN;
import static org.ql.audioeditor.view.param.Constants.WIN_MIN_SIZE;

public class MainWindow extends Window {
    /**
     * Constants.
     */
    private static final String SAVE_QUESTION =
        "Do you want to save the new file?";
    private static final String FILE_BROWSER_INFO =
        "Specify the directory and the file name";
    private static final String FILE_ALREADY_EXISTS_ERROR =
        "The path specified already exists.";
    private static final String COVER_ERROR = "Could not load cover.";
    private static final String WRONG_INPUT_ERROR = "Wrong input data.";
    private static final String FILE_TYPE_ERROR =
        "This file type is not supported.";
    private static final String SONGS_DELETED_INFO = "Songs deleted.";
    private static final String SUCCESSFUL_OPERATION_INFO =
        "Successful operation.";
    private static final String MENU_PANEL = "Menu Panel";
    private static final String VIEW_SONGS_PANEL = "View Songs Panel";
    private static final String DATA_PANEL = "Data Panel";
    private static final String ANALYSIS_PANEL = "Analysis Panel";
    private static final String SPECTROGRAM_PANEL = "Spectrogram Panel";
    private static final String CHROMAGRAM_PANEL = "Chromagram Panel";
    private static final String CUT_SONG_PANEL = "Cut Song Panel";
    private static final int INFO_LABEL_DELAY = 5000;
    private static final Border ROOT_PANE_BORDER =
        BorderFactory.createLineBorder(Color.DARK_GRAY);
    private static final Border CURRENT_SONG_TITLE_BORDER =
        BorderFactory.createEmptyBorder(5, 5, 5, 5);
    private static final Border INFO_LABEL_BORDER =
        BorderFactory.createEmptyBorder(0, 12, 0, 12);

    /**
     * Private data members.
     */
    private final DatabaseAccessModel databaseAccessModel;
    private final MatlabHandler matlabHandler;
    private final SongTableModel viewSongTableModel;
    private final SongTableModel deleteSongTableModel;
    private final CardLayout cardLayout;
    private final Component glassPane;
    private SongModel currentSongModel;
    private JPanel mainPanel;
    private JPanel innerMainPanel;
    private HorizontalBar mediaControlPanel;
    private MenuPanel menuPanel;
    private ViewSongsPanel viewSongsPanel;
    private AnalysisPanel analysisPanel;
    private SpectrogramPanel spectrogramPanel;
    private ChromagramPanel chromagramPanel;
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
    private JLabel currentSongTitle;
    private JLabel infoLabel;
    private String defaultOpenDir;
    private BufferedImage plot;
    /**
     * Listeners.
     */
    private MouseListener emptyMouseListener;
    private ActionListener backToMenuListener;
    private ActionListener openFileListener;
    private ActionListener viewSongsListener;
    private ActionListener showDataListener;
    private ActionListener showSimilarSongsListener;
    private ActionListener favoriteButtonListener;
    private ActionListener unfavoriteButtonListener;
    private ActionListener changePitchListener;
    private ActionListener cpPreviewListener;
    private ActionListener cpSaveListener;
    private ActionListener cutFileListener;
    private ActionListener cutDoneListener;
    private ActionListener viewSpecListener;
    private ActionListener viewChromListener;
    private ActionListener showSpecListener;
    private ActionListener showChromListener;
    private ActionListener analyzeSongListener;
    private ActionListener loadSongListener;
    private ActionListener editSongListener;
    private ActionListener addSongsListener;
    private ActionListener deleteSongListener;
    private ActionListener editDoneListener;
    private ActionListener deleteDoneListener;

    /**
     * Constructor
     *
     * @param databaseAccessModel Database access model
     * @param matlabHandler       Matlab handler
     */
    public MainWindow(DatabaseAccessModel databaseAccessModel,
        MatlabHandler matlabHandler) {
        super();
        this.databaseAccessModel = databaseAccessModel;
        this.matlabHandler = matlabHandler;
        this.viewSongTableModel = new SongTableModel();
        this.deleteSongTableModel = new SongTableModel();
        this.currentSongModel = new SongModel();
        this.cardLayout = new CardLayout();
        this.glassPane = getGlassPane();
        this.rootPane.setBorder(ROOT_PANE_BORDER);
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
        if (d.width < m.width || d.height < m.height) {
            pack();
        }
        super.paint(g);
    }

    public void run() {
        changePanel(MENU_PANEL);
        if (currentSongModel.isEmpty()) {
            optionPanel.showOptions(false);
            menuPanel.hideImage(true);
            mediaControlPanel.setVisible(false);
        }
        setMinimumSize(WIN_MIN_SIZE);
        setSize(WIN_MIN_SIZE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    protected void hideImage(boolean isHidden) {
        if (!currentSongModel.isEmpty()) {
            menuPanel.hideImage(isHidden);
            spectrogramPanel.hideImage(isHidden);
            chromagramPanel.hideImage(isHidden);
        }
    }

    protected void maximizeImage(boolean isMaximized) {
        menuPanel.maximizeImage(isMaximized);
        spectrogramPanel.maximizeImage(isMaximized);
        chromagramPanel.maximizeImage(isMaximized);
        cutSongPanel.maximizeImage(isMaximized);
    }

    private void addListeners() {
        addWindowListener(new ClosingWindowAdapter());
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
                if (currentSongModel.isDefault()) {
                    menuPanel.setFavorite(false);
                }
                else {
                    menuPanel.setFavorite(true);
                }
            } catch (SQLConnectionException e) {
                showDialog(e.getMessage());
                menuPanel.setFavorite(false);
            }
        }
    }

    private void addPanels() {
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.setPreferredSize(mainPanel.getSize());
        add(sideBar, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(innerMainPanel, BorderLayout.CENTER);
        mainPanel.add(mediaControlPanel, BorderLayout.SOUTH);
        innerMainPanel.add(menuPanel, MENU_PANEL);
        innerMainPanel.add(viewSongsPanel, VIEW_SONGS_PANEL);
        innerMainPanel.add(dataPanel, DATA_PANEL);
        innerMainPanel.add(analysisPanel, ANALYSIS_PANEL);
        innerMainPanel.add(spectrogramPanel, SPECTROGRAM_PANEL);
        innerMainPanel.add(chromagramPanel, CHROMAGRAM_PANEL);
        innerMainPanel.add(cutSongPanel, CUT_SONG_PANEL);
    }

    private void refreshCache() {
        try {
            viewSongsPanel.setList(databaseAccessModel.getSongList());
            deleteSongsPanel.setList(databaseAccessModel.getSongList());
            if (databaseAccessModel.hasInvalid()) {
                showInfo(infoLabel, SONGS_DELETED_INFO, INFO_LABEL_DELAY);
                if (!databaseAccessModel.isSongValid(currentSongModel)) {
                    setFavorite(false);
                    currentSongModel
                        .setPath(SongPropertiesLoader.getDefaultPath());
                }
            }
            dataPanel.setSongData(currentSongModel);
        } catch (SQLConnectionException sqe) {
            showInfo(infoLabel, sqe.getMessage(), INFO_LABEL_DELAY);
        }
    }

    private void setTimer() {
        Timer t = new Timer(ConfigPropertiesLoader.getRefreshMillis(),
            e -> refreshCache());
        t.setRepeats(true);
        t.start();
    }

    private File openFile() {
        JFileChooser fileChooser;

        if (defaultOpenDir != null && !defaultOpenDir.equals("")) {
            fileChooser = new JFileChooser(defaultOpenDir);
        }
        else {
            fileChooser = new JFileChooser();
        }

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
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
            Image cover = ImageLoader.getCover();
            try {
                Mp3File song = new Mp3File(currentSongModel.getPath());
                if (song.hasId3v2Tag()) {
                    ID3v2 id3v2tag = song.getId3v2Tag();
                    byte[] imageData = id3v2tag.getAlbumImage();
                    cover = ImageIO.read(new ByteArrayInputStream(imageData));
                }
            } catch (Exception ignored) {
            }
            return cover;
        } catch (Exception ex) {
            showDialog(COVER_ERROR);
        }
        return null;
    }

    private void setFavorite(boolean isFavorite) {
        menuPanel.setFavorite(isFavorite);
        if (isFavorite) {
            dataPanel.setSongData(currentSongModel);
        }
        else {
            currentSongModel.setDefault();
        }
    }

    private void openSong() {
        Image cover = getCover();
        currentSongTitle.setText(currentSongModel.getTitle());
        getSavedInstance();

        new Thread(() -> {
            glassPane.setVisible(true);
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            try {
                matlabHandler.openSong(currentSongModel.getPath());
                matlabHandler
                    .plotSong(ImageLoader.getPlotImagePath());
                SwingUtilities.invokeLater(() -> {
                    matlabHandler.passData(currentSongModel);
                    changePitchPanel.setFreq(currentSongModel.getFreq());
                    try {
                        plot = ImageIO.read(new File(ImageLoader
                            .getPlotImagePath()));
                        menuPanel
                            .setCurrentSong(currentSongModel.getTotalSamples(),
                                currentSongModel.getFreq(), plot, cover,
                                isNormal, getExtendedState() == MAXIMIZED_BOTH);
                        optionPanel.showOptions(true);
                        changePanel(MENU_PANEL);
                    } catch (IOException ignored) {
                    }
                });
            } catch (MatlabEngineException mee) {
                showDialog(mee.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                setCursor(Cursor.getDefaultCursor());
                glassPane.setVisible(false);
            }
        }).start();
    }

    private void showSpec() {
        String windowSizeString = spectrogramPanel.getWindowSize();
        String hopSizeString = spectrogramPanel.getHopSize();
        String nfftString = spectrogramPanel.getNfft();
        String window = spectrogramPanel.getWindow();
        int windowSize = convertToNumber(windowSizeString);
        int hopSize = convertToNumber(hopSizeString);
        int nfft = convertToNumber(nfftString);
        if (checkNumbers(windowSize, hopSize, nfft)) {
            new Thread(() -> {
                glassPane.setVisible(true);
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                try {
                    matlabHandler
                        .showSpectrogram(windowSize, hopSize, nfft, window,
                            ImageLoader.getSpecImagePath(),
                            ImageLoader.getSpec3dImagePath());
                    SwingUtilities.invokeLater(() -> {
                        glassPane.setVisible(false);
                        setCursor(Cursor.getDefaultCursor());
                        try {
                            Image spec =
                                ImageIO.read(new File(ImageLoader
                                    .getSpecImagePath()));
                            Image spec3d =
                                ImageIO.read(new File(ImageLoader
                                    .getSpec3dImagePath()));
                            spectrogramPanel.changeImage(spec, spec3d, isNormal,
                                getExtendedState() == MAXIMIZED_BOTH);
                        } catch (IOException ignored) {
                        }
                    });
                } catch (MatlabEngineException mee) {
                    showDialog(mee.getMessage());
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                    glassPane.setVisible(false);
                }
            }).start();
        }
    }

    private void showChrom() {
        String windowSizeString = chromagramPanel.getWindowSize();
        String hopSizeString = chromagramPanel.getHopSize();
        String nfftString = chromagramPanel.getNfft();
        String window = chromagramPanel.getWindow();
        int windowSize = convertToNumber(windowSizeString);
        int hopSize = convertToNumber(hopSizeString);
        int nfft = convertToNumber(nfftString);
        if (checkNumbers(windowSize, hopSize, nfft)) {
            new Thread(() -> {
                glassPane.setVisible(true);
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                try {
                    matlabHandler
                        .showChromagram(windowSize, hopSize, nfft, window,
                            ImageLoader.getChromImagePath());
                    SwingUtilities.invokeLater(() -> {
                        glassPane.setVisible(false);
                        setCursor(Cursor.getDefaultCursor());
                        try {
                            Image chromagram =
                                ImageIO.read(new File(ImageLoader
                                    .getChromImagePath()));
                            chromagramPanel.changeImage(chromagram, isNormal,
                                getExtendedState() == MAXIMIZED_BOTH);
                        } catch (IOException ignored) {
                        }
                    });
                } catch (MatlabEngineException mee) {
                    showDialog(mee.getMessage());
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                    glassPane.setVisible(false);
                }
            }).start();
        }
    }

    private boolean checkNumbers(int windowSize, int
        hopSize, int nfft) {
        if (windowSize < Math.pow(10, TEXT_FIELD_DIGIT_SIZE_MIN - 1) ||
            hopSize < Math.pow(10, TEXT_FIELD_DIGIT_SIZE_MIN - 1) ||
            nfft < Math.pow(10, TEXT_FIELD_DIGIT_SIZE_MIN - 1) ||
            windowSize <= hopSize) {
            showInfo(infoLabel, WRONG_INPUT_ERROR, INFO_LABEL_DELAY);
            return false;
        }
        return true;
    }

    private void cutSong() {
        new Thread(() -> {
            glassPane.setVisible(true);
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            try {
                matlabHandler
                    .cutSong(cutSongPanel.getFrom(), cutSongPanel.getTo());
            } catch (MatlabEngineException e) {
                showDialog(e.getMessage());
                cutSongPanel.setDefaultValues();
            }

            int result = JOptionPane.showOptionDialog(this,
                SAVE_QUESTION, null, JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, OPTIONS, OPTIONS[0]);

            switch (result) {
                case JOptionPane.YES_OPTION:
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle(FILE_BROWSER_INFO);
                    int userSelection = fileChooser.showSaveDialog(this);
                    try {
                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            File fileToSave = fileChooser.getSelectedFile();
                            if (fileToSave.exists()) {
                                showDialog(FILE_ALREADY_EXISTS_ERROR);
                            }
                            else {
                                matlabHandler
                                    .saveSong(fileToSave.getAbsolutePath());
                            }
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
    }

    private void editSongsDone() {
        try {
            editPanel.setNewData();
            SongModel sm = editPanel.getSelectedSongModel();
            if (sm.getId() == currentSongModel.getId()) {
                currentSongModel = sm;
            }
            sm.setTags();
            databaseAccessModel.editSong(editPanel.getSelectedSongModel());
            showDialog(SUCCESSFUL_OPERATION_INFO);
        } catch (Exception e) {
            showDialog(e.getMessage());
        }
        editDialog.setVisible(false);
    }

    private void addSongs() {
        File[] files = openFiles();
        List<File> validFiles = new ArrayList<>();
        for (File f : files) {
            if (!Arrays.asList(SongPropertiesLoader.getExtensionNames())
                .contains(getFileExtension(f))) {
                showInfo(infoLabel, FILE_TYPE_ERROR, INFO_LABEL_DELAY);
            }
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
    }

    private void deleteSongsDone() {
        deleteSongsDialog.setVisible(false);
        try {
            databaseAccessModel.deleteSongs(deleteSongsPanel.getSelectedRows());
            showDialog(SUCCESSFUL_OPERATION_INFO);
        } catch (InvalidOperationException | SQLConnectionException e) {
            showDialog(e.getMessage());
        }
    }

    private void initializeLabels() {
        currentSongTitle = new Label();
        currentSongTitle.setSize(BOTTOM_FIELD_SIZE);
        currentSongTitle.setOpaque(false);
        currentSongTitle.setBorder(CURRENT_SONG_TITLE_BORDER);
        infoLabel = new Label();
        infoLabel.setSize(BOTTOM_FIELD_SIZE);
        infoLabel.setOpaque(false);
        infoLabel.setBorder(INFO_LABEL_BORDER);
    }

    private void initializePanels() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        innerMainPanel = new JPanel(cardLayout);
        innerMainPanel.setVisible(true);
        mediaControlPanel = new InverseHorizontalBar(new BorderLayout());
        mediaControlPanel.setHeight(MEDIA_CONTROL_PANEL_HEIGHT);

        menuPanel =
            new MenuPanel(matlabHandler, mediaControlPanel, inputMap, actionMap,
                favoriteButtonListener, unfavoriteButtonListener);
        viewSongsPanel =
            new ViewSongsPanel(viewSongTableModel, loadSongListener,
                addSongsListener, editSongListener, deleteSongListener,
                backToMenuListener);

        editPanel = new EditPanel(editDoneListener);
        editDialog = new PopupDialog(this, "Edit song", editPanel);
        editDialog.setVisible(false);

        deleteSongsPanel =
            new DeleteSongsPanel(deleteSongTableModel, deleteDoneListener);
        deleteSongsDialog =
            new PopupDialog(this, "Delete songs", deleteSongsPanel);
        deleteSongsDialog.setVisible(false);

        dataPanel = new DataPanel(currentSongModel, backToMenuListener);
        cutSongPanel = new CutSongPanel(cutDoneListener, backToMenuListener);

        optionPanel = new OptionPanel(backToMenuListener, openFileListener,
            viewSongsListener, showDataListener, changePitchListener,
            cutFileListener, viewSpecListener, viewChromListener,
            analyzeSongListener);
        sideBar = new SideBar();
        sideBar.add(optionPanel);

        analysisPanel = new AnalysisPanel(matlabHandler);
        spectrogramPanel =
            new SpectrogramPanel(showSpecListener, backToMenuListener);
        chromagramPanel =
            new ChromagramPanel(showChromListener, backToMenuListener);

        changePitchPanel =
            new ChangePitchPanel(cpPreviewListener, cpSaveListener);
        changePitchDialog =
            new PopupDialog(this, "Change pitch", changePitchPanel);
        changePitchDialog.setVisible(false);

        bottomPanel = new HorizontalBar();
        bottomPanel.add(currentSongTitle, BorderLayout.WEST);
        bottomPanel.add(infoLabel, BorderLayout.EAST);
        bottomPanel.setHeight(BOTTOM_PANEL_HEIGHT);
    }

    private void changePanel(String name) {
        cardLayout.show(innerMainPanel, name);
        if (name.equals(MENU_PANEL) && !currentSongModel.isEmpty()) {
            mediaControlPanel.setVisible(true);
        }
        else {
            mediaControlPanel.setVisible(false);
        }
    }

    private void initializeListeners() {
        emptyMouseListener = new EmptyMouseListener();

        backToMenuListener = ae -> changePanel(MENU_PANEL);

        openFileListener = ae -> {
            File f = openFile();
            if (f != null) {
                defaultOpenDir = f.getParent();
                currentSongModel = new SongModel(f);
                openSong();
            }
        };

        viewSongsListener = ae -> {
            if (!currentSongModel.isEmpty()) {
                menuPanel.pauseSong();
            }
            changePanel(VIEW_SONGS_PANEL);
        };

        showDataListener = ae -> {
            refreshCache();
            if (!currentSongModel.isEmpty()) {
                menuPanel.pauseSong();
            }
            changePanel(DATA_PANEL);
        };

        favoriteButtonListener = ae -> {
            refreshCache();
            try {
                databaseAccessModel.addSong(currentSongModel);
                setFavorite(true);
            } catch (InvalidOperationException | SQLConnectionException e) {
                showDialog(e.getMessage());
            }
        };

        unfavoriteButtonListener = ae -> {
            refreshCache();
            try {
                databaseAccessModel.deleteSong(currentSongModel);
                setFavorite(false);
            } catch (InvalidOperationException | SQLConnectionException e) {
                showDialog(e.getMessage());
            }
        };

        changePitchListener = ae -> changePitchDialog.setVisible(true);

        cpPreviewListener = ae -> new Thread(() -> {
            glassPane.setVisible(true);
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            matlabHandler.changePitch(changePitchPanel.getFreq());
            SwingUtilities.invokeLater(() -> {
                glassPane.setVisible(false);
                setCursor(Cursor.getDefaultCursor());
            });
        }).start();

        cpSaveListener = ae -> {

        };

        // TODO savebutton listener

        cutFileListener = ae -> {
            if (!currentSongModel.isEmpty()) {
                menuPanel.pauseSong();
            }
            cutSongPanel.setCurrentSong(currentSongModel.getTotalSamples(),
                currentSongModel.getFreq(), plot,
                getExtendedState() == MAXIMIZED_BOTH);
            changePanel(CUT_SONG_PANEL);
        };

        cutDoneListener = ae -> cutSong();

        viewSpecListener = ae -> {
            if (!currentSongModel.isEmpty()) {
                menuPanel.pauseSong();
            }
            changePanel(SPECTROGRAM_PANEL);
        };

        showSpecListener = ae -> showSpec();

        viewChromListener = ae -> {
            if (!currentSongModel.isEmpty()) {
                menuPanel.pauseSong();
            }
            changePanel(CHROMAGRAM_PANEL);
        };

        showChromListener = ae -> showChrom();

        analyzeSongListener = ae -> {
            if (!currentSongModel.isEmpty()) {
                menuPanel.pauseSong();
            }
            changePanel(ANALYSIS_PANEL);
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

        editDoneListener = ae -> editSongsDone();

        addSongsListener = ae -> addSongs();

        deleteSongListener = ae -> deleteSongsDialog.setVisible(true);

        deleteDoneListener = ae -> deleteSongsDone();
    }

    private class ClosingWindowAdapter extends WindowAdapter {
        public void windowClosing(WindowEvent we) {
            glassPane.setVisible(true);
            try {
                matlabHandler.close();
            } catch (MatlabEngineException mee) {
                showDialog(mee.getMessage());
            }
            databaseAccessModel.close();
        }
    }
}
