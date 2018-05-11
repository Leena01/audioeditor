package org.ql.audioeditor.common.util;

/**
 * Utility class for handling time conversion.
 */
public final class TimeUtils {

    public static final int MILLIS_SECONDS_CONVERSION = 1000;
    private static final int HOUR_CONVERSION = 3600;
    private static final int MINUTE_CONVERSION = 60;

    /**
     * Private constructor. May not be called.
     */
    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * Converts seconds to the following format: hh:mm:ss.
     *
     * @param duration Duration in seconds
     * @return Formatted string
     */
    public static String formatDuration(long duration) {
        long absSeconds = Math.abs(duration);
        String positive = String.format(
            "%d:%02d:%02d",
            absSeconds / HOUR_CONVERSION,
            (absSeconds % HOUR_CONVERSION) / MINUTE_CONVERSION,
            absSeconds % MINUTE_CONVERSION);
        if (duration < 0) {
            return "-" + positive;
        }
        return positive;
    }

    /**
     * Converts frames to seconds according to the sample rate given.
     *
     * @param frame Number of frames
     * @param freq  Sample rate
     * @return Seconds
     */
    public static int framesToSeconds(double frame, double freq) {
        return (int) (frame / freq);
    }

    /**
     * Converts seconds to frames according to the sample rate given.
     *
     * @param seconds Seconds
     * @param freq    Sample rate
     * @return Number of frames
     */
    public static int secondsToFrames(int seconds, double freq) {
        return (int) (seconds * freq);
    }
}
