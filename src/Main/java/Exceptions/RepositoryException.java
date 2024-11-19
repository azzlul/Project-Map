package Exceptions;

/**
 * Exception from the Repository layer.
 */
public class RepositoryException extends RuntimeException {
    /**
     * Constructor for class.
     * @param message exception message
     */
    public RepositoryException(String message) {
        super(message);
    }
}
