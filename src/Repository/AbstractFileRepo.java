package Repository;

import Validator.Validator;

import java.io.*;
import java.util.Optional;

/**
 * Repository that stores information in a given file.
 * @param <ID> ID type of Entity
 * @param <Entity> type of stored entity
 */
public abstract class AbstractFileRepo<ID, Entity extends Domain.Entity<ID>> extends AbstractInMemoryRepo<ID, Entity> {
    /**
     * Path for file
     */
    String filePath;

    /**
     * Constructor for class.
     * @param file_path path of the file where the entities are stored
     */
    public AbstractFileRepo(String file_path) {
        super();
        this.filePath = file_path;
    }

    /**
     * Converts a string that represents an Entity into an Entity.
     * @param line string
     * @return Entity
     */
    protected abstract Entity readEntity(String line);

    /**
     * Converts entity to string format.
     * @param entity Entity
     * @return string
     */
    protected abstract String writeEntity(Entity entity);

    /**
     * Writes all the information from the Repository into the file.
     */
    private void writeToFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            for (Entity entity: entities.values()) {
                String ent = writeEntity(entity);
                writer.write(ent);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads all information from the file into the repository.
     */
    private void readFromFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = reader.readLine()) != null){
                Entity entity = readEntity(line);
                entities.put(entity.getId(), entity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Entity> findOne(ID id) {
        readFromFile();
        return super.findOne(id);
    }

    @Override
    public Iterable<Entity> findAll() {
        readFromFile();
        return super.findAll();
    }

    @Override
    public Optional<Entity> save(Entity entity) {
        readFromFile();
        var result = super.save(entity);
        writeToFile();
        return result;
    }

    @Override
    public Optional<Entity> delete(ID id) {
        readFromFile();
        var result = super.delete(id);
        writeToFile();
        return result;
    }

    @Override
    public Optional<Entity> update(Entity entity) {
        readFromFile();
        var result = super.update(entity);
        writeToFile();
        return result;
    }

    @Override
    public int size() {
        readFromFile();
        return entities.size();
    }
}
