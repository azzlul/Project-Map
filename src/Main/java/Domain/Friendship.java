package Domain;
import java.time.LocalDateTime;

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
     * Date when the friend request was sent
     */
    LocalDateTime friendsFrom;
    /**
     * True if the friend request was accepted
     */
    boolean accepted;
    /**
     * Constructor for friendship.
     * @param firstUserID ID of first user
     * @param secondUserID ID of second user
     */
    public Friendship(int firstUserID, int secondUserID, LocalDateTime friendsFrom, boolean accepted) {
        this.firstUserID = firstUserID;
        this.secondUserID = secondUserID;
        this.friendsFrom = friendsFrom;
        this.accepted = accepted;
    }
    public Friendship(int friendshipID, int firstUserID, int secondUserID, LocalDateTime friendsFrom, boolean accepted) {
        setId(friendshipID);
        this.firstUserID = firstUserID;
        this.secondUserID = secondUserID;
        this.friendsFrom = friendsFrom;
        this.accepted = accepted;
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

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public String toString() {
        return "Friendship{" + "firstUserID=" + firstUserID + ", secondUserID=";
    }
}
