package logic.matlab;

import static logic.matlab.MatlabCommands.*;
import static util.Utils.showDialog;
import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import logic.dbaccess.SongModel;

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

    public synchronized void openSong(String p) {
        try {
            eng.putVariable(PATH_VAR, p.toCharArray());
            eng.eval(OPEN_SONG);
            this.totalSamples = eng.getVariable(TOTAL_VAR);
            this.freq = eng.getVariable(FREQ_VAR);
            // TODO: too long
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void plotSong(String imageName) {
        try {
            eng.putVariable(IMG_VAR, imageName.toCharArray());
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
                eng.eval(RELOCATE_SONG_EMPTY);
            else
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
}
