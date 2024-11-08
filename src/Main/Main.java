package Main;

import Repository.Database.FriendshipDbRepo;
import Repository.Database.UserDbRepo;
import Service.ServiceUser;
import UI.UI;
import java.sql.*;
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
        Connection c;
        try{
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "321123");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return;
        }
        var repoUser = new UserDbRepo(c);
        var repoFriendship = new FriendshipDbRepo(c);
        var srvUser = new ServiceUser(repoUser, repoFriendship);
        var ui = new UI(srvUser);
        ui.runUI();
    }
}