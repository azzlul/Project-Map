package Validator;
import Domain.*;
import Exceptions.ValidatorException;

import java.util.Objects;

/**
 * Validator for users.
 */
public class ValidatorUser extends ValidatorInt<User> {
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
    public void validate(User user){
        String errors = null;
        if(user.getName() == null || Objects.equals(user.getName(), "\n")) errors += "Nume invalid!";
        if (errors != null) throw new ValidatorException(errors);
    }
}
