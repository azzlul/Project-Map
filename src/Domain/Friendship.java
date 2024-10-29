package Domain;

/**
 * Class that represents the friendship status of two Users.
 */
public class Friendship extends Entity<Integer>{
    /**
     * ID of first friend.
     */
    int firstUserID;
    /**
     * ID of second friend.
     */
    int secondUserID;

    /**
     * Constructor for friendship.
     * @param firstUserID ID of first user
     * @param secondUserID ID of second user
     */
    public Friendship(int firstUserID, int secondUserID) {
        this.firstUserID = firstUserID;
        this.secondUserID = secondUserID;
    }

    /**
     * Returns the ID of the first user.
     * @return integer
     */
    public int getFirstUserID() {
        return firstUserID;
    }

    /**
     * Sets the ID of the first user.
     * @param firstUserID integer
     */
    public void setFirstUserID(int firstUserID) {
        this.firstUserID = firstUserID;
    }

    /**
     * Returns the ID of the second user.
     * @return integer
     */
    public int getSecondUserID() {
        return secondUserID;
    }

    /**
     * Sets the ID of the second user.
     * @param secondUserID integer
     */
    public void setSecondUserID(int secondUserID) {
        this.secondUserID = secondUserID;
    }

    @Override
    public String toString() {
        return "Friendship{" + "firstUserID=" + firstUserID + ", secondUserID=";
    }
}
