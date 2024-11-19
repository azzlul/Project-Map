package Repository;

import java.util.Comparator;
import java.util.stream.StreamSupport;

public abstract class AbstractFileRepoIntID<Entity extends Domain.Entity<Integer>> extends AbstractFileRepo<Integer, Entity>{
    /**
     * Constructor for class.
     *
     * @param file_path path of the file where the entities are stored
     */
    public AbstractFileRepoIntID(String file_path) {
        super(file_path);
    }

    @Override
    protected Integer generateID() {
        Comparator<Entity> comparator = Comparator.comparing(Domain.Entity::getId);
        var maxIDEntity = StreamSupport.stream(findAll().spliterator(), false).max(comparator);
        return maxIDEntity.map(entity -> entity.getId() + 1).orElse(1);
    }
}
