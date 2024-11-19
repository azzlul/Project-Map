package Validator;

import Exceptions.ValidatorException;

/**
 * General purpose validator.
 */
public abstract class Validator<ID, Entity> {
    /**
     * Constructor for class.
     */
    public Validator() {}

    /**
     * Validates the entity given.
     * @param entity Entity to be validated.
     */
    public abstract void validate(Entity entity);
    /**
     *Verifies if an ID is valid
     * @throws ValidatorException if the ID is not valid.
     * @param ID ID
     */
    public abstract void validateID(ID ID);

    /**
     * Verifies if entity is null.
     * @throws IllegalArgumentException if entity is null
     * @param entity Entity
     */
    public void validateNull(Entity entity){
        if(entity == null) throw new IllegalArgumentException("Entity cannot be null");
    }
}
