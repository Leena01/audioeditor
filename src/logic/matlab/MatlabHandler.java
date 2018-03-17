package logic.matlab;

import static logic.matlab.MatlabCommands.*;
import static util.Utils.showDialog;
import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import logic.dbaccess.SongModel;
import logic.exceptions.MatlabEngineException;

public class MatlabHandler {
    private MatlabEngine eng;
    private double totalSamples;
    private double freq;
    private static MatlabHandler instance = null;

    private final static String FILE_FORMAT_ERROR = "Wrong file format.";
    private final static String NO_SONG_ERROR = "No media found/selected.";
    private final static String CLOSE_ERROR = "Error closing Matlab Engine.";

    public static MatlabHandler getInstance(MatlabEngine eng) {
        if(instance == null)
            instance = new MatlabHandler(eng);
        return instance;
    }

    private MatlabHandler(MatlabEngine eng) {
        this.eng = eng;
        this.totalSamples = 0.0;
        this.freq = 0.0;
        try {
            eng.putVariable(FOLDER_PATH_VAR, FOLDER.toCharArray());
            eng.eval(ADD_PATH);
        } catch (Exception e) {
            showDialog(e.getMessage());
        }

    }

    public void passData(SongModel sm) {
        sm.setTotalSamples(totalSamples);
        sm.setFreq(freq);
    }

    public void close() {
        if (eng != null){
            try {
                eng.close();
                instance = null;
            } catch(EngineException e) {
                showDialog(CLOSE_ERROR);
            } catch (Exception e) {
                showDialog(e.getMessage());
            }
        }
    }

    public synchronized void openSong(String p) throws MatlabEngineException {
        try {
            eng.putVariable(PATH_VAR, p.toCharArray());
            eng.eval(OPEN_SONG);
            this.totalSamples = eng.getVariable(TOTAL_VAR);
            this.freq = eng.getVariable(FREQ_VAR);
        } catch (Exception ex) {
            throw new MatlabEngineException("The file type is not supported.");
        }
    }

    public synchronized void plotSong(String imageName) {
        try {
            eng.putVariable(PLOT_IMG_VAR, imageName.toCharArray());
            eng.eval(PLOT_SONG);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void pauseSong() {
        try {
            eng.eval(PAUSE_SONG);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void resumeSong() {
        try {
            eng.eval(RESUME_SONG);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void stopSong() {
        try {
            eng.eval(STOP_SONG);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void relocateSong(int frame, boolean isPlaying) {
        try {
            eng.putVariable(START_VAR, frame);
            if (frame == 0 || frame == totalSamples)
                eng.putVariable(EMPTY_VAR, 1);
            else
                eng.putVariable(EMPTY_VAR, 0);
            eng.eval(RELOCATE_SONG);

            if (!isPlaying)
                eng.eval(PAUSE_SONG);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void changeVolume(float level, boolean isPlaying) {
        try {
            eng.putVariable(LEVEL_VAR, level);
            eng.eval(CHANGE_VOLUME);
            if (!isPlaying)
                eng.eval(PAUSE_SONG);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void showSpectrogram(double windowSize, double hopSize, double nfft, String imageName,
         String imageName2) {
        try {
            eng.putVariable(WINDOW_SIZE_VAR, windowSize);
            eng.putVariable(HOP_SIZE_VAR, hopSize);
            eng.putVariable(NFFT_VAR, nfft);
            eng.putVariable(SPEC_IMG_VAR, imageName.toCharArray());
            eng.putVariable(SPEC_3D_IMG_VAR, imageName2.toCharArray());
            eng.eval(SHOW_SPECTROGRAM);
            eng.eval(SHOW_SPECTROGRAM_3D);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
