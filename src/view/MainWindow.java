package view;

import model.exceptions.InvalidOperationException;
import model.exceptions.SQLConnectionException;
import model.MatlabHandler;
import view.decorated.SideBar;
import view.decorated.Window;
import view.decorated.Label;
import view.tables.ViewSongsPanel;
import database.entities.Song;
import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static util.Utils.fillColor;
import static util.Utils.showDialog;

public class MainWindow extends Window {
    /**
     * Constants
     */
    private static final String SUCCESSFUL_OPERATION = "Successful operation.";
    private static final String MENU_PANEL = "Menu Panel";
    private static final String VIEW_SONGS_PANEL = "View Songs Panel";

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

    public MainWindow(Model model, MatlabHandler matlabHandler) {
        super();
        this.songTableModel = new SongTableModel();
        initializeListeners(model, matlabHandler);

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
        currentSongName = new Label();
        currentSongName.setPreferredSize(fieldDimension);
        currentSongName.setOpaque(false);
        bottomPanel.add(currentSongName);
        currentSongArtist = new Label();
        currentSongArtist.setPreferredSize(fieldDimension);
        currentSongArtist.setOpaque(false);
        bottomPanel.add(currentSongArtist);
        lastOpenPath = null;


        mainPanel.add(menuPanel, MENU_PANEL);
        mainPanel.add(viewSongsPanel, VIEW_SONGS_PANEL);
        add(sideBar, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setOpaque(false);
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
        pack();
        setMinimumSize(getSize());
        setVisible(true);
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

    private void initializeListeners(Model model, MatlabHandler matlabHandler) {
        backToMenuListener = ae -> cardLayout.show(mainPanel, MENU_PANEL);

        openFileListener = ae -> {
            try {
                File f = openFile();
                if (f != null) {
                    lastOpenPath = f.getParent();
                    String p = f.getPath();
                    String imageName = "temp.png";
                    currentSong = new Song(f.getName(), "-", p);
                    currentSongArtist.setText(currentSong.getArtist());
                    currentSongName.setText(currentSong.getName());
                    // currentSongArtist.setText("Name: " + currentSong.getArtist()); // TODO
                    // currentSongName.setText("Artist: " + currentSong.getName());

                    new Thread(() -> {
                        matlabHandler.openSong(p);
                        matlabHandler.plotSong(imageName);
                        totalSamples = matlabHandler.getTotalSamples();
                        freq = matlabHandler.getFreq();
                        // System.out.println(totalSamples);
                        SwingUtilities.invokeLater(() -> {
                            try {
                                BufferedImage plot = ImageIO.read(new File(imageName));
                                menuPanel.setCurrentSong(totalSamples, freq, plot);
                                optionPanel.showOptions(true);
                                pack();
                                setMinimumSize(getSize());
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
                viewSongsPanel.setList(model.getSongs());
                cardLayout.show(mainPanel, VIEW_SONGS_PANEL);
            } catch (SQLConnectionException sqe) {
                showDialog(sqe.getMessage());
            }
        };

        favoriteButtonListener = ae -> {
            try {
                int newId = model.addSong(currentSong);
                currentSong.setId(newId);
            } catch(InvalidOperationException | SQLConnectionException ex) {
                showDialog(ex.getMessage());
            }
        };

        unfavoriteButtonListener = ae -> {
            try {
                model.deleteSong(currentSong);
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
            editPanel.setSelectedSong(viewSongsPanel.getSelected());
            editFrame.setVisible(true);
        };

        deleteSongListener = ae -> {
            try {
                model.deleteSong(viewSongsPanel.getSelected());
                showDialog(SUCCESSFUL_OPERATION);
            } catch(InvalidOperationException | SQLConnectionException ex) {
                showDialog(ex.getMessage());
            }
        };

        editDoneListener = ae -> {
            editFrame.setVisible(false);
            try {
                model.editSong(editPanel.getSelectedId(), editPanel.getNameField(),
                        editPanel.getArtistField(), editPanel.getSelectedPath());
                showDialog(SUCCESSFUL_OPERATION);
            } catch(InvalidOperationException | SQLConnectionException ex) {
                showDialog(ex.getMessage());
            }
            editPanel.clearFields();
        };
    }
}
