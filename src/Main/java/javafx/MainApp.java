package javafx;

import Repository.Database.FriendshipDbRepo;
import Repository.Database.MessageDbRepo;
import Repository.Database.UserDbRepo;
import Service.ServiceUser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getClassLoader().getResource("fxml/loginScreen.fxml"));
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
        var repoMessage = new MessageDbRepo(c);
        var srv = new ServiceUser(repoUser, repoFriendship, repoMessage);
        stage.setResizable(false);
        stage.setTitle("OtherFace");
        SceneSwitcher sceneSwitcher = new SceneSwitcher(stage,srv);
        sceneSwitcher.switchToLogin();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}