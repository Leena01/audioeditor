package org.ql.audioeditor.logic.matlab;

/**
 * MATLAB variable names.
 */
enum MatlabVariables {
    FILE_VAR("file"),
    FOLDER_PATH_VAR("folderpath"),
    WINDOW_KEYS_VAR("windowkeys"),
    TOTAL_VAR("total"),
    FREQ_VAR("fs"),
    FREQ_VAR_2("fs2"),
    PLOT_IMG_VAR("imgname"),
    SPEC_IMG_VAR("imgname2"),
    SPEC_3D_IMG_VAR("imgname3"),
    CHROM_IMG_VAR("imgname4"),
    ONSET_DET_VAR("imgname5"),
    START_VAR("start"),
    CURRENT_FRAME_VAR("current"),
    LEVEL_VAR("level"),
    EMPTY_VAR("empty"),
    IS_PLAYING_VAR("isplaying"),
    WINDOW_SIZE_VAR("wlen"),
    NOVERLAP_SIZE_VAR("noverlap"),
    NFFT_VAR("nfft"),
    WINDOW_VAR("window"),
    FROM_VAR("from"),
    TO_VAR("to"),
    PLAYER_VAR("player"),
    WINDOW_MAP_VAR("windowmap"),
    SAMPLE_VAR("x"),
    SAMPLE_VAR_2("y"),
    BEAT_EST_VAR("beatest"),
    MIN_BPM_VAR("minbpm"),
    MAX_BPM_VAR("maxbpm"),
    BPM_VAR("bpm"),
    S_VAR("s"),
    BASE_VAR("base"),
    SMALLEST_VAR("smallest"),
    NOTE_VAR("note"),
    SCALE_VAR("scale");

    private final String variable;

    /**
     * Constructor.
     *
     * @param variable MATLAB variable name
     */
    MatlabVariables(String variable) {
        this.variable = variable;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String toString() {
        return variable;
    }
}
