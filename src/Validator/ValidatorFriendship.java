package Validator;
import Domain.*;
import Exceptions.ValidatorException;

/**
 * Validator for friendships.
 */
public class ValidatorFriendship {
    /**
     * Constructor for class.
     */
    public ValidatorFriendship() {}
    /**
     * Verifies if a friendship is valid.
     * A friendship is valid if its user IDs are greater than 0.
     * @throws ValidatorException if the friendship is not valid.
     * @param friendship Friendship
     */
    public static void validate(Friendship friendship) {
        String errors = null;
        if(friendship.getFirstUserID() <= 0) errors += "First user ID invalid!";
        if(friendship.getSecondUserID() <= 0) errors += "Second user ID invalid!";
        if(errors != null) throw new ValidatorException(errors);
    }
}
