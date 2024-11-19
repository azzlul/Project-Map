package Service;
import Exceptions.ServiceException;
import Repository.*;
import Domain.Friendship;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

/**
 * Class that handles operations on a friendship repository.
 */
public class ServiceFriendship {
    /**
     * Repository of friendships with Integer ID.
     */
    private final Repository<Integer, Friendship> repo;

    /**
     * Constructor for class.
     * @param repo Friendship Repository
     */
    public ServiceFriendship(Repository<Integer, Friendship> repo) {
        this.repo = repo;
    }

    /**
     * Creates, validates and adds a friendship to repository.
     * @throws ServiceException if the friendship could not be added.
     * @param firstUserID ID of first User
     * @param secondUserID ID of second User
     */
    public void add(int firstUserID, int secondUserID) {
        Friendship friendship = new Friendship(firstUserID, secondUserID, LocalDateTime.now(), true);
        if(findByUserID(firstUserID, secondUserID).isPresent() || findByUserID(secondUserID, firstUserID).isPresent()) {
            throw new ServiceException("Friendship already exists");
        }
        var result = repo.save(friendship);
        if(result.isPresent()) throw new ServiceException("Friendship could not be added");
    }

    /**
     * Creates, validates and adds a friendship request to repository
     * @throws ServiceException if the friend request could not be added
     * @param firstUserID ID of first User
     * @param secondUserID ID of second User
     */
    public void addRequest(int firstUserID, int secondUserID){
        Friendship friendship = new Friendship(firstUserID, secondUserID, LocalDateTime.now(), false);
        if(findByUserID(firstUserID, secondUserID).isPresent() || findByUserID(secondUserID, firstUserID).isPresent()) {
            throw new ServiceException("Friendship already exists");
        }
        var result = repo.save(friendship);
        if(result.isPresent()) throw new ServiceException("Friendship request could not be added");
    }

    /**
     * Accepts a friend request
     * @param friendshipID ID of friend request
     */
    public void acceptRequest(int friendshipID){
        var friendship = find(friendshipID);
        friendship.setAccepted(true);
        repo.update(friendship);
    }
    /**
     * Creates, validates and updates a friendship from repository.
     *
     * @param friendshipID ID of friendship
     * @param firstUserID  ID of first user
     * @param secondUserID ID of second user
     * @param accepted true if the friend request was accepted
     * @throws ServiceException if friendship could not be updated
     */
    public void update(int friendshipID, int firstUserID, int secondUserID, LocalDateTime timestamp, boolean accepted) {
        Friendship friendship = new Friendship(firstUserID, secondUserID, timestamp, accepted);
        friendship.setId(friendshipID);
        var result = repo.update(friendship);
        if(result.isEmpty()) throw new ServiceException("Friendship could not be updated");
    }

    /**
     * Removes friendship from repository.
     * @throws ServiceException if friendship could not be removed
     * @param friendshipID ID of friendship
     */
    public void remove(int friendshipID) {
        var result = repo.delete(friendshipID);
        if(result.isEmpty()) throw new ServiceException("Friendship could not be removed");
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
    Optional<Friendship> findByUserID(int firstUserID, int secondUserID) {
        var it = findAll();
        Predicate<Friendship> found = friendship ->
                friendship.getFirstUserID() == firstUserID && friendship.getSecondUserID() == secondUserID ||
                        friendship.getFirstUserID() == secondUserID && friendship.getSecondUserID() == firstUserID;
        return StreamSupport.stream(it.spliterator(), false).filter(found).findFirst();
    }
    /**
     * Returns the size of the repository.
     * @return integer
     */
    public int size(){
        return repo.size();
    }
}
