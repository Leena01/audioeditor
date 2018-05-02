package org.ql.audioeditor.logic.exceptions;

/**
 * Exception for invalid operations.
 */
public final class InvalidOperationException extends Exception {
    /**
     * Constructor that accepts a message.
     *
     * @param message message to pass
     */
    public InvalidOperationException(String message) {
        super(message);
    }
}
