package Service;
import Domain.Community;
import Domain.Friendship;
import Domain.Message;
import Domain.User;
import Exceptions.ServiceException;
import Repository.Database.FriendshipDbRepo;
import Repository.Database.MessageDbRepo;
import Repository.Database.UserDbRepo;
import Repository.Repository;
import utils.Observable;
import utils.Observer;
import utils.Page;
import utils.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Class that handles operations on a user repository.
 */
public class ServiceUser implements Observable {
    /**
     * Repository of users with Integer ID.
     */
    final UserDbRepo repo;
    /**
     * Service for friendships.
     */
    final ServiceFriendship serviceFriendship;
    final ServiceMessage serviceMessage;

    private final ArrayList<Observer> observers = new ArrayList<>();
    /**
     * Constructor for class.
     * @param repo user repository
     * @param repoFriendship friendship repository
     */
    public ServiceUser(UserDbRepo repo, FriendshipDbRepo repoFriendship, MessageDbRepo repoMessage) {
        this.repo = repo;
        serviceFriendship = new ServiceFriendship(repoFriendship);
        serviceMessage = new ServiceMessage(repoMessage);
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

    public void addMessage(int fromID, int toID, String message, LocalDateTime date,  int replyID){
        var rez1  = repo.findOne(fromID);
        if(rez1.isEmpty()) throw new ServiceException("Message could not be added");
        var rez2  = repo.findOne(toID);
        if(rez2.isEmpty()) throw new ServiceException("Message could not be added");
        serviceMessage.add(fromID, toID, message, date, replyID);
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

    public void updateMessage(int messageId, int toId, int fromId, String message, LocalDateTime date, int replyID){
        serviceMessage.update(messageId, toId, fromId, message, date, replyID);
    }
    public void setFirstMessage(int firstUserID, int secondUserID, int messageID){
        var rez1  = repo.findOne(firstUserID);
        if(rez1.isEmpty()) throw new ServiceException("Message could not be set");
        var rez2  = repo.findOne(secondUserID);
        if(rez2.isEmpty()) throw new ServiceException("Message could not be set");
        var friendship = serviceFriendship.findByUserID(firstUserID, secondUserID);
        if(friendship.isEmpty()) throw new ServiceException("Message could not be set");
        serviceFriendship.update(friendship.get().getId(), friendship.get().getFirstUserID(), friendship.get().getSecondUserID(), friendship.get().getFriendsFrom(),
                messageID, friendship.get().isAccepted());
        notifyObservers();
    }

    public void setReplyMessage(int firstMessageID, int secondMessageID){
        var msg = serviceMessage.find(firstMessageID);
        serviceMessage.update(firstMessageID, msg.getToID(), msg.getFromID(), msg.getMessage(), msg.getDateTime(), secondMessageID);
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
        repo.delete(userID);
        notifyObservers();
    }

    public void removeMessage(int messageID){
        serviceMessage.remove(messageID);
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

    public Message findMessage(int messageID){
        return serviceMessage.find(messageID);
    }

    public Iterable<Message> findMessagesFromUsers(int userid1, int userid2){
        var rez =  repo.findOne(userid1);
        if(rez.isEmpty()) throw new ServiceException("Message could not be found");
        var rez2 = repo.findOne(userid2);
        if(rez2.isEmpty()) throw new ServiceException("Message could not be found");
        return serviceMessage.findMessagesFromUsers(userid1, userid2);
    }

    public Message findLastMessageFromUsers(int userid1, int userid2){
        var rez =  repo.findOne(userid1);
        if(rez.isEmpty()) throw new ServiceException("Message could not be found");
        var rez2 = repo.findOne(userid2);
        if(rez2.isEmpty()) throw new ServiceException("Message could not be found");
        return serviceMessage.findLastMessageFromUsers(userid1, userid2);
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

    public Optional<Friendship> findFriendshipByUserID(int userID1, int userID2){
        return serviceFriendship.findByUserID(userID1, userID2);
    }
    /**
     * Returns an iterable for all users.
     * @return iterable for all users
     */
    public Iterable<User> findAllUsers(){
        return repo.findAll();
    }

    public Page<User> findAllUsersOnPage(Pageable pageable){
        return repo.findAllOnPage(pageable);
    }
    public Page<Friendship> findALlFriendshipsByUserIDPaged(int userID, Pageable pageable){
        findUser(userID);
        return serviceFriendship.findAllUserIDPaged(userID, pageable);
    }
    /**
     * Returns an iterable for all friendships.
     * @return iterable for all friendships
     */
    public Iterable<Friendship> findAllFriendships(){
        return serviceFriendship.findAll();
    }

    public Iterable<Message> findAllMessages(){
        return serviceMessage.findAll();
    }

    public Iterable<Friendship> findAllFriendshipsByUserID(int userID){
        findUser(userID);
        return serviceFriendship.findAllByUserID(userID);
    }

    public Iterable<Friendship> findRequestByUserID(int userID){
        findUser(userID);
        return serviceFriendship.findRequestByUserID(userID);
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

    public int sizeMessages(){
        return serviceMessage.size();
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
