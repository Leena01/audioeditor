package logic.matlab;

import java.io.File;

final class MatlabCommands {
    static final String FOLDER = new File("").getAbsolutePath() + "\\resources\\matlab";
    /**
     * Matlab language injection
     */
    static final String PATH_VAR = "path";
    static final String FOLDER_PATH_VAR = "folderpath";
    static final String TOTAL_VAR = "total";
    static final String FREQ_VAR = "fs";
    static final String PLOT_IMG_VAR = "imgname";
    static final String SPEC_IMG_VAR = "imgname2";
    static final String START_VAR = "start";
    static final String LEVEL_VAR = "level";
    static final String EMPTY_VAR = "empty";
    private static final String SAMPLE_VAR = "x";
    private static final String PLAYER_VAR = "player";
    static final String WINDOW_SIZE_VAR = "wlen";
    static final String HOP_SIZE_VAR = "hop";
    static final String NFFT_VAR = "nfft";

    static final String ADD_PATH = String.format("addpath(genpath(%s));", FOLDER_PATH_VAR);
    static final String OPEN_SONG = String.format("[%s, %s, %s, %s] = openSong(%s);", PLAYER_VAR, SAMPLE_VAR, TOTAL_VAR, FREQ_VAR, PATH_VAR);
    static final String PLOT_SONG = String.format("plotSong(%s, %s, %s);", SAMPLE_VAR, FREQ_VAR, PLOT_IMG_VAR);
    static final String PAUSE_SONG = String.format("pauseSong(%s);", PLAYER_VAR);
    static final String RESUME_SONG = String.format("resumeSong(%s);", PLAYER_VAR);
    static final String STOP_SONG = String.format("stopSong(%s);", PLAYER_VAR);
    static final String RELOCATE_SONG = String.format("relocateSong(%s, %s, %s);", PLAYER_VAR, START_VAR, EMPTY_VAR);
    static final String CHANGE_VOLUME = String.format("changeVolume(%s, %s, %s);", PLAYER_VAR, SAMPLE_VAR, LEVEL_VAR);
    static final String ANALYZE_SONG = String.format("length(x);analyzeSong(%s, %s, %s, %s, %s, %s);",
            SAMPLE_VAR, WINDOW_SIZE_VAR, HOP_SIZE_VAR, NFFT_VAR, FREQ_VAR, SPEC_IMG_VAR);
}
