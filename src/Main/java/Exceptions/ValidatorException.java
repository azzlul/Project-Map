package Exceptions;

/**
 * Exception from the Validator Layer.
 */
public class ValidatorException extends RuntimeException {
    /**
     * Constructor for class.
     * @param message exception message
     */
    public ValidatorException(String message) {
        super(message);
    }
}
