package org.ql.audioeditor.common.util;

import java.awt.event.ActionEvent;

/**
 * Utility class for checking the time passed between two successive action
 * events.
 */
public final class DoubleActionTool {
    private static final int DOUBLE_PRESS_SPEED = 300;
    private static long timeSinceLastAction = 0;

    /**
     * Private constructor.
     */
    private DoubleActionTool() {
        throw new AssertionError();
    }

    /**
     * Returns true if keys are pressed twice within the given time interval.
     *
     * @param ae Action event
     * @return Logical value
     */
    public static boolean isDoubleAction(ActionEvent ae) {
        if ((ae.getWhen() - timeSinceLastAction) <= DOUBLE_PRESS_SPEED) {
            return true;
        } else {
            timeSinceLastAction = ae.getWhen();
        }
        return false;
    }
}
