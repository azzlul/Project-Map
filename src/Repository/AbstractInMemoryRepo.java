package Repository;

import java.util.HashMap;
import java.util.Map;

import Exceptions.RepositoryException;

/**
 * Repository where the information is stored in RAM.
 * @param <ID> ID type of Entity
 * @param <Entity> type of stored Entity
 */
public abstract class AbstractInMemoryRepo<ID, Entity extends Domain.Entity<ID>> implements Repository<ID, Entity> {
    /**
     * List of all entities in repository.
     */
    Map<ID, Entity> entities;
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
    public void add(Entity entity) {
        entity.setId(generateID());
        entities.put(entity.getId(), entity);
    }

    @Override
    public void update(Entity entity) {
        if(!entities.containsKey(entity.getId())) throw new RepositoryException("Entity does not exist");
        entities.put(entity.getId(), entity);
    }

    @Override
    public void remove(ID id) {
        if(!entities.containsKey(id)) throw new RepositoryException("Entity does not exist");
        entities.remove(id);
    }

    @Override
    public Entity find(ID id) {
        if(!entities.containsKey(id)) throw new RepositoryException("Entity does not exist");
        return entities.get(id);
    }

    @Override
    public Iterable<Entity> findAll() {
        return entities.values();
    }

    /**
     * Generates an ID for an integer-based ID.
     * @return new ID
     */
    protected int generateIDInt(){
        int lastIntID = 0;
        for(var x: findAll()){
            lastIntID = Math.max(lastIntID, (int)x.getId());
        }
        return lastIntID+1;
    }

    @Override
    public int size() {
        return entities.size();
    }
}
