package Repository.Database;

import Domain.Entity;
import Exceptions.RepositoryException;
import Repository.Repository;
import Validator.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;

public abstract class AbstractDbRepo<ID, E extends Entity<ID>> implements Repository<ID, E> {
    Connection connection;
    Validator<ID, E> validator;
    /**
     * Creates an entity based on the query result
     * @param entity query result
     * @return Entity
     */
    protected abstract E readEntity(ResultSet entity) throws SQLException;

    /**
     * Inserts entity into database
     * @param entity Entity
     */
    protected abstract void  insertDb(E entity) throws SQLException;

    /**
     * Updates entity in database
     * @param entity Entity
     */
    protected abstract void updateDb(E entity) throws SQLException;

    /**
     * Deletes entity from database
     * @param id ID
     */
    protected abstract void deleteDb(ID id) throws SQLException;

    /**
     * Returns the result from searching the given id in database
     * @param id ID
     * @return result of find
     */
    protected abstract ResultSet findDb(ID id) throws SQLException;

    /**
     * Returns all records from the database table
     * @return result of find
     */
    protected abstract ResultSet findAllDb() throws SQLException;

    /**
     * Returns number of records from table
     * @return result
     */
    protected abstract ResultSet sizeDb() throws SQLException;

    @Override
    public Optional<E> findOne(ID id) {
        validator.validateID(id);
        try{
            var result = findDb(id);
            if(!result.next()) return Optional.empty();
            return Optional.of(readEntity(result));
        }
        catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Iterable<E> findAll() {
        try {
            var result = findAllDb();
            var set = new HashSet<E>();
            while(result.next()) {
                set.add(readEntity(result));
            }
            return set;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<E> save(E entity) {
        validator.validateNull(entity);
        validator.validate(entity);
        try{
            insertDb(entity);
            return Optional.empty();
        }
        catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<E> delete(ID id) {
        validator.validateID(id);
        try{
            var result = findOne(id);
            if(result.isEmpty()) return Optional.empty();
            deleteDb(id);
            return result;
        }
        catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<E> update(E entity) {
        validator.validateNull(entity);
        validator.validate(entity);
        try{
            if(findOne(entity.getId()).isEmpty()) return Optional.of(entity);
            System.out.println("test");
            updateDb(entity);
            return Optional.empty();
        }
        catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public int size() {
        try{
            var result = sizeDb();
            result.next();
            return result.getInt(1);
        }
        catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
    }
}
