package org.ql.audioeditor.view;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import org.ql.audioeditor.common.properties.ConfigPropertiesLoader;
import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.common.util.DoubleActionTool;
import org.ql.audioeditor.logic.dbaccess.DatabaseAccessModel;
import org.ql.audioeditor.logic.dbaccess.SongListModel;
import org.ql.audioeditor.logic.dbaccess.SongModel;
import org.ql.audioeditor.logic.dbaccess.SongTableModel;
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
import org.ql.audioeditor.view.enums.PanelName;
import org.ql.audioeditor.view.panel.CutSongPanel;
import org.ql.audioeditor.view.panel.DataPanel;
import org.ql.audioeditor.view.panel.MenuPanel;
import org.ql.audioeditor.view.panel.OptionPanel;
import org.ql.audioeditor.view.panel.ViewSongsPanel;
import org.ql.audioeditor.view.panel.analysis.AnalysisPanel;
import org.ql.audioeditor.view.panel.analysis.ChromagramPanel;
import org.ql.audioeditor.view.panel.analysis.SpectrogramPanel;
import org.ql.audioeditor.view.panel.popup.ChangePitchPanel;
import org.ql.audioeditor.view.panel.popup.DeleteSongsPanel;
import org.ql.audioeditor.view.panel.popup.EditPanel;
import org.ql.audioeditor.view.panel.popup.SimilarSongsPanel;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
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

import static org.ql.audioeditor.common.util.GeneralUtils.convertToNumber;
import static org.ql.audioeditor.common.util.GeneralUtils.getDir;
import static org.ql.audioeditor.common.util.GeneralUtils.getFileExtension;
import static org.ql.audioeditor.common.util.TimeUtils
    .MILLIS_SECONDS_CONVERSION;
import static org.ql.audioeditor.common.util.TimeUtils.framesToSeconds;
import static org.ql.audioeditor.common.util.ViewUtils.showDialog;
import static org.ql.audioeditor.common.util.ViewUtils.showInfo;
import static org.ql.audioeditor.view.enums.ActionName.MAKE_FAVORITE;
import static org.ql.audioeditor.view.enums.ActionName.SHOW_RELATED;
import static org.ql.audioeditor.view.enums.Message.FILE_ALREADY_EXISTS_ERROR;
import static org.ql.audioeditor.view.enums.Message.FILE_TYPE_ERROR;
import static org.ql.audioeditor.view.enums.Message.IMAGE_ERROR;
import static org.ql.audioeditor.view.enums.Message.INTERRUPTED_ERROR;
import static org.ql.audioeditor.view.enums.Message.LONG_SONG_INFO;
import static org.ql.audioeditor.view.enums.Message.SONGS_DELETED_INFO;
import static org.ql.audioeditor.view.enums.Message.SONG_TOO_SHORT_ERROR;
import static org.ql.audioeditor.view.enums.Message.SUCCESSFUL_OPERATION_INFO;
import static org.ql.audioeditor.view.enums.Message.TIMEOUT_ERROR;
import static org.ql.audioeditor.view.enums.Message.WRONG_INPUT_ERROR;
import static org.ql.audioeditor.view.enums.PanelName.ANALYSIS_PANEL;
import static org.ql.audioeditor.view.enums.PanelName.CHROMAGRAM_PANEL;
import static org.ql.audioeditor.view.enums.PanelName.CUT_SONG_PANEL;
import static org.ql.audioeditor.view.enums.PanelName.DATA_PANEL;
import static org.ql.audioeditor.view.enums.PanelName.MEDIA_CONTROL_PANEL;
import static org.ql.audioeditor.view.enums.PanelName.MENU_PANEL;
import static org.ql.audioeditor.view.enums.PanelName.ONSET_MEDIA_CONTROL_PANEL;
import static org.ql.audioeditor.view.enums.PanelName.SPECTROGRAM_PANEL;
import static org.ql.audioeditor.view.enums.PanelName.VIEW_SONGS_PANEL;
import static org.ql.audioeditor.view.param.Constants.ATTRIBUTES;
import static org.ql.audioeditor.view.param.Constants.BOTTOM_FIELD_SIZE;
import static org.ql.audioeditor.view.param.Constants.BOTTOM_PANEL_HEIGHT;
import static org.ql.audioeditor.view.param.Constants.CURRENT_SONG_TITLE_BORDER;
import static org.ql.audioeditor.view.param.Constants.INFO_LABEL_BORDER;
import static org.ql.audioeditor.view.param.Constants
    .MEDIA_CONTROL_PANEL_HEIGHT;
import static org.ql.audioeditor.view.param.Constants.OPTIONS;
import static org.ql.audioeditor.view.param.Constants.ROOT_PANE_BORDER;
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
    private static final String FILE_BROWSER_INSTR =
        "Specify the directory and the file name";
    private static final String SHOW_SIMILAR_SONGS_TITLE = "Show similar songs";
    private static final String SHOW_SIMILAR_SONGS_INSTR =
        "Please choose a filter.";

    private static final String PLOT_IMG_PATH =
        ImageLoader.getPlotImagePath();
    private static final String SPEC_IMG_PATH =
        ImageLoader.getSpecImagePath();
    private static final String SPEC_3D_IMG_PATH =
        ImageLoader.getSpec3dImagePath();
    private static final String CHROM_IMG_PATH =
        ImageLoader.getChromImagePath();
    private static final String ONSET_IMG_PATH =
        ImageLoader.getOnsetImagePath();
    private static final int INFO_LABEL_DELAY = 5000;
    private static final int BASE_NUM = 10;
    private static final int TIMEOUT_MILLIS =
        ConfigPropertiesLoader.getTimeoutSeconds() * MILLIS_SECONDS_CONVERSION;

    /**
     * Private data members.
     */
    private final DatabaseAccessModel databaseAccessModel;
    private final MatlabHandler matlabHandler;
    private final SongTableModel viewSongTableModel;
    private final SongTableModel deleteSongTableModel;
    private final SongTableModel similarSongTableModel;
    private final CardLayout cardLayout;
    private final CardLayout lowerCardLayout;
    private final Component glassPane;
    private SongModel currentSongModel;
    private SongListModel currentSongListModel;
    private JPanel mainPanel;
    private JPanel innerMainPanel;
    private HorizontalBar mediaControlPanel;
    private HorizontalBar onsetMediaControlPanel;
    private JPanel lowerBar;
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
    private ActionListener showPreviousListener;
    private ActionListener showNextListener;
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
    private ActionListener beatEstListener;
    private ActionListener onsetDetListener;
    private ActionListener keyDetListener;
    private ActionListener beatEstDoneListener;
    private ActionListener onsetDetDoneListener;
    private ActionListener keyDetDoneListener;

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
        this.currentSongListModel = new SongListModel();
        this.cardLayout = new CardLayout();
        this.lowerCardLayout = new CardLayout();
        this.glassPane = getGlassPane();
        this.rootPane.setBorder(ROOT_PANE_BORDER);
        this.t = new Thread();

        initKeyBindings();
        initializeGeneralListeners();
        initializeEditListeners();
        initializeAnListeners();
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
            lowerBar.setVisible(false);
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
            analysisPanel.hideImage(isHidden);
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
        analysisPanel.maximizeImage(isMaximized);
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
                showDialog(e.getMessage());
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
        mainPanel.add(lowerBar, BorderLayout.SOUTH);
        innerMainPanel.add(menuPanel, MENU_PANEL.toString());
        innerMainPanel.add(viewSongsPanel, VIEW_SONGS_PANEL.toString());
        innerMainPanel.add(dataPanel, DATA_PANEL.toString());
        innerMainPanel.add(analysisPanel, ANALYSIS_PANEL.toString());
        innerMainPanel.add(spectrogramPanel, SPECTROGRAM_PANEL.toString());
        innerMainPanel.add(chromagramPanel, CHROMAGRAM_PANEL.toString());
        innerMainPanel.add(cutSongPanel, CUT_SONG_PANEL.toString());
        lowerBar.add(mediaControlPanel, MEDIA_CONTROL_PANEL.toString());
        lowerBar.add(onsetMediaControlPanel,
            ONSET_MEDIA_CONTROL_PANEL.toString());
    }

    /**
     * Refreshes the cache.
     */
    private void refreshCache() {
        try {
            currentSongListModel = databaseAccessModel.getSongList();
            viewSongsPanel.setList(currentSongListModel);
            deleteSongsPanel.setList(currentSongListModel);
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
            t = new Thread(new ShowChromRunnable(
                windowSize, hopSize, nfft, window));
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
    private boolean checkNumbers(int windowSize, int hopSize, int nfft) {
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
     * Checks whether the numeric input fetched from the BPM setter is correct.
     *
     * @param minBpm Minimum BPM
     * @param maxBpm Maximum BPM
     * @return Logical value
     */
    private boolean checkNumbersBeat(int minBpm, int maxBpm) {
        if (minBpm > maxBpm) {
            showInfo(infoLabel, WRONG_INPUT_ERROR, INFO_LABEL_DELAY);
            return false;
        }
        return true;
    }

    /**
     * Checks whether the numeric input fetched from the onset detector is
     * correct.
     *
     * @param bpm        BPM
     * @param smoothness Filter size
     * @return Logical value
     */
    private boolean checkNumbersOnset(int bpm, int smoothness) {
        if (bpm == 0 || smoothness <= 0) {
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

        int result = showSaveDialog();
        switch (result) {
            case JOptionPane.YES_OPTION:
                String path = saveAsNewFile();
                if (path != null) {
                    t = new Thread(new CutSongDoneRunnable(path));
                    waitForThread(t);
                }
                break;
            case JOptionPane.NO_OPTION:
                t = new Thread(new CutSongDoneRunnable(currentSongModel
                    .getPath()));
                waitForThread(t);
                openSong(currentSongModel);
            default:
                break;
        }
    }

    /**
     * Get a path to save a new file to.
     *
     * @return Path
     */
    private String saveAsNewFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(FILE_BROWSER_INSTR);
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (fileToSave.exists()) {
                showDialog(FILE_ALREADY_EXISTS_ERROR);
                return null;
            }
            return fileToSave.getAbsolutePath();
        }
        return null;
    }

    /**
     * Edits a song in the database.
     */
    private void editSongDone() {
        try {
            editPanel.setNewData();
            SongModel sm = editPanel.getSelectedSongModel();
            if (sm.getId() == currentSongModel.getId()) {
                currentSongModel = new SongModel(sm);
            }
            try {
                sm.setTags();
            } catch (Exception ignored) {
            }
            databaseAccessModel.editSong(sm);
            refreshCache();
            showDialog(SUCCESSFUL_OPERATION_INFO);
        } catch (InvalidOperationException | SQLConnectionException e) {
            showDialog(e.getMessage());
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
        if (!validFiles.isEmpty()) {
            SongListModel songListModel = new SongListModel(validFiles);
            try {
                databaseAccessModel.addSongs(songListModel);
                showDialog(SUCCESSFUL_OPERATION_INFO);
            } catch (InvalidOperationException | SQLConnectionException e) {
                showDialog(e.getMessage());
            }
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
            showDialog(SUCCESSFUL_OPERATION_INFO);
        } catch (InvalidOperationException | SQLConnectionException e) {
            showDialog(e.getMessage());
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
            showDialog(e.getMessage());
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
                if (attribute.equals(ATTRIBUTES[0])) {
                    similarSongsPanel.setList(
                        databaseAccessModel.getSongList());
                } else {
                    similarSongsPanel
                        .setList(databaseAccessModel.getSongList(attribute,
                            currentSongModel));
                }
                int i = similarSongTableModel.indexOf(currentSongModel);
                similarSongsPanel.setCurrentSongIndex(i);
                similarSongsDialog.setVisible(true);
            } catch (SQLConnectionException e) {
                showDialog(e.getMessage());
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
        onsetMediaControlPanel = new InverseHorizontalBar(new BorderLayout());
        onsetMediaControlPanel.setHeight(MEDIA_CONTROL_PANEL_HEIGHT);
        lowerBar = new JPanel(lowerCardLayout);

        menuPanel =
            new MenuPanel(matlabHandler, mediaControlPanel, inputMap, actionMap,
                favoriteButtonListener, unfavoriteButtonListener,
                showPreviousListener, showNextListener,
                showSimilarSongsListener);
        viewSongsPanel =
            new ViewSongsPanel(viewSongTableModel, loadSongListener,
                addSongsListener, editSongListener, deleteSongListener,
                backToMenuListener);

        editPanel = new EditPanel(editDoneListener);
        editDialog = new PopupDialog(this, "Edit song", editPanel);
        editDialog.setMinimumSize(editDialog.getSize());
        editDialog.setVisible(false);

        deleteSongsPanel =
            new DeleteSongsPanel(deleteSongTableModel, deleteDoneListener);
        deleteSongsDialog =
            new PopupDialog(this, "Delete songs", deleteSongsPanel);
        deleteSongsDialog.setMinimumSize(deleteSongsDialog.getSize());
        deleteSongsDialog.setVisible(false);

        similarSongsPanel = new SimilarSongsPanel(similarSongTableModel,
            showSimilarDoneListener);
        similarSongsDialog =
            new PopupDialog(this, "Similar songs", similarSongsPanel);
        similarSongsDialog.setMinimumSize(similarSongsDialog.getSize());
        similarSongsDialog.setVisible(false);

        changePitchPanel =
            new ChangePitchPanel(matlabHandler, cpPreviewListener,
                cpSaveListener);
        changePitchDialog =
            new PopupDialog(this, "Change pitch", changePitchPanel, new
                ClosingChangePitchAdapter());
        changePitchDialog.setMinimumSize(changePitchDialog.getSize());
        changePitchDialog.setVisible(false);

        dataPanel = new DataPanel(currentSongModel, backToMenuListener);
        cutSongPanel = new CutSongPanel(cutDoneListener, backToMenuListener);

        optionPanel = new OptionPanel(backToMenuListener, openFileListener,
            viewSongsListener, showDataListener, changePitchListener,
            cutFileListener, viewSpecListener, viewChromListener,
            analyzeSongListener);
        sideBar = new SideBar();
        sideBar.add(optionPanel);

        analysisPanel = new AnalysisPanel(matlabHandler,
            onsetMediaControlPanel, beatEstListener,
            onsetDetListener, keyDetListener, backToMenuListener,
            beatEstDoneListener, onsetDetDoneListener, keyDetDoneListener);
        spectrogramPanel =
            new SpectrogramPanel(showSpecListener, backToMenuListener);
        chromagramPanel =
            new ChromagramPanel(showChromListener, backToMenuListener);

        bottomPanel = new HorizontalBar();
        bottomPanel.add(currentSongTitle, BorderLayout.WEST);
        bottomPanel.add(infoLabel, BorderLayout.EAST);
        bottomPanel.setHeight(BOTTOM_PANEL_HEIGHT);
    }

    /**
     * Switches panel.
     *
     * @param panelName Panel to switch to
     */
    private void changePanel(PanelName panelName) {
        cardLayout.show(innerMainPanel, panelName.toString());
        if (!currentSongModel.isEmpty()) {
            if (panelName.equals(MENU_PANEL)) {
                lowerCardLayout.show(lowerBar, MEDIA_CONTROL_PANEL.toString());
                menuPanel.setActive(true);
                lowerBar.setVisible(true);
            } else if (panelName.equals(ANALYSIS_PANEL)) {
                lowerCardLayout
                    .show(lowerBar, ONSET_MEDIA_CONTROL_PANEL.toString());
                menuPanel.setActive(false);
                if (analysisPanel.isMediaControlActive()) {
                    lowerBar.setVisible(true);
                } else {
                    lowerBar.setVisible(false);
                }
            } else {
                menuPanel.setActive(false);
                lowerBar.setVisible(false);
            }
        }
    }

    /**
     * Initializes listeners.
     */
    private void initializeGeneralListeners() {
        emptyMouseListener = new EmptyMouseListener();

        backToMenuListener = ae -> {
            if (analysisPanel.isVisible() && analysisPanel
                .isMediaControlActive()) {
                analysisPanel.stopSong();
                analysisPanel.resetVolume();
            }
            changePanel(MENU_PANEL);
        };

        openFileListener = ae -> {
            File f = openFile();
            if (f != null) {
                defaultOpenDir = f.getParent();
                if (!currentSongModel.isEmpty()) {
                    menuPanel.stopSong();
                    menuPanel.resetVolume();
                }
                openSong(new SongModel(f));
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
                changePanel(DATA_PANEL);
            }
        };

        showSimilarSongsListener = ae -> showRelated();

        showPreviousListener = ae -> {
            if (menuPanel.isActive() && !DoubleActionTool.isDoubleAction(ae)) {
                SongModel sm =
                    currentSongListModel.getPreviousSong(currentSongModel);
                if (sm != null) {
                    openSong(sm);
                } else {
                    menuPanel.stopSong();
                }
                menuPanel.playSong();
            }
        };

        showNextListener = ae -> {
            if (menuPanel.isActive() && !DoubleActionTool.isDoubleAction(ae)) {
                SongModel sm =
                    currentSongListModel.getNextSong(currentSongModel);
                if (sm != null) {
                    openSong(sm);
                    menuPanel.playSong();
                } else {
                    menuPanel.stopSong();
                }
            }
        };

        favoriteButtonListener = ae -> setFavorite();

        unfavoriteButtonListener = ae -> setFavorite();

        loadSongListener = ae -> {
            SongModel sm = viewSongsPanel.getSelectedRow();
            if (sm != null) {
                defaultOpenDir = getDir(sm.getPath());
                openSong(sm);
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
                openSong(sm);
            }
        };
    }

    private void initializeEditListeners() {
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
            int result = showSaveDialog();
            switch (result) {
                case JOptionPane.YES_OPTION:
                    String path = saveAsNewFile();
                    if (path != null) {
                        t = new Thread(new ChangePitchRunnable(path));
                        waitForThread(t);
                    }
                    break;
                case JOptionPane.NO_OPTION:
                    t = new Thread(new ChangePitchRunnable(currentSongModel
                        .getPath()));
                    waitForThread(t);
                    openSong(currentSongModel);
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
    }

    private void initializeAnListeners() {
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
                if (framesToSeconds(currentSongModel.getTotalSamples(),
                    currentSongModel.getFreq()) < 2) {
                    showDialog(SONG_TOO_SHORT_ERROR);
                } else {
                    if (analysisPanel.isMediaControlActive()) {
                        lowerBar.setVisible(true);
                    }
                    menuPanel.stopSong();
                    menuPanel.resetVolume();
                    changePanel(ANALYSIS_PANEL);
                }
            }
        };

        beatEstListener = ae -> analysisPanel.setBeatEstOn();

        onsetDetListener = ae -> {
            analysisPanel.setOnsetDetOn();
            if (analysisPanel.isMediaControlActive()) {
                lowerBar.setVisible(true);
            }
        };

        keyDetListener = ae -> analysisPanel.setKeyDetOn();

        beatEstDoneListener = ae -> {
            int minBpm = analysisPanel.getMinBpm();
            int maxBpm = analysisPanel.getMaxBpm();
            if (checkNumbersBeat(minBpm, maxBpm)) {
                t = new Thread(new BeatEstRunnable(minBpm, maxBpm));
                waitForThread(t);
            }
        };

        onsetDetDoneListener = ae -> {
            String bpmText = analysisPanel.getBpmOnset();
            String smoothnessText = analysisPanel.getSmoothnessOnset();
            int base = analysisPanel.getBaseOnset();
            int smallest = analysisPanel.getSmallestOnset();
            int bpm = convertToNumber(bpmText);
            int smoothness = convertToNumber(smoothnessText);
            if (checkNumbersOnset(bpm, smoothness)) {
                t = new Thread(
                    new OnsetDetRunnable(bpm, smoothness, base, smallest));
                waitForThread(t);
            }
        };

        keyDetDoneListener = ae -> {
            String bpmText = analysisPanel.getBpmKey();
            String smoothnessText = analysisPanel.getSmoothnessKey();
            int base = analysisPanel.getBaseKey();
            int smallest = analysisPanel.getSmallestKey();
            int bpm = convertToNumber(bpmText);
            int smoothness = convertToNumber(smoothnessText);
            if (checkNumbersOnset(bpm, smoothness)) {
                t = new Thread(
                    new KeyDetRunnable(bpm, smoothness, base, smallest));
                waitForThread(t);
            }
        };
    }

    /**
     * Option dialog with MainWindow as parent.
     *
     * @return Option dialog
     */
    private int showSaveDialog() {
        return JOptionPane.showOptionDialog(this,
            SAVE_QUESTION, null, JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, OPTIONS,
            OPTIONS[0]);
    }

    /**
     * Window adapter for defining actions to execute after exit.
     */
    private final class ClosingWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosed(WindowEvent we) {
            glassPane.setVisible(true);
            try {
                matlabHandler.close();
            } catch (MatlabEngineException mee) {
                showDialog(mee.getMessage());
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
                try {
                    double freq = currentSongModel.getFreq();
                    matlabHandler.changePitch(freq);
                    changePitchPanel.setFreq(freq);
                } catch (MatlabEngineException mee) {
                    new Thread(new DialogRunnable(mee.getMessage())).start();
                }
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
                    .plotSong(PLOT_IMG_PATH);
                matlabHandler.passData(sm);
                SwingUtilities.invokeLater(() -> {
                    double freq = sm.getFreq();
                    double totalSamples = sm.getTotalSamples();
                    try {
                        BufferedImage plot = ImageIO.read(
                            new File(PLOT_IMG_PATH));
                        sm.setPlot(plot);
                        menuPanel.setCurrentSong(
                            totalSamples, freq, plot, sm.getCover(), isNormal,
                            getExtendedState() == MAXIMIZED_BOTH);
                        changePitchPanel.setSong(totalSamples, freq, plot);
                        currentSongModel = sm;
                        loadEnv();
                    } catch (IOException ioe) {
                        showDialog(IMAGE_ERROR);
                    }
                });
            } catch (MatlabEngineException mee) {
                new Thread(new DialogRunnable(mee.getMessage())).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void loadEnv() {
            if (framesToSeconds(sm.getTotalSamples(), sm.getFreq())
                > SongPropertiesLoader.getMaxSeconds()) {
                showInfo(infoLabel, LONG_SONG_INFO,
                    INFO_LABEL_DELAY);
            }
            currentSongTitle.setText(sm.getTitle());
            analysisPanel.resetFields();
            spectrogramPanel.removeImages();
            chromagramPanel.removeImages();
            analysisPanel.removeSong();
            optionPanel.showOptions(true);
            changePanel(MENU_PANEL);
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
                matlabHandler.showSpectrogram(windowSize, hopSize, nfft, window,
                    SPEC_IMG_PATH, SPEC_3D_IMG_PATH);
                SwingUtilities.invokeLater(() -> {
                    try {
                        Image spec = ImageIO.read(new File(SPEC_IMG_PATH));
                        Image spec3d = ImageIO.read(new File(SPEC_3D_IMG_PATH));
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
                        CHROM_IMG_PATH);
                SwingUtilities.invokeLater(() -> {
                    try {
                        Image chromagram =
                            ImageIO.read(new File(CHROM_IMG_PATH));
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
            } catch (MatlabEngineException mee) {
                new Thread(new DialogRunnable(mee.getMessage())).start();
                cutSongPanel.setDefaultValues();
            }
        }
    }

    /**
     * Runnable for changing pitch.
     */
    private final class CutSongDoneRunnable implements Runnable {
        private final String path;

        private CutSongDoneRunnable(String path) {
            this.path = path;
        }

        @Override
        public synchronized void run() {
            try {
                matlabHandler.saveSongCut(path);
            } catch (MatlabEngineException mee) {
                showDialog(mee.getMessage());
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
                    changePitchDialog
                        .setMinimumSize(changePitchDialog.getSize());
                });
            } catch (MatlabEngineException mee) {
                showDialog(mee.getMessage());
            }
        }
    }

    /**
     * Runnable for changing pitch.
     */
    private final class ChangePitchRunnable implements Runnable {
        private final String path;

        private ChangePitchRunnable(String path) {
            this.path = path;
        }

        @Override
        public synchronized void run() {
            try {
                matlabHandler.saveSongChangePitch(path);
            } catch (MatlabEngineException mee) {
                showDialog(mee.getMessage());
            }
        }
    }

    /**
     * Runnable for beat estimation.
     */
    private final class BeatEstRunnable implements Runnable {
        private final int minBpm;
        private final int maxBpm;

        private BeatEstRunnable(int minBpm, int maxBpm) {
            this.minBpm = minBpm;
            this.maxBpm = maxBpm;
        }

        @Override
        public synchronized void run() {
            try {
                int est = matlabHandler.estimateBeat(minBpm, maxBpm);
                SwingUtilities.invokeLater(() ->
                    analysisPanel.showEstimation(est));
            } catch (MatlabEngineException mee) {
                showDialog(mee.getMessage());
            }
        }
    }

    /**
     * Runnable for onset detection.
     */
    private final class OnsetDetRunnable implements Runnable {
        private final int bpm;
        private final int smoothness;
        private final int base;
        private final int smallest;

        private OnsetDetRunnable(int bpm, int smoothness, int base,
            int smallest) {
            this.bpm = bpm;
            this.smoothness = smoothness;
            this.base = base;
            this.smallest = smallest;
        }

        @Override
        public synchronized void run() {
            try {
                matlabHandler.plotSongOnset(bpm, smoothness, base,
                    smallest, ONSET_IMG_PATH);
                SwingUtilities.invokeLater(() -> {
                    try {
                        BufferedImage plot = ImageIO.read(
                            new File(ONSET_IMG_PATH));
                        analysisPanel.setOnsetImage(
                            currentSongModel.getTotalSamples(),
                            currentSongModel.getFreq(), plot);
                        lowerBar.setVisible(true);
                    } catch (IOException ioe) {
                        showDialog(IMAGE_ERROR);
                    }
                });
            } catch (MatlabEngineException mee) {
                showDialog(mee.getMessage());
            }
        }
    }

    /**
     * Runnable for key detection.
     */
    private final class KeyDetRunnable implements Runnable {
        private final int bpm;
        private final int smoothness;
        private final int base;
        private final int smallest;

        private KeyDetRunnable(int bpm, int smoothness, int base,
            int smallest) {
            this.bpm = bpm;
            this.smoothness = smoothness;
            this.base = base;
            this.smallest = smallest;
        }

        @Override
        public synchronized void run() {
            try {
                String est = matlabHandler.keyDet(bpm, smoothness, base,
                    smallest);
                SwingUtilities
                    .invokeLater(() -> analysisPanel.showEstimation(est));
            } catch (MatlabEngineException mee) {
                showDialog(mee.getMessage());
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
            showDialog(message);
        }
    }
}
