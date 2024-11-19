package javafx;

import Domain.User;
import Service.ServiceUser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {
    Stage stage;
    ServiceUser srv;
    String loginScene;
    String friendsListScene;

    public SceneSwitcher(Stage stage, ServiceUser srv, String loginScene, String friendsListScene) {
        this.stage = stage;
        this.srv = srv;
        this.loginScene = loginScene;
        this.friendsListScene = friendsListScene;
    }

    void switchToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(loginScene));
        Scene scene = new Scene(loader.load());
        LoginController loginController = loader.getController();
        loginController.setSrv(srv);
        loginController.setSceneSwitcher(this);
        stage.setScene(scene);
    }
    void switchToFriendList(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(friendsListScene));
        Scene scene = new Scene(loader.load());
        FriendListController friendListController = loader.getController();
        friendListController.setSrv(srv);
        friendListController.setActiveUser(user);
        friendListController.setSceneSwitcher(this);
        stage.setScene(scene);
    }
}
