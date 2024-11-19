package Domain;

/**
 * Class that represents a user on the platform.
 */
public class User extends Entity<Integer>{
    /**
     * Username
     */
    String name;
    /**
     * Constructor for class where the friends list is null.
     * @param name username
     */
    public User(String name) {
        this.name = name;
    }
    public User(int id, String name) {
        this.setId(id);
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
                name + ' ' +
                "ID: " + getId();
    }
}
