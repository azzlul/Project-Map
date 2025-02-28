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
    String loginScene = "fxml/loginScreen.fxml";
    String loginCSS = "css/loginScreen.css";
    String friendsListScene = "fxml/friendListScreen.fxml";
    String friendsListCSS = "css/friendList.css";
    public SceneSwitcher(Stage stage, ServiceUser srv) {
        this.stage = stage;
        this.srv = srv;
    }

    void switchToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(loginScene));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getClassLoader().getResource(loginCSS).toExternalForm());
        LoginController loginController = loader.getController();
        loginController.setSrv(srv);
        loginController.setSceneSwitcher(this);
        stage.setTitle("OtherFace");
        stage.setScene(scene);
    }
    void switchToFriendList(User user) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(friendsListScene));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getClassLoader().getResource(friendsListCSS).toExternalForm());
        FriendListController friendListController = loader.getController();
        friendListController.setSrv(srv);
        friendListController.setActiveUser(user);
        friendListController.setSceneSwitcher(this);
        stage.setTitle("Logged in as: " + user.getName());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
