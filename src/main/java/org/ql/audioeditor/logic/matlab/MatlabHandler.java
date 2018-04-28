package org.ql.audioeditor.logic.matlab;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.matlab.types.CellStr;
import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.logic.dbaccess.SongModel;
import org.ql.audioeditor.logic.exceptions.MatlabEngineException;

import static org.ql.audioeditor.logic.matlab.MatlabCommands.*;

public class MatlabHandler {
    private final static String FILE_FORMAT_ERROR =
        "This file type is not supported.";
    private final static String IMAGE_ERROR = "Cannot generate image.";
    private final static String CANNOT_CUT_ERROR = "Cannot cut song.";
    private final static String CLOSE_ERROR = "Error closing Matlab Engine.";
    private static MatlabHandler instance = null;
    private MatlabEngine eng;
    private double totalSamples;
    private double freq;

    private MatlabHandler(MatlabEngine eng) {
        this.eng = eng;
        this.totalSamples = 0.0;
        this.freq = 0.0;
    }

    public static MatlabHandler getInstance(MatlabEngine eng) {
        if (instance == null)
            instance = new MatlabHandler(eng);
        return instance;
    }

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

    public void passData(SongModel sm) {
        sm.setTotalSamples(totalSamples);
        sm.setFreq(freq);
    }

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

    public synchronized void plotSong(String imageName)
        throws MatlabEngineException {
        try {
            eng.putVariable(PLOT_IMG_VAR, imageName.toCharArray());
            eng.eval(PLOT_SONG);
        } catch (Exception e) {
            throw new MatlabEngineException(IMAGE_ERROR);
        }
    }

    public synchronized void pauseSong() {
        try {
            eng.eval(PAUSE_SONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void resumeSong() {
        try {
            eng.eval(RESUME_SONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopSong() {
        try {
            eng.eval(STOP_SONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized double getCurrentFrame() {
        try {
            eng.eval(GET_CURRENT_FRAME);
            return eng.getVariable(CURRENT_FRAME_VAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public synchronized boolean isPlaying() {
        try {
            eng.eval(IS_PLAYING);
            return eng.getVariable(IS_PLAYING_VAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized void relocateSong(int frame) {
        try {
            eng.putVariable(START_VAR, frame);
            if (frame <= 0 || frame >= totalSamples)
                eng.putVariable(EMPTY_VAR, 1);
            else
                eng.putVariable(EMPTY_VAR, 0);
            eng.eval(RELOCATE_SONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void changeVolume(float level) {
        try {
            eng.putVariable(LEVEL_VAR, level);
            eng.eval(CHANGE_VOLUME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void showSpectrogram(double windowSize, double hopSize,
        double nfft, String window,
        String imageName, String imageName2) throws MatlabEngineException {
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

    public synchronized void changePitch(double freq) {
        try {
            eng.putVariable(FREQ_VAR_2, freq);
            eng.eval(CHANGE_PITCH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
