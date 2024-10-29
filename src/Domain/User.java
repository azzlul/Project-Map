package Domain;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Class that represents a user on the platform.
 */
public class User extends Entity<Integer>{
    /**
     * Username
     */
    String name;
    /**
     * List of friends, represented by ID.
     */
    ArrayList<Integer> friendsID;

    /**
     * Constructor for class where the friends list is null.
     * @param name username
     */
    public User(String name) {
        this.name = name;
        this.friendsID = new ArrayList<>();
    }

    /**
     * Constructor for class where the friends list is given.
     * @param name username
     * @param friendsID ArrayList with all the user's friendID's
     */
    public User(String name, ArrayList<Integer> friendsID) {
        this.name = name;
        this.friendsID = friendsID;
    }

    /**
     * Returns the username.
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Set the username.
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the friends list.
     * @return ArrayList with all friend ID's
     */
    public ArrayList<Integer> getFriendsID() {
        return friendsID;
    }

    /**
     * Set the friends list.
     * @param friendsID ArrayList with all friend ID's
     */
    public void setFriendsID(ArrayList<Integer> friendsID) {
        this.friendsID = friendsID;
    }

    @Override
    public String toString() {
        return
                "Name:" + name + ' ' +
                "ID: " + getId();
    }
}
