package logic.exceptions;

public class InvalidOperationException extends Exception {
    /**
     * Constructor that accepts a message
     * @param message message to pass
     */
    public InvalidOperationException(String message) {
        super(message);
    }
}
