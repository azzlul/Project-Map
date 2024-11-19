package Domain;

import java.util.Objects;

/**
 * Class that contains an uniquely identifiable ID.
 * @param <ID> ID's data type
 */
public abstract class Entity<ID> {
    /**
     * Constructor for class.
     */
    public Entity() {}

    /**
     * ID of entity.
     */
    private ID id;

    /**
     * Returns the ID.
     * @return ID
     */
    public ID getId() {
        return id;
    }

    /**
     * Replaces the current ID with the given ID.
     * @param id new ID
     */
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
