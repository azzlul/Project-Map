package Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import Validator.*;
/**
 * Repository where the information is stored in RAM.
 * @param <ID> ID type of Entity
 * @param <Entity> type of stored Entity
 */
public abstract class AbstractInMemoryRepo<ID, Entity extends Domain.Entity<ID>> implements Repository<ID, Entity> {
    /**
     * List of all entities in repository.
     */
    final Map<ID, Entity> entities;
    /**
     * Validator for stored entities
     */
    Validator<ID, Entity> validator;
    /**
     * Constructor for class.
     */
    public AbstractInMemoryRepo() {
        entities = new HashMap<>();
    }

    /**
     * Function that generates an ID for a newly added entity.
     * @return new and unique ID
     */
    protected abstract ID generateID();


    @Override
    public Optional<Entity> findOne(ID id) {
        validator.validateID(id);
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<Entity> findAll() {
        return entities.values();
    }

    @Override
    public Optional<Entity> save(Entity entity) {
        validator.validateNull(entity);
        validator.validate(entity);
        entity.setId(generateID());
        var result = entities.put(entity.getId(), entity);
        if(result == null) return Optional.of(entity);
        else return Optional.empty();
    }

    @Override
    public Optional<Entity> delete(ID id) {
        validator.validateID(id);
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<Entity> update(Entity entity) {
        validator.validateNull(entity);
        validator.validate(entity);
        var result = entities.put(entity.getId(), entity);
        if (result == null) return Optional.of(entity);
        return Optional.empty();
    }

    @Override
    public int size() {
        return entities.size();
    }
}
