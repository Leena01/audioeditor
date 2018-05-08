package org.ql.audioeditor.logic.matlab;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.matlab.types.CellStr;
import org.ql.audioeditor.common.properties.ConfigPropertiesLoader;
import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.logic.dbaccess.SongModel;
import org.ql.audioeditor.logic.exceptions.MatlabEngineException;

import java.io.File;

import static org.ql.audioeditor.common.util.GeneralUtils.getPath;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.ADD_PATH;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.CHANGE_PITCH;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.CHANGE_VOLUME;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.CREATE_WINDOW_MAP;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.CUT_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.DETECT_ONSET;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.ESTIMATE_BEAT;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.GET_CURRENT_FRAME;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.IS_PLAYING;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.OPEN_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.PAUSE_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.PLOT_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.RELOCATE_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.RESUME_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabCommands
    .SAVE_SONG_CHANGE_PITCH;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.SAVE_SONG_CUT;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.SHOW_CHROMAGRAM;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.SHOW_SPECTROGRAM;
import static org.ql.audioeditor.logic.matlab.MatlabCommands
    .SHOW_SPECTROGRAM_3D;
import static org.ql.audioeditor.logic.matlab.MatlabCommands.STOP_SONG;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.BASE_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.BEAT_EST_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.BPM_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.CHROM_IMG_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.CURRENT_FRAME_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.EMPTY_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.FILE_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.FOLDER_PATH_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.FREQ_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.FREQ_VAR_2;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.FROM_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.HOP_SIZE_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.IS_PLAYING_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.LEVEL_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.MAX_BPM_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.MIN_BPM_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.NFFT_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.ONSET_DET_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.PLOT_IMG_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.SMALLEST_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.SPEC_3D_IMG_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.SPEC_IMG_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.START_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.S_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.TOTAL_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.TO_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.WINDOW_KEYS_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.WINDOW_SIZE_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.WINDOW_VAR;

/**
 * Singleton class for establishing connection with the MATLAB Engine.
 */
public final class MatlabHandler {
    private static final int MIN_FRAMES = 1;
    private static final String OPEN_ERROR =
        "Error: file cannot be opened.";
    private static final String IMAGE_ERROR = "Cannot generate image.";
    private static final String CANNOT_CUT_ERROR = "Cannot cut song.";
    private static final String CANNOT_CHANGE_PITCH_ERROR =
        "Cannot change pitch.";
    private static final String CANNOT_ESTIMATE_BEAT =
        "Beat estimation has failed.";
    private static final String CLOSE_ERROR = "Error closing Matlab Engine.";
    private static final String FOLDER = getPath() + File.separator
        + ConfigPropertiesLoader.getMatlabFolder();
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
     * Returns the smallest frame index.
     *
     * @return Smallest frame index
     */
    public static int getMinFrames() {
        return MIN_FRAMES;
    }

    /**
     * Initialization (loading classpath and creating a map for window types).
     *
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public void init() throws MatlabEngineException {
        try {
            eng.putVariable(FOLDER_PATH_VAR.toString(),
                FOLDER.toCharArray());
            eng.eval(ADD_PATH);
            eng.putVariable(WINDOW_KEYS_VAR.toString(),
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
            eng.putVariable(FILE_VAR.toString(), file.toCharArray());
            eng.eval(OPEN_SONG);
            this.totalSamples = eng.getVariable(TOTAL_VAR.toString());
            this.freq = eng.getVariable(FREQ_VAR.toString());
        } catch (Exception e) {
            throw new MatlabEngineException(OPEN_ERROR);
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
            eng.putVariable(PLOT_IMG_VAR.toString(), imageName.toCharArray());
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
            return eng.getVariable(CURRENT_FRAME_VAR.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MIN_FRAMES;
    }

    /**
     * Checks whether the current song is playing.
     *
     * @return Logical value
     */
    public synchronized boolean isPlaying() {
        try {
            eng.eval(IS_PLAYING);
            return eng.getVariable(IS_PLAYING_VAR.toString());
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
            eng.putVariable(START_VAR.toString(), frame);
            if (frame < MIN_FRAMES || frame >= totalSamples) {
                eng.putVariable(EMPTY_VAR.toString(), 1);
            } else {
                eng.putVariable(EMPTY_VAR.toString(), 0);
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
            eng.putVariable(LEVEL_VAR.toString(), level);
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
            eng.putVariable(WINDOW_SIZE_VAR.toString(), windowSize);
            eng.putVariable(HOP_SIZE_VAR.toString(), hopSize);
            eng.putVariable(NFFT_VAR.toString(), nfft);
            eng.putVariable(WINDOW_VAR.toString(), window.toCharArray());
            eng.putVariable(SPEC_IMG_VAR.toString(), imageName.toCharArray());
            eng.putVariable(SPEC_3D_IMG_VAR.toString(),
                imageName2.toCharArray());
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
            eng.putVariable(WINDOW_SIZE_VAR.toString(), windowSize);
            eng.putVariable(HOP_SIZE_VAR.toString(), hopSize);
            eng.putVariable(NFFT_VAR.toString(), nfft);
            eng.putVariable(WINDOW_VAR.toString(), window.toCharArray());
            eng.putVariable(CHROM_IMG_VAR.toString(), imageName.toCharArray());
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
            eng.putVariable(FROM_VAR.toString(), from);
            eng.putVariable(TO_VAR.toString(), to);
            eng.eval(CUT_SONG);
        } catch (Exception e) {
            throw new MatlabEngineException(CANNOT_CUT_ERROR);
        }
    }

    /**
     * Change pitch.
     *
     * @param freq New frequency
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public synchronized void changePitch(double freq) throws
        MatlabEngineException {
        try {
            eng.putVariable(FREQ_VAR_2.toString(), freq);
            eng.eval(CHANGE_PITCH);
        } catch (Exception e) {
            throw new MatlabEngineException(CANNOT_CHANGE_PITCH_ERROR);
        }
    }

    /**
     * Estimate beat.
     *
     * @param minBpm Minimum BPM
     * @param maxBpm Maximum BPM
     * @return Estimated BPM
     *
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public synchronized int estimateBeat(int minBpm, int maxBpm) throws
        MatlabEngineException {
        try {
            eng.putVariable(MIN_BPM_VAR.toString(), minBpm);
            eng.putVariable(MAX_BPM_VAR.toString(), maxBpm);
            eng.eval(ESTIMATE_BEAT);
            return eng.getVariable(BEAT_EST_VAR.toString());
        } catch (Exception e) {
            throw new MatlabEngineException(CANNOT_ESTIMATE_BEAT);
        }
    }

    /**
     * Show a song's oscillogram with the detected onsets.
     *
     * @param bpm BPM
     * @param smoothness Filter size
     * @param base Lower value of the time signature (value of beat)
     * @param smallest Smallest note value that might appear in the song
     * @param imageName  Name of the generated image
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public synchronized void plotSongOnset(int bpm, int smoothness, int base,
        int smallest, String imageName)
        throws MatlabEngineException {
        try {
            eng.putVariable(BPM_VAR.toString(), bpm);
            eng.putVariable(S_VAR.toString(), smoothness);
            eng.putVariable(BASE_VAR.toString(), base);
            eng.putVariable(SMALLEST_VAR.toString(), smallest);
            eng.putVariable(ONSET_DET_VAR.toString(), imageName.toCharArray());
            eng.eval(DETECT_ONSET);
        } catch (Exception e) {
            throw new MatlabEngineException(IMAGE_ERROR);
        }
    }

    /**
     * Save song after cut.
     *
     * @param file Path to save the song to
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public synchronized void saveSongCut(String file)
        throws MatlabEngineException {
        saveSong(file, SAVE_SONG_CUT);
    }

    /**
     * Save song after the pitch was changed.
     *
     * @param file Path to save the song to
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    public synchronized void saveSongChangePitch(String file)
        throws MatlabEngineException {
        saveSong(file, SAVE_SONG_CHANGE_PITCH);
    }

    /**
     * Save song.
     *
     * @param file Path to save the song to
     * @param type Type of save
     * @throws MatlabEngineException Exception caused by the MATLAB Engine
     */
    private synchronized void saveSong(String file, String type)
        throws MatlabEngineException {
        try {
            eng.putVariable(FILE_VAR.toString(), file.toCharArray());
            eng.eval(type);
        } catch (Exception e) {
            throw new MatlabEngineException(OPEN_ERROR);
        }
    }
}
