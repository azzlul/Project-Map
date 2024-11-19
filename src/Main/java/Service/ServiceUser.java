package Service;
import Domain.Community;
import Domain.Friendship;
import Domain.User;
import Exceptions.ServiceException;
import Repository.Repository;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import utils.Observable;
import utils.Observer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Class that handles operations on a user repository.
 */
public class ServiceUser implements Observable {
    /**
     * Repository of users with Integer ID.
     */
    final Repository<Integer, User> repo;
    /**
     * Service for friendships.
     */
    final ServiceFriendship serviceFriendship;

    private ArrayList<Observer> observers = new ArrayList<>();
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
        if(findUserByName(name).isPresent()) throw new ServiceException("User already exists");
        repo.save(user);
        notifyObservers();
    }

    /**
     * Adds a friendship to the friendship repository
     * @param firstUserID ID of first user
     * @param secondUserID ID of second user
     */
    public void addFriendship(int firstUserID, int secondUserID){
        var rez1  = repo.findOne(firstUserID);
        if(rez1.isEmpty()) throw new ServiceException("Friendship could not be added");
        var rez2  = repo.findOne(secondUserID);
        if(rez2.isEmpty()) throw new ServiceException("Friendship could not be added");
        serviceFriendship.add(firstUserID, secondUserID);
        notifyObservers();
    }

    /**
     * Adds a friendship to the friendship repository
     * @param firstUserID ID of first user
     * @param secondUserID ID of second user
     */
    public void addFriendRequest(int firstUserID, int secondUserID){
        var rez1  = repo.findOne(firstUserID);
        if(rez1.isEmpty()) throw new ServiceException("Friendship could not be added");
        var rez2  = repo.findOne(secondUserID);
        if(rez2.isEmpty()) throw new ServiceException("Friendship could not be added");
        serviceFriendship.addRequest(firstUserID, secondUserID);
        notifyObservers();
    }

    /**
     * Accepts a friend request
     * @param friendshipID ID of friendship
     */
    public void acceptFriendRequest(int friendshipID){
        serviceFriendship.acceptRequest(friendshipID);
        notifyObservers();
    }
    /**
     * Removes a friendship from the friendship repository
     * @throws ServiceException if friendship could not be removed.
     * @param friendshipID ID of the friendship
     */
    public void removeFriendship(int friendshipID){
        serviceFriendship.remove(friendshipID);
        notifyObservers();
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
        notifyObservers();
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
     * Returns the first user with the given name
     * @param name String
     * @return User
     */
    public Optional<User> findUserByName(String name){
        for(var user : repo.findAll()){
            if(user.getName().equals(name)) return Optional.of(user);
        }
        return Optional.empty();
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

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
