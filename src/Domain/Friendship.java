package Domain;

public class Friendship {
    int friendshipID;
    int firstUserID;
    int secondUserID;

    public Friendship(int firstUserID, int secondUserID) {
        this.firstUserID = firstUserID;
        this.secondUserID = secondUserID;
    }

    public int getFriendshipID() {
        return friendshipID;
    }

    public int getFirstUserID() {
        return firstUserID;
    }

    public void setFirstUserID(int firstUserID) {
        this.firstUserID = firstUserID;
    }

    public int getSecondUserID() {
        return secondUserID;
    }

    public void setSecondUserID(int secondUserID) {
        this.secondUserID = secondUserID;
    }
}
