package Domain;

public class User {
    int ID;
    String name;
    int[] friendsID;

    public User(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getFriendsID() {
        return friendsID;
    }

    public void setFriendsID(int[] friendsID) {
        this.friendsID = friendsID;
    }

}
