package Main;

import Repository.FriendshipFileRepo;
import Repository.UserFileRepo;
import Service.ServiceUser;
import UI.UI;

/**
 * Main class.
 */
public class Main {
    /**
     * Constructor for class.
     */
    public Main() {
    }
    /**
     * Main function.
     * @param args arguments
     */
    public static void main(String[] args) {
        var repoUser = new UserFileRepo("src/Files/users");
        var repoFriendship = new FriendshipFileRepo("src/Files/friendships");
        var srvUser = new ServiceUser(repoUser, repoFriendship);
        var ui = new UI(srvUser);
        ui.runUI();
    }
}