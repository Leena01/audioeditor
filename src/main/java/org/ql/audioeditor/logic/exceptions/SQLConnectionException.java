package org.ql.audioeditor.logic.exceptions;

/**
 * Class for exceptions caused by the SQL connection.
 */
public final class SQLConnectionException extends Exception {
    /**
     * Constructor that accepts a message.
     *
     * @param message message to pass
     */
    public SQLConnectionException(String message) {
        super(message);
    }
}
