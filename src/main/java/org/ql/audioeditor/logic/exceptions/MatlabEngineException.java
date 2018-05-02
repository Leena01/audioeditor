package org.ql.audioeditor.logic.exceptions;

/**
 * Class for exceptions caused by the MATLAB Engine.
 */
public final class MatlabEngineException extends Exception {
    /**
     * Constructor that accepts a message.
     *
     * @param message message to pass
     */
    public MatlabEngineException(String message) {
        super(message);
    }
}
