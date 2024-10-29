package Repository;
import Domain.Entity;
import Exceptions.RepositoryException;

/**
 * Interface class for a Repository.
 * @param <ID> ID type of Entity
 * @param <Entity> type of the stored entity
 */
public interface Repository<ID, Entity extends Domain.Entity<ID>> {
    /**
     * Adds a new entity to the repository.
     * @param entity Entity to be added
     */
    public void add(Entity entity);

    /**
     * Updates an entity from the repository.
     * @throws RepositoryException if the given user's ID is not found.
     * @param entity updated Entity
     */
    public void update(Entity entity);

    /**
     * Removes the entity with the given ID.
     * @throws RepositoryException if the ID is not found.
     * @param id ID of entity to be removed
     */
    public void remove(ID id);

    /**
     * Returns the user with the given ID.
     * @throws RepositoryException of the ID is not found.
     * @param id ID of entity to be returned
     * @return Entity with the given ID
     */
    public Entity find(ID id);

    /**
     * Returns all entities.
     * @return iterable for the repository
     */
    public Iterable<Entity> findAll();

    /**
     * Returns size of repository.
     * @return integer
     */
    public int size();
}
