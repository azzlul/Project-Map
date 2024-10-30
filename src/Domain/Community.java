package Domain;
import java.util.ArrayList;

/**
 * Class that represents a community of users.
 * A community is a set of users who are all connected by a chain of friendships.
 */
public class Community {
    /**
     * List of all users in the community.
     */
    ArrayList<User> users;
    /**
     * Size of community.
     */
    int size;

    /**
     * Constructor for class where the list of users in empty.
     */
    public Community() {
        users = new ArrayList<>();
    }

    /**
     * Constructor for class where the list of users is given.
     * @param users ArrayList of users
     */
    public Community(ArrayList<User> users) {
        this.users = users;
        this.size = users.size();
    }

    /**
     * Returns the list of users.
     * @return ArrayList of users
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Sets the list of users
     * @param users ArrayList of users
     */
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    /**
     * Returns size of community.
     * @return integer
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the size of community.
     * @param size integer
     */
    public void setSize(int size) {
        this.size = size;
    }
    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("Size:").append(size).append("\n");
        for(var user: users){
            msg.append(user);
            msg.append("\n");
        }
        return msg.toString();
    }
}
