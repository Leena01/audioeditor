package util;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import static util.Utils.showDialog;

public class MatlabHandler {
    private MatlabEngine eng;
    private double totalSamples;
    private double freq;

    private final static String FILE_FORMAT_ERROR = "Wrong file format.";
    private final static String NO_SONG_ERROR = "No media found/selected.";

    public MatlabHandler(MatlabEngine eng) {
        this.eng = eng;
        this.totalSamples = 0.0;
        this.freq = 0.0;
    }

    public double getTotalSamples() {
        return totalSamples;
    }

    public double getFreq() {
        return freq;
    }

    public void close() {
        if (eng != null){
            try {
                eng.close();
            } catch(EngineException e) {
                showDialog("Error closing Matlab Engine.");
            } catch (Exception e) {
                showDialog(e.getMessage());
            }
        }
    }

    public synchronized void openSong(String p) {
        try {
            eng.putVariable("path", p.toCharArray());
            eng.eval("[x, fs] = audioread(path); player = audioplayer(x, fs);");
            eng.eval("total = get(player, 'TotalSamples');");
            this.totalSamples = eng.getVariable("total");
            System.out.println("AAAA" + totalSamples);
            this.freq = eng.getVariable("fs");
            // TODO: too long
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void pauseSong() {
        try {
            eng.eval("pause(player)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void resumeSong() {
        try {
            eng.eval("resume(player)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void stopSong() {
        try {
            eng.eval("stop(player)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void relocateSong(int frame, boolean isPlaying) {
        try {
            eng.putVariable("start", frame);
            if (frame == 0)
                eng.eval("stop(player); play(player);");
            else
                eng.eval("stop(player); play(player, start);");

            if (!isPlaying)
                eng.eval("pause(player)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void changeVolume(float level, boolean isPlaying) {
        try {
            eng.putVariable("level", level);
            eng.eval("current = get(player, 'CurrentSample'); y = x * level; " +
                    "player = audioplayer(y, fs); play(player, current);");
            if (!isPlaying)
                eng.eval("pause(player)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
