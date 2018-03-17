package logic.exceptions;

public class MatlabEngineException extends Exception {
    public MatlabEngineException() {}

    // Constructor that accepts a message
    public MatlabEngineException(String message) {
        super(message);
    }
}
