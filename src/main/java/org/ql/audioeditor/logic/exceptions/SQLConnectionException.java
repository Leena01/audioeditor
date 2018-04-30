package org.ql.audioeditor.logic.exceptions;

/**
 * Created by Livia on 2017.08.31..
 */
public class SQLConnectionException extends Exception {
    /**
     * Constructor that accepts a message.
     *
     * @param message message to pass
     */
    public SQLConnectionException(String message) {
        super(message);
    }
}
