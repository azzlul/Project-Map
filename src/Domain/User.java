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

    /**
     * Constructor for class where the friends list is null.
     * @param name username
     */
    public User(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return
                "Name:" + name + ' ' +
                "ID: " + getId();
    }
}
