package logic.exceptions;

public class InvalidOperationException extends Exception {
    public InvalidOperationException() {}

    // Constructor that accepts a message
    public InvalidOperationException(String message) {
        super(message);
    }
}
