package Service;
import Exceptions.ServiceException;
import Exceptions.ValidatorException;
import Repository.*;
import Domain.Friendship;
import Validator.ValidatorFriendship;
import Validator.Validator;

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
     * @param firstUserID ID of first User
     * @param secondUserID ID of second User
     */
    public void add(int firstUserID, int secondUserID) {
        Friendship friendship = new Friendship(firstUserID, secondUserID);
        ValidatorFriendship.validate(friendship);
        repo.add(friendship);
    }

    /**
     * Creates, validates and updates a friendship from repository.
     * @param friendshipID ID of friendship
     * @param firstUserID ID of first user
     * @param secondUserID ID of second user
     */
    public void update(int friendshipID, int firstUserID, int secondUserID) {
        Friendship friendship = new Friendship(firstUserID, secondUserID);
        ValidatorFriendship.validate(friendship);
        friendship.setId(friendshipID);
        repo.update(friendship);
    }

    /**
     * Removes friendship from repository.
     * @param friendshipID ID of friendship
     */
    public void remove(int friendshipID) {
        Validator.validateIntID(friendshipID);
        repo.remove(friendshipID);
    }

    /**
     * Returns friendship from repository.
     * @param friendshipID ID of friendship
     * @return friendship with the given ID
     */
    public Friendship find(int friendshipID) {
        Validator.validateIntID(friendshipID);
        return repo.find(friendshipID);
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
        for(var friendship : it) {
            if((friendship.getFirstUserID() == firstUserID && friendship.getSecondUserID() == secondUserID) ||
                friendship.getFirstUserID() == secondUserID && friendship.getSecondUserID() == firstUserID) {
                return friendship;
            }
        }
        throw new ServiceException("Friendship not found!");
    }

    /**
     * Returns the size of the repository.
     * @return integer
     */
    public int size(){
        return repo.size();
    }
}
