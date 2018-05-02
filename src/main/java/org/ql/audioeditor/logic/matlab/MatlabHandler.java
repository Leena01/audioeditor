package org.ql.audioeditor.logic.matlab;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.matlab.types.CellStr;
import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.logic.dbaccess.SongModel;
import org.ql.audioeditor.logic.exceptions.MatlabEngineException;

import static org.ql.audioeditor.logic.matlab.MatlabCommands.ADD_PATH;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.CHANGE_PITCH;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.CHANGE_VOLUME;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.CHROM_IMG_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.CREATE_WINDOW_MAP;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.CURRENT_FRAME_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.CUT_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.EMPTY_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.FILE_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.FOLDER;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.FOLDER_PATH_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.FREQ_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.FREQ_VAR_2;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.FROM_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.GET_CURRENT_FRAME;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.HOP_SIZE_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.IS_PLAYING;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.IS_PLAYING_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.LEVEL_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.NFFT_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.OPEN_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.PAUSE_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.PLOT_IMG_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.PLOT_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.RELOCATE_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.RESUME_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.SAVE_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.SHOW_CHROMAGRAM;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.SHOW_SPECTROGRAM;
import static org.ql.audioeditor.logic.matlab.MatlabCommands
    .SHOW_SPECTROGRAM_3D;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.SPEC_3D_IMG_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.SPEC_IMG_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.START_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.STOP_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.TOTAL_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.TO_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.WINDOW_KEYS_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.WINDOW_SIZE_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.WINDOW_VAR;

/**
 * Singleton class for establishing connection with the MATLAB Engine.
 */
public final class MatlabHandler {
    private static final String FILE_FORMAT_ERROR =
        "This file type is not supported.";
    private static final String IMAGE_ERROR = "Cannot generate image.";
    private static final String CANNOT_CUT_ERROR = "Cannot cut song.";
    private static final String CLOSE_ERROR = "Error closing Matlab Engine.";
    private static MatlabHandler instance = null;
    private final MatlabEngine eng;
    private double totalSamples;
    private double freq;

    /**
     * Constructor.
     *
     * @param eng MATLAB Engine
     */
    private MatlabHandler(MatlabEngine eng) {
        this.eng = eng;
        this.totalSamples = 0.0;
        this.freq = 0.0;
    }

    /**
     * Get instance.
     *
     * @param eng MATLAB Engine
     * @return MatlabHandler
     */
    public static MatlabHandler getInstance(MatlabEngine eng) {
        if (instance == null) {
            instance = new MatlabHandler(eng);
        }
        return instance;
    }

    /**
     * Initialization (loading classpath and creating a map for window types).
     *
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public void init() throws MatlabEngineException {
        try {
            eng.putVariable(FOLDER_PATH_VAR, FOLDER.toCharArray());
            eng.eval(ADD_PATH);
            eng.putVariable(WINDOW_KEYS_VAR,
                new CellStr(SongPropertiesLoader.getWindowNames()));
            eng.eval(CREATE_WINDOW_MAP);
        } catch (Exception e) {
            throw new MatlabEngineException(e.getMessage());
        }
    }

    /**
     * Pass data to a song model.
     *
     * @param sm Song model
     */
    public void passData(SongModel sm) {
        sm.setTotalSamples(totalSamples);
        sm.setFreq(freq);
    }

    /**
     * Close connection.
     *
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public void close() throws MatlabEngineException {
        if (eng != null) {
            try {
                eng.close();
                instance = null;
            } catch (EngineException e) {
                throw new MatlabEngineException(CLOSE_ERROR);
            }
        }
    }

    /**
     * Open a song.
     *
     * @param file File path
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public synchronized void openSong(String file)
        throws MatlabEngineException {
        try {
            eng.putVariable(FILE_VAR, file.toCharArray());
            eng.eval(OPEN_SONG);
            this.totalSamples = eng.getVariable(TOTAL_VAR);
            this.freq = eng.getVariable(FREQ_VAR);
        } catch (Exception e) {
            throw new MatlabEngineException(FILE_FORMAT_ERROR);
        }
    }

    /**
     * Plot a song.
     *
     * @param imageName Name of the generated image
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public synchronized void plotSong(String imageName)
        throws MatlabEngineException {
        try {
            eng.putVariable(PLOT_IMG_VAR, imageName.toCharArray());
            eng.eval(PLOT_SONG);
        } catch (Exception e) {
            throw new MatlabEngineException(IMAGE_ERROR);
        }
    }

    /**
     * Pause song.
     */
    public synchronized void pauseSong() {
        try {
            eng.eval(PAUSE_SONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resume song.
     */
    public synchronized void resumeSong() {
        try {
            eng.eval(RESUME_SONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop song.
     */
    public synchronized void stopSong() {
        try {
            eng.eval(STOP_SONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get current frame.
     *
     * @return Current frame
     */
    public synchronized double getCurrentFrame() {
        try {
            eng.eval(GET_CURRENT_FRAME);
            return eng.getVariable(CURRENT_FRAME_VAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * Checks whether the current song is playing.
     *
     * @return Logical value
     */
    public synchronized boolean isPlaying() {
        try {
            eng.eval(IS_PLAYING);
            return eng.getVariable(IS_PLAYING_VAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Relocate song.
     *
     * @param frame Frame to relocate the song to
     */
    public synchronized void relocateSong(int frame) {
        try {
            eng.putVariable(START_VAR, frame);
            if (frame <= 0 || frame >= totalSamples) {
                eng.putVariable(EMPTY_VAR, 1);
            }
            else {
                eng.putVariable(EMPTY_VAR, 0);
            }
            eng.eval(RELOCATE_SONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Change volume.
     *
     * @param level Level to change the volume to. Please see the according
     *              MATLAB file's documentation for the available levels.
     */
    public synchronized void changeVolume(float level) {
        try {
            eng.putVariable(LEVEL_VAR, level);
            eng.eval(CHANGE_VOLUME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show spectrogram.
     *
     * @param windowSize Window size
     * @param hopSize    Hop size
     * @param nfft       Number of FFT points.
     * @param window     Window type
     * @param imageName  Name of the generated image (2D)
     * @param imageName2 Name of the generated image (3D)
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public synchronized void showSpectrogram(double windowSize, double hopSize,
        double nfft, String window, String imageName, String imageName2)
        throws MatlabEngineException {
        try {
            eng.putVariable(WINDOW_SIZE_VAR, windowSize);
            eng.putVariable(HOP_SIZE_VAR, hopSize);
            eng.putVariable(NFFT_VAR, nfft);
            eng.putVariable(WINDOW_VAR, window.toCharArray());
            eng.putVariable(SPEC_IMG_VAR, imageName.toCharArray());
            eng.putVariable(SPEC_3D_IMG_VAR, imageName2.toCharArray());
            eng.eval(SHOW_SPECTROGRAM);
            eng.eval(SHOW_SPECTROGRAM_3D);
        } catch (Exception e) {
            throw new MatlabEngineException(IMAGE_ERROR);
        }
    }

    /**
     * Show chromagram.
     *
     * @param windowSize Window size
     * @param hopSize    Hop size
     * @param nfft       Number of FFT points.
     * @param window     Window type
     * @param imageName  Name of the generated image (2D)
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public synchronized void showChromagram(double windowSize, double hopSize,
        double nfft, String window, String imageName)
        throws MatlabEngineException {
        try {
            eng.putVariable(WINDOW_SIZE_VAR, windowSize);
            eng.putVariable(HOP_SIZE_VAR, hopSize);
            eng.putVariable(NFFT_VAR, nfft);
            eng.putVariable(WINDOW_VAR, window.toCharArray());
            eng.putVariable(CHROM_IMG_VAR, imageName.toCharArray());
            eng.eval(SHOW_CHROMAGRAM);
        } catch (Exception e) {
            throw new MatlabEngineException(IMAGE_ERROR);
        }
    }

    /**
     * Cut song.
     *
     * @param from Start frame
     * @param to   End frame
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public synchronized void cutSong(int from, int to)
        throws MatlabEngineException {
        try {
            eng.putVariable(FROM_VAR, from);
            eng.putVariable(TO_VAR, to);
            eng.eval(CUT_SONG);
        } catch (Exception e) {
            throw new MatlabEngineException(CANNOT_CUT_ERROR);
        }
    }

    /**
     * Change pitch.
     *
     * @param freq New frequency
     */
    public synchronized void changePitch(double freq) {
        try {
            eng.putVariable(FREQ_VAR_2, freq);
            eng.eval(CHANGE_PITCH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save song.
     *
     * @param file Path to save the song to
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public synchronized void saveSong(String file)
        throws MatlabEngineException {
        try {
            eng.putVariable(FILE_VAR, file.toCharArray());
            eng.eval(SAVE_SONG);
        } catch (Exception e) {
            throw new MatlabEngineException(FILE_FORMAT_ERROR);
        }
    }
}
