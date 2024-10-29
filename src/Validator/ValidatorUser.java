package Validator;
import Domain.*;
import Exceptions.ValidatorException;

/**
 * Validator for users.
 */
public class ValidatorUser {
    /**
     * Constructor for class.
     */
    public ValidatorUser() {}
    /**
     * Verifies if a user is valid.
     * A user is valid if the name is not a null String.
     * @throws ValidatorException if user is not valid.
     * @param user User
     */
    public static void validate(User user){
        String errors = null;
        if(user.getName() == null) errors += "Nume invalid!";
        if (errors != null) throw new ValidatorException(errors);
    }
}
