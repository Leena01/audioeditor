package org.ql.audioeditor.logic.matlab;

import java.io.File;

final class MatlabCommands {
    static final String FOLDER = new File("").getAbsolutePath() + "\\src\\main\\matlab";
    /**
     * Matlab language injection
     */
    static final String FILE_VAR = "file";
    static final String FOLDER_PATH_VAR = "folderpath";
    private static final String WINDOW_MAP_VAR = "windowmap";
    static final String WINDOW_KEYS_VAR = "windowkeys";
    static final String TOTAL_VAR = "total";
    static final String FREQ_VAR = "fs";
    static final String FREQ_VAR_2 = "fs2";
    static final String PLOT_IMG_VAR = "imgname";
    static final String SPEC_IMG_VAR = "imgname2";
    static final String SPEC_3D_IMG_VAR = "imgname3";
    static final String START_VAR = "start";
    static final String CURRENT_FRAME_VAR = "current";
    static final String LEVEL_VAR = "level";
    static final String EMPTY_VAR = "empty";
    static final String IS_PLAYING_VAR = "isplaying";
    private static final String SAMPLE_VAR = "x";
    private static final String SAMPLE_VAR_2 = "y";
    private static final String PLAYER_VAR = "player";
    static final String WINDOW_SIZE_VAR = "wlen";
    static final String HOP_SIZE_VAR = "hop";
    static final String NFFT_VAR = "nfft";
    static final String WINDOW_VAR = "window";
    static final String FROM_VAR = "from";
    static final String TO_VAR = "to";

    static final String ADD_PATH = String.format("addpath(genpath(%s));", FOLDER_PATH_VAR);
    static final String CREATE_WINDOW_MAP = String.format("%s = createWindowMap(%s);", WINDOW_MAP_VAR, WINDOW_KEYS_VAR);
    static final String OPEN_SONG = String.format("[%s, %s, %s, %s] = openSong(%s);",
            PLAYER_VAR, SAMPLE_VAR, TOTAL_VAR, FREQ_VAR, FILE_VAR);
    static final String PLOT_SONG = String.format("plotSong(%s, %s, %s);", SAMPLE_VAR, FREQ_VAR, PLOT_IMG_VAR);
    static final String PAUSE_SONG = String.format("pauseSong(%s);", PLAYER_VAR);
    static final String RESUME_SONG = String.format("resumeSong(%s);", PLAYER_VAR);
    static final String STOP_SONG = String.format("stopSong(%s);", PLAYER_VAR);
    static final String GET_CURRENT_FRAME = String.format("%s = getCurrentFrame(%s);", CURRENT_FRAME_VAR, PLAYER_VAR);
    static final String IS_PLAYING = String.format("%s = isPlaying(%s);", IS_PLAYING_VAR, PLAYER_VAR);
    static final String RELOCATE_SONG = String.format("relocateSong(%s, %s, %s);",
            PLAYER_VAR, START_VAR, EMPTY_VAR);
    static final String CHANGE_VOLUME = String.format("%s = changeVolume(%s, %s, %s, %s);",
            PLAYER_VAR, PLAYER_VAR, SAMPLE_VAR, FREQ_VAR, LEVEL_VAR);
    static final String SHOW_SPECTROGRAM = String.format("length(x); showSpectrogram(%s, %s, %s, %s, %s, %s, %s, %s);",
        SAMPLE_VAR, WINDOW_SIZE_VAR, HOP_SIZE_VAR, NFFT_VAR, WINDOW_VAR, FREQ_VAR, SPEC_IMG_VAR, WINDOW_MAP_VAR);
    static final String SHOW_SPECTROGRAM_3D = String.format("length(x); showSpectrogram3d(%s, %s, %s, %s, %s, %s, %s, %s);",
        SAMPLE_VAR, WINDOW_SIZE_VAR, HOP_SIZE_VAR, NFFT_VAR, WINDOW_VAR, FREQ_VAR, SPEC_3D_IMG_VAR, WINDOW_MAP_VAR);
    static final String CUT_SONG = String.format("%s = cutSong(%s, %s, %s);",
            SAMPLE_VAR_2, SAMPLE_VAR, FROM_VAR, TO_VAR);
    static final String SAVE_SONG = String.format("saveSong(%s, %s, %s);", FILE_VAR, SAMPLE_VAR_2, FREQ_VAR);
    static final String CHANGE_PITCH = String.format("%s = changePitch(%s);", PLAYER_VAR, FREQ_VAR_2);
}
