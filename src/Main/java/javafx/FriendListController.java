package javafx;

import Domain.Friendship;
import Domain.User;
import Service.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import utils.Observer;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class FriendListController implements Initializable, Observer {
    private ServiceUser srv;
    private User activeUser;
    private SceneSwitcher sceneSwitcher;
    @FXML
    private ListView<HBox> friendsList;

    @FXML
    private ListView<HBox> friendRequestList;

    @FXML
    private Button addFriendButton;

    @FXML
    private Button backButton;

    void setSrv(ServiceUser serviceUser) {
        srv = serviceUser;
        srv.addObserver(this);
    }
     void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
         updateFriendList();
         updateFriendRequestList();
     }
     void setSceneSwitcher(SceneSwitcher sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
     }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    void updateFriendList(){
        friendsList.getItems().clear();
        ObservableList<HBox> friends = FXCollections.observableArrayList();
        for(var friendship : srv.findAllFriendships()){
            if(friendship.isAccepted()){
                if(friendship.getFirstUserID() == activeUser.getId()){
                    createButtonFriends(srv.findUser(friendship.getSecondUserID()).toString(), friends, friendship);
                }
                if(friendship.getSecondUserID() == activeUser.getId()){
                    createButtonFriends(srv.findUser(friendship.getFirstUserID()).toString(), friends, friendship);
                }
            }
        }
        friendsList.setItems(friends);
    }

    private void createButtonFriends(String labelText, ObservableList<HBox> friends, Friendship friendship) {
        Label label = new Label(labelText);
        Button button = new Button("Delete");
        label.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(label, Priority.ALWAYS);
        button.setOnAction(actionEvent -> srv.removeFriendship(friendship.getId()));
        button.setVisible(false);
        HBox hbox = new HBox();
        hbox.getChildren().addAll(label, button);
        friends.add(hbox);
    }

    void updateFriendRequestList(){
        friendRequestList.getItems().clear();
        ObservableList<HBox> friendRequests = FXCollections.observableArrayList();
        for(var friendship : srv.findAllFriendships()){
            if(!friendship.isAccepted() && friendship.getSecondUserID() == activeUser.getId()){
                Label label = new Label(srv.findUser(friendship.getFirstUserID()).getName() + " " + friendship.getFriendsFrom().format(DateTimeFormatter.ISO_DATE));
                label.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(label, Priority.ALWAYS);
                Button button = new Button("Accept");
                button.setOnAction(actionEvent -> {
                    srv.acceptFriendRequest(friendship.getId());
                });
                button.setVisible(false);
                Button button2 = new Button("Reject");
                button2.setOnAction(actionEvent -> {
                    srv.removeFriendship(friendship.getId());
                });
                button2.setVisible(false);
                HBox hbox = new HBox();
                hbox.getChildren().addAll(label, button, button2);
                friendRequests.add(hbox);
            }
        }
        friendRequestList.setItems(friendRequests);
    }
    public void openFriendRequestWindow(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/addScreen.fxml"));
        Scene scene = new Scene(loader.load());
        AddScreenController controller = loader.getController();
        controller.setSrv(srv);
        controller.setUser(activeUser);
        stage.setScene(scene);
        stage.setTitle("Send friend request");
        stage.show();
    }

    public void makeDeleteButtonVisible(MouseEvent mouseEvent) {
        if(!friendsList.getSelectionModel().isEmpty()) {
            friendsList.getItems().forEach(hBox -> hBox.getChildren().get(1).setVisible(false));
            friendsList.getSelectionModel().getSelectedItem().getChildren().get(1).setVisible(true);
        }
    }

    public void makeAcceptRejectButtonsVisible(MouseEvent mouseEvent) {
        if(!friendRequestList.getSelectionModel().isEmpty()) {
            friendRequestList.getItems().forEach(hBox -> {
                hBox.getChildren().get(1).setVisible(false);
                hBox.getChildren().get(2).setVisible(false);
            });
            friendRequestList.getSelectionModel().getSelectedItem().getChildren().get(1).setVisible(true);
            friendRequestList.getSelectionModel().getSelectedItem().getChildren().get(2).setVisible(true);
        }
    }

    @Override
    public void update() {
        updateFriendList();
        updateFriendRequestList();
    }

    public void switchLoginScreen(ActionEvent actionEvent) throws IOException {
        sceneSwitcher.switchToLogin();
    }
}
