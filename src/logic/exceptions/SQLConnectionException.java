package logic.exceptions;

/**
 * Created by Livia on 2017.08.31..
 */
public class SQLConnectionException extends Exception {
    public SQLConnectionException() {}

    // Constructor that accepts a message
    public SQLConnectionException(String message) {
        super(message);
    }
}
