package Service;
import Exceptions.ServiceException;
import Repository.*;
import Domain.Friendship;

import java.util.function.Predicate;
import java.util.stream.StreamSupport;

/**
 * Class that handles operations on a friendship repository.
 */
public class ServiceFriendship {
    /**
     * Repository of friendships with Integer ID.
     */
    private Repository<Integer, Friendship> repo = null;

    /**
     * Constructor for class.
     * @param repo Friendship Repository
     */
    public ServiceFriendship(Repository<Integer, Friendship> repo) {
        this.repo = repo;
    }

    /**
     * Creates, validates and adds a friendship to repository.
     * @throws ServiceException if friendship could not be added.
     * @param firstUserID ID of first User
     * @param secondUserID ID of second User
     */
    public void add(int firstUserID, int secondUserID) {
        Friendship friendship = new Friendship(firstUserID, secondUserID);
        var result = repo.save(friendship);
        if(result.isEmpty()) throw new ServiceException("Friendship could be added");
    }

    /**
     * Creates, validates and updates a friendship from repository.
     * @throws ServiceException if friendship could not be updated
     * @param friendshipID ID of friendship
     * @param firstUserID ID of first user
     * @param secondUserID ID of second user
     */
    public void update(int friendshipID, int firstUserID, int secondUserID) {
        Friendship friendship = new Friendship(firstUserID, secondUserID);
        friendship.setId(friendshipID);
        var result = repo.update(friendship);
        if(result.isEmpty()) throw new ServiceException("Friendship could be updated");
    }

    /**
     * Removes friendship from repository.
     * @throws ServiceException if friendship could not be removed
     * @param friendshipID ID of friendship
     */
    public void remove(int friendshipID) {
        var result = repo.delete(friendshipID);
        if(result.isEmpty()) throw new ServiceException("Friendship could be removed");
    }

    /**
     * Returns friendship from repository.
     * @throws ServiceException if friendship could not be found
     * @param friendshipID ID of friendship
     * @return friendship with the given ID
     */
    public Friendship find(int friendshipID) {
        var result = repo.findOne(friendshipID);
        if(result.isEmpty()) throw new ServiceException("Friendship could be not found");
        return result.get();
    }

    /**
     * Returns all friendships from repository.
     * @return iterable for friendship repository
     */
    public Iterable<Friendship> findAll() {
        return repo.findAll();
    }

    /**
     * Returns the friendship corresponding to the given user id's.
     * @throws ServiceException if the friendship is not found.
     * @param firstUserID ID of first user
     * @param secondUserID ID of second user
     * @return friendship with the given ID's
     */
    public Friendship findByUserID(int firstUserID, int secondUserID) {
        var it = findAll();
        Predicate<Friendship> found = friendship ->
                friendship.getFirstUserID() == firstUserID && friendship.getSecondUserID() == secondUserID ||
                        friendship.getFirstUserID() == secondUserID && friendship.getSecondUserID() == firstUserID;
        for(var friendship : it)  if(found.test(friendship)) return friendship;
        var friendship = StreamSupport.stream(it.spliterator(), false).filter(found).findFirst();
        if(friendship.isEmpty())throw new ServiceException("Friendship not found!");
        return friendship.get();
    }

    /**
     * Returns the size of the repository.
     * @return integer
     */
    public int size(){
        return repo.size();
    }
}
