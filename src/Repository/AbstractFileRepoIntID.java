package Repository;

import Domain.Entity;

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
        int lastIntID = 0;
        for(var x: findAll()){
            lastIntID = Math.max(lastIntID, x.getId());
        }
        return lastIntID+1;
    }
}
