package Validator;

import Exceptions.ValidatorException;

/**
 * General purpose validator.
 */
public class Validator {
    /**
     * Constructor for class.
     */
    public Validator() {}
    /**
     * Verifies if an integer ID is valid.
     * An integer ID is valid if it's greater than 0.
     * @throws ValidatorException if the ID is not valid.
     * @param ID Integer ID
     */
    public static void validateIntID(int ID){
        if(ID <= 0) throw new ValidatorException("ID is invalid!");
    }
}
