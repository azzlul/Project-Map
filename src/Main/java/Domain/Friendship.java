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
    int firstMessageID;
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
    public Friendship(int friendshipID, int firstUserID, int secondUserID, LocalDateTime friendsFrom, int firstMessageID, boolean accepted) {
        setId(friendshipID);
        this.firstUserID = firstUserID;
        this.secondUserID = secondUserID;
        this.friendsFrom = friendsFrom;
        this.firstMessageID = firstMessageID;
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

    public int getFirstMessageID() {
        return firstMessageID;
    }

    public void setFirstMessageID(int firstMessageID) {
        this.firstMessageID = firstMessageID;
    }

    @Override
    public String toString() {
        return "Friendship{" + "firstUserID=" + firstUserID + ", secondUserID=";
    }
}
