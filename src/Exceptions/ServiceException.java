package Exceptions;

/**
 * Exception from the Service layer.
 */
public class ServiceException extends RuntimeException {
    /**
     * Constructor for class.
     * @param message exception message
     */
    public ServiceException(String message) {
        super(message);
    }
}
