package org.ql.audioeditor.view;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import org.ql.audioeditor.common.properties.ConfigPropertiesLoader;
import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.common.util.DoubleActionTool;
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
import org.ql.audioeditor.view.panel.popup.SimilarSongsPanel;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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

import static org.ql.audioeditor.common.util.Helper.MILLIS_SECONDS_CONVERSION;
import static org.ql.audioeditor.common.util.Helper.convertToNumber;
import static org.ql.audioeditor.common.util.Helper.framesToSeconds;
import static org.ql.audioeditor.common.util.Helper.getDir;
import static org.ql.audioeditor.common.util.Helper.getFileExtension;
import static org.ql.audioeditor.common.util.Helper.showInfo;
import static org.ql.audioeditor.view.param.Constants.ATTRIBUTES;
import static org.ql.audioeditor.view.param.Constants.BOTTOM_FIELD_SIZE;
import static org.ql.audioeditor.view.param.Constants.BOTTOM_PANEL_HEIGHT;
import static org.ql.audioeditor.view.param.Constants
    .MEDIA_CONTROL_PANEL_HEIGHT;
import static org.ql.audioeditor.view.param.Constants.OPTIONS;
import static org.ql.audioeditor.view.param.Constants.TEXT_FIELD_DIGIT_SIZE_MIN;
import static org.ql.audioeditor.view.param.Constants.WIN_MIN_SIZE;

/**
 * Main window (frame). Acts as controller.
 */
public final class MainWindow extends Window {
    /**
     * Constants.
     */
    private static final String SAVE_QUESTION =
        "Do you want to save the new file?";
    private static final String FILE_BROWSER_INFO =
        "Specify the directory and the file name";
    private static final String SHOW_SIMILAR_SONGS_TITLE = "Show similar songs";
    private static final String SHOW_SIMILAR_SONGS_INSTR =
        "Please choose an attribute.";
    private static final String LONG_SONG_INFO =
        "Warning: this song might be too long to analyze.";
    private static final String FILE_ALREADY_EXISTS_ERROR =
        "The path specified already exists.";
    private static final String WRONG_INPUT_ERROR = "Wrong input data.";
    private static final String TIMEOUT_ERROR =
        "Error: process took too long to finish.";
    private static final String INTERRUPTED_ERROR
        = "Error: the process was interrupted.";
    private static final String FILE_TYPE_ERROR =
        "This file type is not supported.";
    private static final String IMAGE_ERROR = "Cannot load image.";
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
    private static final String MAKE_FAVORITE = "makeFavoriteAction";
    private static final String SHOW_RELATED = "showRelatedAction";
    private static final Border ROOT_PANE_BORDER =
        BorderFactory.createLineBorder(Color.DARK_GRAY);
    private static final Border CURRENT_SONG_TITLE_BORDER =
        BorderFactory.createEmptyBorder(5, 5, 5, 5);
    private static final Border INFO_LABEL_BORDER =
        BorderFactory.createEmptyBorder(0, 12, 0, 12);
    private static final int INFO_LABEL_DELAY = 5000;
    private static final int BASE_NUM = 10;
    private static final int TIMEOUT_MILLIS = 30 * MILLIS_SECONDS_CONVERSION;

    /**
     * Private data members.
     */
    private final DatabaseAccessModel databaseAccessModel;
    private final MatlabHandler matlabHandler;
    private final SongTableModel viewSongTableModel;
    private final SongTableModel deleteSongTableModel;
    private final SongTableModel similarSongTableModel;
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
    private JDialog editDialog;
    private JDialog deleteSongsDialog;
    private JDialog similarSongsDialog;
    private JDialog changePitchDialog;
    private EditPanel editPanel;
    private DeleteSongsPanel deleteSongsPanel;
    private SimilarSongsPanel similarSongsPanel;
    private ChangePitchPanel changePitchPanel;
    private DataPanel dataPanel;
    private CutSongPanel cutSongPanel;
    private OptionPanel optionPanel;
    private JPanel sideBar;
    private HorizontalBar bottomPanel;
    private JLabel currentSongTitle;
    private JLabel infoLabel;
    private String defaultOpenDir;
    private Action makeFavoriteAction;
    private Action showRelatedAction;
    private Thread t;

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
    private ActionListener showSimilarDoneListener;

    /**
     * Constructor.
     *
     * @param databaseAccessModel Database access model
     * @param matlabHandler       MATLAB handler
     */
    public MainWindow(DatabaseAccessModel databaseAccessModel,
        MatlabHandler matlabHandler) {
        super();
        this.databaseAccessModel = databaseAccessModel;
        this.matlabHandler = matlabHandler;
        this.viewSongTableModel = new SongTableModel();
        this.deleteSongTableModel = new SongTableModel();
        this.similarSongTableModel = new SongTableModel();
        this.currentSongModel = new SongModel();
        this.cardLayout = new CardLayout();
        this.glassPane = getGlassPane();
        this.rootPane.setBorder(ROOT_PANE_BORDER);
        this.t = new Thread();

        initKeyBindings();
        initializeListeners();
        addListeners();
        initializeLabels();
        initializePanels();
        addPanels();
        getDefaultSong();
        setTimer();
    }

    /**
     * Starts the GUI.
     */
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

    /**
     * Hides/shows the core image according to the given boolean.
     *
     * @param isHidden Indicates whether the core image should be hidden
     */
    @Override
    protected void hideImage(boolean isHidden) {
        if (!currentSongModel.isEmpty()) {
            menuPanel.hideImage(isHidden);
            spectrogramPanel.hideImage(isHidden);
            chromagramPanel.hideImage(isHidden);
        }
    }

    /**
     * Maximizes/normalizes the core image according to the given boolean.
     *
     * @param isMaximized Indicates whether the core image should be maximized
     */
    @Override
    protected void maximizeImage(boolean isMaximized) {
        menuPanel.maximizeImage(isMaximized);
        spectrogramPanel.maximizeImage(isMaximized);
        chromagramPanel.maximizeImage(isMaximized);
        cutSongPanel.maximizeImage(isMaximized);
    }

    /**
     * Adds WindowAdapter and EmptyMouseListener.
     */
    private void addListeners() {
        addWindowListener(new ClosingWindowAdapter());
        this.glassPane.addMouseListener(emptyMouseListener);
    }

    /**
     * Gets the default song.
     */
    private void getDefaultSong() {
        defaultOpenDir = getDir(currentSongModel.getPath());
        getSavedInstance(currentSongModel);
    }

    /**
     * Gets the ID of the current song's saved instance.
     */
    private void getSavedInstance(SongModel sm) {
        if (!sm.isEmpty()) {
            try {
                int id = databaseAccessModel.getId(sm);
                sm.setId(id);
                if (sm.isDefault()) {
                    setFavorite(false);
                } else {
                    setFavorite(true);
                }
            } catch (SQLConnectionException e) {
                showMessageDialog(e.getMessage());
                menuPanel.setFavorite(false);
            }
        }
    }

    /**
     * Adds panels.
     */
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

    /**
     * Refreshes the cache.
     */
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

    /**
     * Sets the timer to refresh the cache.
     */
    private void setTimer() {
        Timer t = new Timer(ConfigPropertiesLoader.getRefreshMillis(),
            e -> refreshCache());
        t.setRepeats(true);
        t.start();
    }

    /**
     * Open a selected file.
     *
     * @return File opened
     */
    private File openFile() {
        JFileChooser fileChooser;

        if (defaultOpenDir != null && !defaultOpenDir.equals("")) {
            fileChooser = new JFileChooser(defaultOpenDir);
        } else {
            fileChooser = new JFileChooser();
        }

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Open multiple files.
     *
     * @return Files opened.
     */
    private File[] openFiles() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.showOpenDialog(this);
        return chooser.getSelectedFiles();
    }

    /**
     * Gets and saves the cover of the current song.
     */
    private void getCover(SongModel sm) {
        try {
            Mp3File song = new Mp3File(sm.getPath());
            if (song.hasId3v2Tag()) {
                ID3v2 id3v2tag = song.getId3v2Tag();
                byte[] imageData = id3v2tag.getAlbumImage();
                sm.setCover(ImageIO.read(new ByteArrayInputStream(imageData)));
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Sets certain GUI elements according to the current song's being/not being
     * the database.
     *
     * @param isFavorite True if the current song is already in the database
     */
    private void setFavorite(boolean isFavorite) {
        menuPanel.setFavorite(isFavorite);
        currentSongModel.setSaved(isFavorite);
        if (isFavorite) {
            dataPanel.setSongData(currentSongModel);
        } else {
            currentSongModel.setDefault();
        }
    }

    /**
     * Interrupts a thread if it is still running after a given time.
     *
     * @param t Thread
     */
    private void waitForThread(Thread t) {
        try {
            glassPane.setVisible(true);
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            t.start();
            t.join(TIMEOUT_MILLIS);
            if (t.isAlive()) {
                t.interrupt();
                showInfo(infoLabel, TIMEOUT_ERROR, INFO_LABEL_DELAY);
            }
        } catch (InterruptedException e) {
            showInfo(infoLabel, INTERRUPTED_ERROR, INFO_LABEL_DELAY);
        } finally {
            setCursor(Cursor.getDefaultCursor());
            glassPane.setVisible(false);
        }
    }

    /**
     * Loads the song provided.
     *
     * @param sm Song model
     */
    private void loadSong(SongModel sm) {
        defaultOpenDir = getDir(currentSongModel.getPath());
        openSong(sm);
    }

    /**
     * Opens the current song.
     */
    private void openSong(SongModel sm) {
        getCover(sm);
        getSavedInstance(sm);

        t = new Thread(new OpenSongRunnable(sm));
        waitForThread(t);
    }

    /**
     * Show spectrogram.
     */
    private void showSpec() {
        String windowSizeString = spectrogramPanel.getWindowSize();
        String hopSizeString = spectrogramPanel.getHopSize();
        String nfftString = spectrogramPanel.getNfft();
        String window = spectrogramPanel.getWindow();
        int windowSize = convertToNumber(windowSizeString);
        int hopSize = convertToNumber(hopSizeString);
        int nfft = convertToNumber(nfftString);
        if (checkNumbers(windowSize, hopSize, nfft)) {
            t = new Thread(new ShowSpecRunnable(windowSize, hopSize, nfft,
                window));
            waitForThread(t);
        }
    }

    /**
     * Show chromagram.
     */
    private void showChrom() {
        String windowSizeString = chromagramPanel.getWindowSize();
        String hopSizeString = chromagramPanel.getHopSize();
        String nfftString = chromagramPanel.getNfft();
        String window = chromagramPanel.getWindow();
        int windowSize = convertToNumber(windowSizeString);
        int hopSize = convertToNumber(hopSizeString);
        int nfft = convertToNumber(nfftString);
        if (checkNumbers(windowSize, hopSize, nfft)) {
            t = new Thread(new ShowChromRunnable(windowSize, hopSize, nfft,
                window));
            waitForThread(t);
        }
    }

    /**
     * Checks whether the numeric input fetched from the spectrogram/chromagram
     * chooser is correct.
     *
     * @param windowSize Window size
     * @param hopSize    Hop size
     * @param nfft       Number of FFT points
     * @return Logical value
     */
    private boolean checkNumbers(int windowSize, int
        hopSize, int nfft) {
        if (windowSize < Math.pow(BASE_NUM, TEXT_FIELD_DIGIT_SIZE_MIN - 1)
            || hopSize < Math.pow(BASE_NUM, TEXT_FIELD_DIGIT_SIZE_MIN - 1)
            || nfft < Math.pow(BASE_NUM, TEXT_FIELD_DIGIT_SIZE_MIN - 1)
            || windowSize <= hopSize) {
            showInfo(infoLabel, WRONG_INPUT_ERROR, INFO_LABEL_DELAY);
            return false;
        }
        return true;
    }

    /**
     * Cuts the current song.
     */
    private void cutSong() {
        t = new Thread(new CutSongRunnable());
        waitForThread(t);
    }

    /**
     * Edits a song in the database.
     */
    private void editSongDone() {
        try {
            editPanel.setNewData();
            SongModel sm = editPanel.getSelectedSongModel();
            if (sm.getId() == currentSongModel.getId()) {
                currentSongModel = sm;
            }
            sm.setTags();
            databaseAccessModel.editSong(editPanel.getSelectedSongModel());
            showMessageDialog(SUCCESSFUL_OPERATION_INFO);
        } catch (InvalidOperationException | SQLConnectionException e) {
            showMessageDialog(e.getMessage());
        } catch (Exception ignored) {
        }
        editDialog.setVisible(false);
    }

    /**
     * Adds songs to the database.
     */
    private void addSongs() {
        File[] files = openFiles();
        List<File> validFiles = new ArrayList<>();
        for (File f : files) {
            if (!Arrays.asList(SongPropertiesLoader.getExtensionNames())
                .contains(getFileExtension(f))) {
                showInfo(infoLabel, FILE_TYPE_ERROR, INFO_LABEL_DELAY);
            } else {
                validFiles.add(f);
            }
        }
        SongListModel songListModel = new SongListModel(validFiles);
        try {
            databaseAccessModel.addSongs(songListModel);
            showMessageDialog(SUCCESSFUL_OPERATION_INFO);
        } catch (InvalidOperationException | SQLConnectionException e) {
            showMessageDialog(e.getMessage());
        }
    }

    /**
     * Deletes songs previously chosen by the user.
     */
    private void deleteSongsDone() {
        deleteSongsDialog.setVisible(false);
        try {
            SongListModel slm = deleteSongsPanel.getSelectedRows();
            if (slm != null) {
                databaseAccessModel.deleteSongs(slm);
            }
            showMessageDialog(SUCCESSFUL_OPERATION_INFO);
        } catch (InvalidOperationException | SQLConnectionException e) {
            showMessageDialog(e.getMessage());
        }
    }

    /**
     * Adds the current song to the database (removes the current song from the
     * database).
     */
    private void setFavorite() {
        refreshCache();
        try {
            if (!currentSongModel.isSaved()) {
                databaseAccessModel.addSong(currentSongModel);
                setFavorite(true);
            } else {
                databaseAccessModel.deleteSong(currentSongModel);
                setFavorite(false);
            }
        } catch (InvalidOperationException | SQLConnectionException e) {
            showMessageDialog(e.getMessage());
        }
    }

    /**
     * Creates dialog for showing related songs.
     */
    private void showRelated() {
        String attribute = (String) JOptionPane
            .showInputDialog(this,
                SHOW_SIMILAR_SONGS_INSTR,
                SHOW_SIMILAR_SONGS_TITLE,
                JOptionPane.QUESTION_MESSAGE,
                null,
                ATTRIBUTES,
                ATTRIBUTES[0]);
        if (attribute != null) {
            try {
                similarSongsPanel
                    .setList(databaseAccessModel.getSongList(attribute,
                        currentSongModel));
                similarSongsDialog.setVisible(true);
            } catch (SQLConnectionException e) {
                showMessageDialog(e.getMessage());
            }
        }
    }

    /**
     * Initializes key actions and bindings.
     */
    private void initKeyBindings() {
        makeFavoriteAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (menuPanel.isActive()
                    && !DoubleActionTool.isDoubleAction(e)) {
                    setFavorite();
                }
            }
        };
        showRelatedAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (menuPanel.isActive()
                    && !DoubleActionTool.isDoubleAction(e)) {
                    showRelated();
                }
            }
        };

        KeyStroke fKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F, 0);
        KeyStroke rKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, 0);
        inputMap.put(fKeyStroke, MAKE_FAVORITE);
        actionMap.put(MAKE_FAVORITE, makeFavoriteAction);
        inputMap.put(rKeyStroke, SHOW_RELATED);
        actionMap.put(SHOW_RELATED, showRelatedAction);
    }

    /**
     * Initializes labels.
     */
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

    /**
     * Initializes panels.
     */
    private void initializePanels() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        innerMainPanel = new JPanel(cardLayout);
        innerMainPanel.setVisible(true);
        mediaControlPanel = new InverseHorizontalBar(new BorderLayout());
        mediaControlPanel.setHeight(MEDIA_CONTROL_PANEL_HEIGHT);

        menuPanel =
            new MenuPanel(matlabHandler, mediaControlPanel, inputMap, actionMap,
                favoriteButtonListener, unfavoriteButtonListener,
                showSimilarSongsListener);
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

        similarSongsPanel =
            new SimilarSongsPanel(similarSongTableModel,
                showSimilarDoneListener);
        similarSongsDialog =
            new PopupDialog(this, "Similar songs", similarSongsPanel);
        similarSongsDialog.setVisible(false);

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
            new ChangePitchPanel(matlabHandler, cpPreviewListener,
                cpSaveListener);
        changePitchDialog =
            new PopupDialog(this, "Change pitch", changePitchPanel, new
                ClosingChangePitchAdapter());
        changePitchDialog.setVisible(false);
        changePitchDialog.setMinimumSize(changePitchDialog.getSize());

        bottomPanel = new HorizontalBar();
        bottomPanel.add(currentSongTitle, BorderLayout.WEST);
        bottomPanel.add(infoLabel, BorderLayout.EAST);
        bottomPanel.setHeight(BOTTOM_PANEL_HEIGHT);
    }

    /**
     * Switches panel.
     *
     * @param name Panel to switch to
     */
    private void changePanel(String name) {
        cardLayout.show(innerMainPanel, name);
        if (name.equals(MENU_PANEL) && !currentSongModel.isEmpty()) {
            mediaControlPanel.setVisible(true);
            menuPanel.setActive(true);
        } else {
            menuPanel.setActive(false);
            mediaControlPanel.setVisible(false);
        }
    }

    /**
     * Initializes listeners.
     */
    private void initializeListeners() {
        emptyMouseListener = new EmptyMouseListener();

        backToMenuListener = ae -> changePanel(MENU_PANEL);

        openFileListener = ae -> {
            File f = openFile();
            if (f != null) {
                defaultOpenDir = f.getParent();
                openSong(new SongModel(f));
            }
        };

        viewSongsListener = ae -> {
            if (!currentSongModel.isEmpty()) {
                menuPanel.pauseSong();
                changePanel(VIEW_SONGS_PANEL);
            }
        };

        showDataListener = ae -> {
            refreshCache();
            if (!currentSongModel.isEmpty()) {
                menuPanel.pauseSong();
                changePanel(DATA_PANEL);
            }
        };

        showSimilarSongsListener = ae -> showRelated();

        favoriteButtonListener = ae -> setFavorite();

        unfavoriteButtonListener = ae -> setFavorite();

        changePitchListener = ae -> {
            if (!currentSongModel.isEmpty()) {
                menuPanel.stopSong();
                changePitchDialog.setVisible(true);
            }
        };

        cpPreviewListener = ae -> {
            menuPanel.stopSong();
            t = new Thread(new PreviewSongRunnable());
            waitForThread(t);
        };

        cpSaveListener = ae -> {
            //TODO Load save button after preview?
            int result = showSaveDialog();
            switch (result) {
                case JOptionPane.YES_OPTION:
                    break;
                case JOptionPane.NO_OPTION:
                    break;
                default:
                    break;
            }
        };

        cutFileListener = ae -> {
            if (!currentSongModel.isEmpty()) {
                menuPanel.pauseSong();
                cutSongPanel.setCurrentSong(currentSongModel.getTotalSamples(),
                    currentSongModel.getFreq(), currentSongModel.getPlot(),
                    getExtendedState() == MAXIMIZED_BOTH);
                changePanel(CUT_SONG_PANEL);
            }
        };

        cutDoneListener = ae -> cutSong();

        viewSpecListener = ae -> {
            if (!currentSongModel.isEmpty()) {
                menuPanel.pauseSong();
                changePanel(SPECTROGRAM_PANEL);
            }
        };

        showSpecListener = ae -> showSpec();

        viewChromListener = ae -> {
            if (!currentSongModel.isEmpty()) {
                menuPanel.pauseSong();
                changePanel(CHROMAGRAM_PANEL);
            }
        };

        showChromListener = ae -> showChrom();

        analyzeSongListener = ae -> {
            if (!currentSongModel.isEmpty()) {
                menuPanel.pauseSong();
                changePanel(ANALYSIS_PANEL);
            }
            // TODO
        };

        loadSongListener = ae -> {
            SongModel sm = viewSongsPanel.getSelectedRow();
            if (sm != null) {
                loadSong(sm);
            }
        };

        editSongListener = ae -> {
            SongModel sm = viewSongsPanel.getSelectedRow();
            if (sm != null) {
                editPanel.setSelectedSong(sm);
                editDialog.setVisible(true);
            }
        };

        editDoneListener = ae -> editSongDone();

        addSongsListener = ae -> addSongs();

        deleteSongListener = ae -> deleteSongsDialog.setVisible(true);

        deleteDoneListener = ae -> deleteSongsDone();

        showSimilarDoneListener = ae -> {
            SongModel sm = similarSongsPanel.getSelectedRow();
            if (sm != null) {
                loadSong(sm);
            }
        };
    }

    private int showSaveDialog() {
        return JOptionPane.showOptionDialog(this,
            SAVE_QUESTION, null, JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, OPTIONS,
            OPTIONS[0]);
    }

    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Window adapter for defining actions to execute after exit.
     */
    private final class ClosingWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent we) {
            glassPane.setVisible(true);
            try {
                matlabHandler.close();
            } catch (MatlabEngineException mee) {
                JOptionPane
                    .showMessageDialog(MainWindow.this, mee.getMessage());
            }
            databaseAccessModel.close();
        }
    }

    /**
     * Window adapter for defining actions to execute after exiting the
     * ChangePitchPanel.
     */
    private final class ClosingChangePitchAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent we) {
            changePitchPanel.removeSong();
            t = new Thread(() -> {
                matlabHandler.stopSong();
                matlabHandler.changePitch(currentSongModel.getFreq());
            });
            waitForThread(t);
        }
    }

    /**
     * Runnable for opening songs.
     */
    private final class OpenSongRunnable implements Runnable {
        private final SongModel sm;

        private OpenSongRunnable(SongModel sm) {
            this.sm = sm;
        }

        @Override
        public synchronized void run() {
            try {
                matlabHandler.openSong(sm.getPath());
                matlabHandler
                    .plotSong(ImageLoader.getPlotImagePath());
                matlabHandler.passData(sm);
                SwingUtilities.invokeLater(() -> {
                    double freq = sm.getFreq();
                    double totalSamples = sm.getTotalSamples();
                    try {
                        BufferedImage plot = ImageIO.read(
                            new File(ImageLoader.getPlotImagePath()));
                        sm.setPlot(plot);
                        menuPanel.stopSong();
                        menuPanel
                            .setCurrentSong(totalSamples, freq, plot,
                                sm.getCover(), isNormal,
                                getExtendedState() == MAXIMIZED_BOTH);
                        changePitchPanel.setSong(totalSamples, freq, plot);
                        currentSongTitle.setText(sm.getTitle());
                        spectrogramPanel.removeImages();
                        chromagramPanel.removeImages();
                        optionPanel.showOptions(true);
                        currentSongModel = sm;
                        changePanel(MENU_PANEL);
                        if (framesToSeconds(totalSamples, freq)
                            > SongPropertiesLoader.getMaxSeconds()) {
                            showInfo(infoLabel, LONG_SONG_INFO,
                                INFO_LABEL_DELAY);
                        }
                    } catch (IOException ioe) {
                        showMessageDialog(IMAGE_ERROR);
                    }
                });
            } catch (MatlabEngineException mee) {
                new Thread(new DialogRunnable(mee.getMessage())).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Runnable for generating spectrograms.
     */
    private final class ShowSpecRunnable implements Runnable {
        private final int windowSize;
        private final int hopSize;
        private final int nfft;
        private final String window;

        private ShowSpecRunnable(int windowSize, int hopSize, int nfft,
            String window) {
            this.windowSize = windowSize;
            this.hopSize = hopSize;
            this.nfft = nfft;
            this.window = window;
        }

        @Override
        public synchronized void run() {
            try {
                matlabHandler
                    .showSpectrogram(windowSize, hopSize, nfft, window,
                        ImageLoader.getSpecImagePath(),
                        ImageLoader.getSpec3dImagePath());
                SwingUtilities.invokeLater(() -> {
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
                new Thread(new DialogRunnable(mee.getMessage())).start();
            }
        }
    }

    /**
     * Runnable for showing chromagrams.
     */
    private final class ShowChromRunnable implements Runnable {
        private final int windowSize;
        private final int hopSize;
        private final int nfft;
        private final String window;

        private ShowChromRunnable(int windowSize, int hopSize, int nfft,
            String window) {
            this.windowSize = windowSize;
            this.hopSize = hopSize;
            this.nfft = nfft;
            this.window = window;
        }

        @Override
        public synchronized void run() {
            try {
                matlabHandler
                    .showChromagram(windowSize, hopSize, nfft, window,
                        ImageLoader.getChromImagePath());
                SwingUtilities.invokeLater(() -> {
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
                new Thread(new DialogRunnable(mee.getMessage())).start();
            }
        }
    }

    /**
     * Runnable for cutting songs.
     */
    private final class CutSongRunnable implements Runnable {
        @Override
        public synchronized void run() {
            try {
                matlabHandler
                    .cutSong(cutSongPanel.getFrom(), cutSongPanel.getTo());
                SwingUtilities.invokeLater(() -> {
                    int result = showSaveDialog();
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            saveAs();
                            break;
                        case JOptionPane.NO_OPTION:
                            save();
                        default:
                            break;
                    }
                });
            } catch (MatlabEngineException mee) {
                new Thread(new DialogRunnable(mee.getMessage())).start();
                cutSongPanel.setDefaultValues();
            }
        }

        private void saveAs() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(FILE_BROWSER_INFO);
            int userSelection = fileChooser
                .showSaveDialog(MainWindow.this);
            try {
                if (userSelection
                    == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser
                        .getSelectedFile();
                    if (fileToSave.exists()) {
                        showMessageDialog(FILE_ALREADY_EXISTS_ERROR);
                    } else {
                        matlabHandler.saveSong(
                            fileToSave.getAbsolutePath());
                    }
                }
            } catch (MatlabEngineException mee) {
                showMessageDialog(mee.getMessage());
            }
        }

        private void save() {
            try {
                matlabHandler
                    .saveSong(currentSongModel.getPath());
                openSong(currentSongModel);
            } catch (MatlabEngineException mee) {
                showMessageDialog(mee.getMessage());
            }
        }
    }

    /**
     * Runnable for previewing changed songs.
     */
    private final class PreviewSongRunnable implements Runnable {
        @Override
        public synchronized void run() {
            try {
                double newFreq = changePitchPanel.getFreq();
                matlabHandler.changePitch(newFreq);
                SwingUtilities.invokeLater(() -> {
                    changePitchPanel.setFreq(newFreq);
                    changePitchDialog.pack();
                    changePitchDialog.setMinimumSize(changePitchDialog.getSize());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Runnable for displaying message dialogs.
     */
    private final class DialogRunnable implements Runnable {
        private final String message;

        private DialogRunnable(String message) {
            this.message = message;
        }

        @Override
        public synchronized void run() {
            showMessageDialog(message);
        }
    }
}
