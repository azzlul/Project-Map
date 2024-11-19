package Validator;
public abstract class ValidatorInt<Entity> extends Validator<Integer, Entity>{
    /**
     * Verifies if an integer id is valid.
     * An integer id is valid if it is not equal to 0.
     * @throws IllegalArgumentException if id is invalid
     * @param ID Integer ID
     */
    public void validateID(Integer ID) {
        if(ID == 0) throw new IllegalArgumentException("ID is null!");
    }
}
