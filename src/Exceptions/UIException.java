package Exceptions;

/**
 * Exception from the UI layer.
 */
public class UIException extends RuntimeException {
    /**
     * Constructor for class.
     * @param message exception message
     */
    public UIException(String message) {
        super(message);
    }
}
