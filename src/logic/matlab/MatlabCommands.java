package logic.matlab;

final class MatlabCommands {
    /**
     * Matlab language injection
     */
    static final String PATH_VAR = "path";
    static final String TOTAL_VAR = "total";
    static final String FREQ_VAR = "fs";
    static final String IMG_VAR = "imgname";
    static final String START_VAR = "start";
    static final String LEVEL_VAR = "level";
    private static final String SAMPLE_VAR = "x";
    private static final String PLAYER_VAR = "player";

    static final String OPEN_SONG = "[" + SAMPLE_VAR + ", " + FREQ_VAR + "] = audioread(" + PATH_VAR + "); " +
            PLAYER_VAR + " = audioplayer(" + SAMPLE_VAR + ", " + FREQ_VAR + "); " +
            TOTAL_VAR + " = get(" + PLAYER_VAR + ", 'TotalSamples');";

    static final String PLOT_SONG = "figure('visible', 'off');" +
            "x1 = " + SAMPLE_VAR + "(:, 1); xlen = length(" + SAMPLE_VAR + "); " +
            "tlen = xlen/" + FREQ_VAR + "; tper = 1/" + FREQ_VAR + "; " +
            "t = 0:tper:(xlen/" + FREQ_VAR + ") - tper; " +
            "p = plot(t, x1); set(gca, 'Color', 'k'); " +
            "axis([t(1) t(end) -max(x1) max(x1)]); " +
            "ax = gca; box off; " +
            "set(ax, 'XTick', [], 'YTick', []); " +
            "outerpos = ax.OuterPosition; " +
            "ti = ax.TightInset; " +
            "left = outerpos(1) + ti(1); " +
            "bottom = outerpos(2) + ti(2); " +
            "ax_width = outerpos(3) - ti(1) - ti(3); " +
            "ax_height = outerpos(4) - ti(2) - ti(4); " +
            "ax.Position = [left bottom ax_width ax_height]; " +
            "hgexport(gcf, " + IMG_VAR + ", hgexport('factorystyle'), 'Format', 'png');";

    static final String PAUSE_SONG = "pause(" + PLAYER_VAR + ")";
    static final String RESUME_SONG = "resume(" + PLAYER_VAR + ")";
    static final String STOP_SONG = "stop(" + PLAYER_VAR + ")";
    static final String RELOCATE_SONG_EMPTY = "stop(" + PLAYER_VAR + "); play(" + PLAYER_VAR + ");";
    static final String RELOCATE_SONG = "stop(" + PLAYER_VAR + "); play(" + PLAYER_VAR + ", " + START_VAR + ");";

    static final String CHANGE_VOLUME = "current = get(" + PLAYER_VAR + ", 'CurrentSample'); " +
            "y = " + SAMPLE_VAR + " * " + LEVEL_VAR + "; " +
            PLAYER_VAR + " = audioplayer(y, " + FREQ_VAR + "); play(" + PLAYER_VAR + ", current);";
}
