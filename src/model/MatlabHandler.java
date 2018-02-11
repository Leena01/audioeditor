package model;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import static util.Utils.showDialog;

public class MatlabHandler {
    private MatlabEngine eng;
    private double totalSamples;
    private double freq;
    private static MatlabHandler instance = null;

    private final static String FILE_FORMAT_ERROR = "Wrong file format.";
    private final static String NO_SONG_ERROR = "No media found/selected.";

    public static MatlabHandler getInstance(MatlabEngine eng) {
        if(instance == null) {
            instance = new MatlabHandler(eng);
        }
        return instance;
    }

    private MatlabHandler(MatlabEngine eng) {
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
                instance = null;
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
            eng.eval("[x, fs] = audioread(path); player = audioplayer(x, fs); " +
                    "total = get(player, 'TotalSamples');");
            this.totalSamples = eng.getVariable("total");
            // System.out.println("AAAA" + totalSamples);
            this.freq = eng.getVariable("fs");
            // TODO: too long
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void plotSong(String imageName) {
        try {
            eng.putVariable("imgname", imageName.toCharArray());
            eng.eval("figure('visible', 'off');" +
                    "x1 = x(:, 1); xlen = length(x); tlen = xlen/fs; tper = 1/fs; " +
                    "t = 0:tper:(xlen/fs) - tper;" +
                    "plot(t, x1); " +
                    "axis([t(1) t(end) -max(x1) max(x1)]);" +
                    "ax = gca; box off;" +
                    "set(gca, 'XTick', [], 'YTick', []); " +
                    "outerpos = ax.OuterPosition; " +
                    "ti = ax.TightInset; " +
                    "left = outerpos(1) + ti(1); " +
                    "bottom = outerpos(2) + ti(2); " +
                    "ax_width = outerpos(3) - ti(1) - ti(3); " +
                    "ax_height = outerpos(4) - ti(2) - ti(4); " +
                    "ax.Position = [left bottom ax_width ax_height];");
            eng.eval("hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');");
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
            if (frame == 0 || frame == totalSamples)
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
