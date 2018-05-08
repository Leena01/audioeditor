package org.ql.audioeditor.common.matlab;

/**
 * List of MATLAB files to use.
 */
public enum MatlabFiles {
    CHANGE_PITCH("changePitch.m"),
    CHANGE_VOLUME("changeVolume.m"),
    CREATE_WINDOW_MAP("createWindowMap.m"),
    CUT_SONG("cutSong.m"),
    DELETE_SONG("deleteSong.m"),
    FREQ_BIN("freqBin.m"),
    GET_C_MATRIX("getCMatrix.m"),
    GET_CURRENT_FRAME("getCurrentFrame.m"),
    IS_PLAYING("isPlaying.m"),
    ISTFT("istft.m"),
    MP3WRITE("mp3write.m"),
    OPEN_SONG("openSong.m"),
    PAUSE_SONG("pauseSong.m"),
    PLOT_SONG("plotSong.m"),
    RELOCATE_SONG("relocateSong.m"),
    RESUME_SONG("resumeSong.m"),
    SAVE_SONG("saveSong.m"),
    SHOW_CHROMAGRAM("showChromagram.m"),
    SHOW_SPECTROGRAM("showSpectrogram.m"),
    SHOW_SPECTROGRAM_3D("showSpectrogram3d.m"),
    STFT("stft.m"),
    FILTERBANK("filterbank.m"),
    HWINDOW("hwindow.m"),
    DIFFRECT("diffrect.m"),
    TIMECOMB("timecomb.m"),
    BEAT_ALGO("beatAlgo.m"),
    CALC_MIN_DISTANCE("calcMinDistance.m"),
    ONSET_DET("onsetDet.m");

    private final String filePath;

    MatlabFiles(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the string representation of the filename.
     *
     * @return Filename
     */
    @Override
    public String toString() {
        return filePath;
    }
}
