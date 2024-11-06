package Service;
import Domain.Community;
import Domain.Friendship;
import Domain.User;
import Exceptions.ServiceException;
import Repository.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Class that handles operations on a user repository.
 */
public class ServiceUser {
    /**
     * Repository of users with Integer ID.
     */
    Repository<Integer, User> repo;
    /**
     * Service for friendships.
     */
    ServiceFriendship serviceFriendship;

    /**
     * Constructor for class.
     * @param repo user repository
     * @param repoFriendship friendship repository
     */
    public ServiceUser(Repository<Integer, User> repo, Repository<Integer, Friendship> repoFriendship) {
        this.repo = repo;
        serviceFriendship = new ServiceFriendship(repoFriendship);
    }

    /**
     * Creates, validates and adds a user to the repository.
     * @throws ServiceException if user couldn't be added
     * @param name username
     */
    public void addUser(String name){
        var user = new User(name);
        repo.save(user);
    }

    /**
     * Adds a friendship to the friendship repository, and updates the user's friends list accordingly.
     * @param firstUserID ID of first user
     * @param secondUserID ID of second user
     */
    public void addFriendship(int firstUserID, int secondUserID){
        var rez1  = repo.findOne(firstUserID);
        var rez2  = repo.findOne(secondUserID);
        if(rez1.isEmpty() || rez2.isEmpty()) throw new ServiceException("Friendship could not be added");
        serviceFriendship.add(firstUserID, secondUserID);
    }

    /**
     * Removes a friendship from the friendship repository, and updates the user's friends list accordingly.
     * @throws ServiceException if friendship could not be removed.
     * @param friendshipID ID of the friendship
     */
    public void removeFriendship(int friendshipID){
        Friendship friendship = serviceFriendship.find(friendshipID);
        var rez1 = repo.findOne(friendship.getFirstUserID());
        var rez2 = repo.findOne(friendship.getSecondUserID());
        if(rez1.isEmpty() || rez2.isEmpty()) throw new ServiceException("Friendship could not be removed");
        serviceFriendship.remove(friendshipID);
    }

    /**
     * Removes user from the repository and all friends list.
     * @throws ServiceException if user couldn't be removed
     * @param userID ID of user
     */
    public void removeUser(int userID){
        var rez = repo.findOne(userID);
        if(rez.isEmpty()) throw new ServiceException("User could not be removed");
        User user = rez.get();
        ArrayList<Integer> friendshipsToRemove = new ArrayList<>();
        serviceFriendship.findAll().forEach(friendship -> {
            if(friendship.getFirstUserID() == user.getId() || friendship.getSecondUserID() == user.getId()) friendshipsToRemove.add(friendship.getId());
        });
        friendshipsToRemove.forEach(serviceFriendship::remove);
        repo.delete(userID);
    }

    /**
     * Returns the user with the given ID.
     * @throws ServiceException if user couldn't be found
     * @param userID ID of user
     * @return User with the given ID
     */
    public User findUser(int userID){
        var rez =  repo.findOne(userID);
        if(rez.isEmpty()) throw new ServiceException("User could not be found");
        return rez.get();
    }

    /**
     * Returns an iterable for all users.
     * @return iterable for all users
     */
    public Iterable<User> findAllUsers(){
        return repo.findAll();
    }

    /**
     * Returns an iterable for all friendships.
     * @return iterable for all friendships
     */
    public Iterable<Friendship> findAllFriendships(){
        return serviceFriendship.findAll();
    }

    /**
     * Returns the size of the user repository.
     * @return integer
     */
    public int sizeUsers(){
        return repo.size();
    }

    /**
     * Returns the size of the friendship repository.
     * @return integer
     */
    public int sizeFriendships(){
        return serviceFriendship.size();
    }

    /**
     * Adds to the community the given user and all users linked to the first user by a friendship chain.
     * @param user User
     * @param community Community
     * @param addedUsers ArrayList that contains ID's of all users that were added to any community
     */
    private void createCommunity(User user, Community community, ArrayList<Integer> addedUsers){
        community.getUsers().add(user);
        community.setSize(community.getSize() + 1);
        addedUsers.add(user.getId());
//        user.getFriendsID().forEach(fr -> {
//            if(predicate.test(fr))
//                createCommunity(findUser(fr), community, addedUsers);
//        });
        findAllFriendships().forEach(
                friendship -> {
                    if(friendship.getSecondUserID() == user.getId() && !addedUsers.contains(friendship.getFirstUserID()))
                        createCommunity(findUser(friendship.getFirstUserID()), community, addedUsers);
                    else if((friendship.getFirstUserID() == user.getId() && !addedUsers.contains(friendship.getSecondUserID())))
                        createCommunity(findUser(friendship.getSecondUserID()), community, addedUsers);
                }
        );
    }

    /**
     * Creates an array list of all communities.
     * @return array list of communities
     */
    private ArrayList<Community> getCommunites(){
        ArrayList<Community> communities = new ArrayList<>();
        ArrayList<Integer> addedUsers = new ArrayList<>();
        Predicate<Integer> predicate = addedUsers::contains;
        repo.findAll().forEach(user ->{
            if(predicate.negate().test(user.getId())){
                var community = new Community();
                createCommunity(user, community, addedUsers);
                communities.add(community);
            }
        });
        return communities;
    }

    /**
     * Returns the number of communities.
     * @return integer
     */
    public int allCommunities(){
        var communities = getCommunites();
        return communities.size();
    }

    /**
     * Returns the biggest community.
     * @throws ServiceException if there are no communities
     * @return Community
     */
    public Community maxCommunity(){
        var communities = getCommunites();
        Comparator<Community> comparator = Comparator.comparing(Community::getSize);
        var maxCom = communities.stream().max(comparator);
        if(maxCom.isEmpty()) throw new ServiceException("No community found");
        return maxCom.get();
    }
}
