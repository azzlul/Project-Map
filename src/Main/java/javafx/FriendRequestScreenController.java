package javafx;

import Domain.User;
import Service.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import utils.Observer;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class FriendRequestScreenController implements Observer, Initializable {

    @FXML
    private ListView<HBox> friendRequestList;
    @FXML
    private AnchorPane background;
    ServiceUser srv;
    User activeUser;


    public void setSrv(ServiceUser srv) {
        this.srv = srv;
        srv.addObserver(this);
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
        updateFriendRequestList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    void updateFriendRequestList(){
        friendRequestList.getItems().clear();
        ObservableList<HBox> friendRequests = FXCollections.observableArrayList();
        var list = srv.findRequestByUserID(activeUser.getId());
        if(!list.iterator().hasNext()) background.setStyle("-fx-background-image: url(images/requestListBackgroundEmpty.png)");
        else background.setStyle("-fx-background-image: url(images/requestListBackground.png)");
        for(var friendship : srv.findRequestByUserID(activeUser.getId())){
            Label label = new Label(srv.findUser(friendship.getFirstUserID()).getName() + " " + friendship.getFriendsFrom().format(DateTimeFormatter.ISO_DATE));
            label.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(label, Priority.ALWAYS);
            label.setStyle("-fx-text-fill: #535bc5");
            Button button = new Button("Accept");
            button.setOnAction(actionEvent -> {
                srv.acceptFriendRequest(friendship.getId());
            });
            button.setVisible(false);
            button.getStylesheets().add(getClass().getClassLoader().getResource("css/friendRequestListButton.css").toExternalForm());
            Button button2 = new Button("Reject");
            button2.setOnAction(actionEvent -> {
                srv.removeFriendship(friendship.getId());
            });
            button2.setVisible(false);
            button2.getStylesheets().add(getClass().getClassLoader().getResource("css/friendRequestListButton.css").toExternalForm());
            HBox hbox = new HBox();
            hbox.getChildren().addAll(label, button, button2);
            friendRequests.add(hbox);
        }
        friendRequestList.setItems(friendRequests);
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
        updateFriendRequestList();
    }

}
