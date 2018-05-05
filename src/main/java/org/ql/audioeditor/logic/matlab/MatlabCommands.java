package org.ql.audioeditor.logic.matlab;

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
import static org.ql.audioeditor.logic.matlab.MatlabVariables.NFFT_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.PLAYER_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.PLOT_IMG_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.SAMPLE_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.SAMPLE_VAR_2;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.SPEC_3D_IMG_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.SPEC_IMG_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.START_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.TOTAL_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.TO_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.WINDOW_KEYS_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.WINDOW_MAP_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.WINDOW_SIZE_VAR;
import static org.ql.audioeditor.logic.matlab.MatlabVariables.WINDOW_VAR;

/**
 * MATLAB commands for MATLAB language injection.
 */
final class MatlabCommands {

    /**
     * MATLAB functions.
     */
    static final String ADD_PATH =
        String.format("addpath(genpath(%s));",
            FOLDER_PATH_VAR);

    static final String CREATE_WINDOW_MAP =
        String.format("%s = createWindowMap(%s);",
            WINDOW_MAP_VAR, WINDOW_KEYS_VAR);

    static final String OPEN_SONG =
        String.format("[%s, %s, %s, %s] = openSong(%s);",
            PLAYER_VAR, SAMPLE_VAR, TOTAL_VAR, FREQ_VAR, FILE_VAR);

    static final String PAUSE_SONG =
        String.format("pauseSong(%s);",
            PLAYER_VAR);

    static final String RESUME_SONG =
        String.format("resumeSong(%s);",
            PLAYER_VAR);

    static final String STOP_SONG =
        String.format("stopSong(%s);",
            PLAYER_VAR);

    static final String GET_CURRENT_FRAME =
        String.format("%s = getCurrentFrame(%s);",
            CURRENT_FRAME_VAR, PLAYER_VAR);

    static final String IS_PLAYING =
        String.format("%s = isPlaying(%s);",
            IS_PLAYING_VAR, PLAYER_VAR);

    static final String PLOT_SONG =
        String.format("plotSong(%s, %s, %s);",
            SAMPLE_VAR, FREQ_VAR, PLOT_IMG_VAR);

    static final String SHOW_SPECTROGRAM =
        String.format("showSpectrogram(%s, %s, %s, %s, %s, %s, %s, %s);",
            SAMPLE_VAR, WINDOW_SIZE_VAR, HOP_SIZE_VAR, NFFT_VAR,
            WINDOW_VAR, FREQ_VAR, SPEC_IMG_VAR, WINDOW_MAP_VAR);

    static final String SHOW_SPECTROGRAM_3D =
        String.format("showSpectrogram3d(%s, %s, %s, %s, %s, %s, %s, %s);",
            SAMPLE_VAR, WINDOW_SIZE_VAR, HOP_SIZE_VAR, NFFT_VAR,
            WINDOW_VAR, FREQ_VAR, SPEC_3D_IMG_VAR, WINDOW_MAP_VAR);

    static final String SHOW_CHROMAGRAM =
        String.format("showChromagram(%s, %s, %s, %s, %s, %s, %s, %s);",
            SAMPLE_VAR, WINDOW_SIZE_VAR, HOP_SIZE_VAR, NFFT_VAR,
            WINDOW_VAR, FREQ_VAR, CHROM_IMG_VAR, WINDOW_MAP_VAR);

    static final String CUT_SONG =
        String.format("%s = cutSong(%s, %s, %s);",
            SAMPLE_VAR_2, SAMPLE_VAR, FROM_VAR, TO_VAR);

    static final String RELOCATE_SONG =
        String.format("relocateSong(%s, %s, %s);",
            PLAYER_VAR, START_VAR, EMPTY_VAR);

    static final String CHANGE_VOLUME =
        String.format("%s = changeVolume(%s, %s, %s);",
            PLAYER_VAR, PLAYER_VAR, SAMPLE_VAR, LEVEL_VAR);

    static final String CHANGE_PITCH =
        String.format("%s = audioplayer(%s, %s);",
            PLAYER_VAR, SAMPLE_VAR, FREQ_VAR_2);

    private static final String SAVE_SONG = "saveSong(%s, %s, %s);";

    static final String SAVE_SONG_CUT =
        String.format(SAVE_SONG, FILE_VAR, SAMPLE_VAR_2, FREQ_VAR);

    static final String SAVE_SONG_CHANGE_PITCH =
        String.format(SAVE_SONG, FILE_VAR, SAMPLE_VAR, FREQ_VAR_2);

    private MatlabCommands() {
        throw new AssertionError();
    }
}
