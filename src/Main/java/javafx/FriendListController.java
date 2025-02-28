package javafx;

import Domain.Friendship;
import Domain.Message;
import Domain.User;
import Service.ServiceUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.Observer;
import utils.Pageable;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class FriendListController implements Initializable, Observer {
    @FXML
    public Button friendRequestButton;

    @FXML
    public ScrollPane chatBackground;

    @FXML
    public VBox chatWindow;

    @FXML
    public TextField chatInput;
    public Label replyLabel;
    public Button closeReplyButton;
    public VBox replyBackground;
    public Label chatLabel;
    public Label forwardLabel;
    public Button previousButton;
    public Label pageLabel;
    public Button nextButton;

    private ArrayList<Integer> forwardList = new ArrayList<Integer>();
    private ServiceUser srv;
    private User activeUser;
    private SceneSwitcher sceneSwitcher;
    private int activeChatID = 0;
    private int pageSize = 7;
    private int currentPage = 0;
    private int size = 0;

    @FXML
    private ListView<HBox> friendsList;

    @FXML
    private Button addFriendButton;

    @FXML
    private Button backButton;

    private int replyID;
    void setSrv(ServiceUser serviceUser) {
        srv = serviceUser;
        srv.addObserver(this);
    }
     void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
         updateFriendList();
         refreshNotifButton();
     }
     void setSceneSwitcher(SceneSwitcher sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
     }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatWindow.heightProperty().addListener(observable -> chatBackground.setVvalue(1D));
        previousButton.setDisable(true);
    }
    void refreshNotifButton(){
        ImageView image;
        if(!srv.findRequestByUserID(activeUser.getId()).iterator().hasNext()) image = new ImageView("images/friendRequestButton.png");
        else image = new ImageView("images/friendRequestButtonNotif.png");
        image.setFitHeight(30);
        image.setFitWidth(30);
        friendRequestButton.setGraphic(image);
    }
    void updateFriendList(){
        friendsList.getItems().clear();
        ObservableList<HBox> friends = FXCollections.observableArrayList();
        var page = srv.findALlFriendshipsByUserIDPaged(activeUser.getId(), new Pageable(currentPage, pageSize));
        size = page.getTotalNumberOfElements();
        if(size == 0){
            pageLabel.setText("Page 0 of 0");
            previousButton.setDisable(true);
            nextButton.setDisable(true);
            return;
        }
        pageLabel.setText("Page " + (currentPage+1) + " of " + (int)(Math.ceil((double) page.getTotalNumberOfElements() /pageSize)));
        for(var friendship : page.getElementsOnPage()) {
            if(friendship.getFirstUserID() == activeUser.getId()){
                    createButtonFriends(srv.findUser(friendship.getSecondUserID()).toString(), friends, friendship, friendship.getSecondUserID());
            }
            if(friendship.getSecondUserID() == activeUser.getId()){
                    createButtonFriends(srv.findUser(friendship.getFirstUserID()).toString(), friends, friendship, friendship.getFirstUserID());
            }
        }
        if(currentPage == size/pageSize) nextButton.setDisable(true);
        friendsList.setItems(friends);
    }

    private void createButtonFriends(String labelText, ObservableList<HBox> friends, Friendship friendship, int friendId) {
        Label label = new Label(labelText);
        Button button = new Button("Delete");
        Button button2 = new Button("Chat");
        label.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(label, Priority.ALWAYS);
        label.setStyle("-fx-text-fill: #FFFFFF");
        button2.setOnAction(actionEvent -> loadMessages(friendId));
        button2.setVisible(false);
        button2.getStylesheets().add(getClass().getClassLoader().getResource("css/friendListButton.css").toExternalForm());
        button.setOnAction(actionEvent -> srv.removeFriendship(friendship.getId()));
        button.setVisible(false);
        button.getStylesheets().add(getClass().getClassLoader().getResource("css/friendListButton.css").toExternalForm());
        HBox hbox = new HBox();
        Button button3  = new Button("Forward");
        button3.setVisible(false);
        button3.getStylesheets().add(getClass().getClassLoader().getResource("css/friendListButton.css").toExternalForm());
        button3.setOnAction(actionEvent -> addForward(friendId));
        hbox.getChildren().addAll(label, button3, button2, button);
        hbox.setUserData(friendId);
        friends.add(hbox);
    }


    public void openFriendRequestWindow(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/addScreen.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css/addScreen.css").toExternalForm());
        AddScreenController controller = loader.getController();
        controller.setSrv(srv);
        controller.setUser(activeUser);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Send friend request");
        stage.show();
    }

    public void makeDeleteButtonVisible() {
        if(!friendsList.getSelectionModel().isEmpty()) {
            friendsList.getItems().forEach(hBox -> {hBox.getChildren().get(1).setVisible(false);
                hBox.getChildren().get(2).setVisible(false);
                hBox.getChildren().get(3).setVisible(false);
            });
            friendsList.getSelectionModel().getSelectedItem().getChildren().get(2).setVisible(true);
            friendsList.getSelectionModel().getSelectedItem().getChildren().get(3).setVisible(true);
            if(activeChatID != (int)friendsList.getSelectionModel().getSelectedItem().getUserData() && activeChatID != 0)
                friendsList.getSelectionModel().getSelectedItem().getChildren().get(1).setVisible(true);
        }
    }

    @Override
    public void update() {
        updateFriendList();
        refreshNotifButton();
        if(activeChatID != 0)loadMessages(activeChatID);
    }

    public void switchLoginScreen(ActionEvent actionEvent) throws IOException {
        sceneSwitcher.switchToLogin();
    }

    public void openFriendRequestScreen(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/friendRequestScreen.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css/friendRequestScreen.css").toExternalForm());
        FriendRequestScreenController controller = loader.getController();
        controller.setSrv(srv);
        controller.setActiveUser(activeUser);
        stage.setScene(scene);
        scene.getWindow().addEventHandler(WindowEvent.WINDOW_HIDDEN, event -> srv.removeObserver(controller));
        stage.setResizable(false);
        stage.setTitle("Friend Request");
        stage.show();
    }

    void setReplyID(int replyID) {
        this.replyID = replyID;
        setReplyMessage();
    }
    void loadForwardLabel(){
        if(!forwardList.isEmpty())forwardLabel.setText("Forward to: ");
        else forwardLabel.setText("");
        forwardList.forEach(id -> forwardLabel.setText(forwardLabel.getText() + srv.findUser(id).getName() + " "));
    }
    void addForward(int forwardID){
        if(forwardList.contains(forwardID))return;
        forwardList.add(forwardID);
        loadForwardLabel();
    }

    void clearForward(){
        forwardList.clear();
        loadForwardLabel();
    }
    VBox createReplyMessage(int replyID){
        var message = srv.findMessage(replyID);
        Label label = new Label();
        VBox vbox = new VBox();
        label.setText("Reply to:\n" + message.getMessage());
        if(Objects.equals(message.getFromID(), activeUser.getId())) {
            label.getStylesheets().add(getClass().getClassLoader().getResource("css/chatLabelUser.css").toExternalForm());
            vbox.setBackground(new Background(new BackgroundFill(Paint.valueOf("#6068DC"), new CornerRadii(10), Insets.EMPTY)));
        }
        else {
            vbox.setBackground(new Background(new BackgroundFill(Paint.valueOf("#F0F0F0"), new CornerRadii(10), Insets.EMPTY)));
            label.getStylesheets().add(getClass().getClassLoader().getResource("css/chatLabelFriend.css").toExternalForm());
        }
        vbox.setPadding(new Insets(2, 10, 2, 10));
        vbox.getChildren().add(label);
        return vbox;
    }

    public void addMessage(int messageId, String message, boolean isUser, int replyID) {
        HBox hbox = new HBox();
        Label label = new Label(message);
        VBox vbox = new VBox();
        Button replyButton = new Button();
        ImageView replyIcon = new ImageView("images/replyIcon.png");
        replyButton.setGraphic(replyIcon);
        replyButton.setVisible(false);
        replyButton.setStyle("-fx-background-color: transparent;");
        hbox.setPrefWidth(435);
        hbox.setSpacing(10);
        replyButton.setOnAction(actionEvent -> setReplyID(messageId));
        if(isUser){
            hbox.setAlignment(Pos.CENTER_RIGHT);
            vbox.setBackground(new Background(new BackgroundFill(Paint.valueOf("#777ee1"), new CornerRadii(10), Insets.EMPTY)));
            label.getStylesheets().add(getClass().getClassLoader().getResource("css/chatLabelUser.css").toExternalForm());
            if(replyID == 0)vbox.getChildren().addAll(label);
            else vbox.getChildren().addAll(createReplyMessage(replyID), label);
            hbox.getChildren().addAll(replyButton, vbox);
            hbox.setOnMouseEntered(event -> {hbox.getChildren().get(0).setVisible(true);});
            hbox.setOnMouseExited(event -> {hbox.getChildren().get(0).setVisible(false);});
        }
        else {
            hbox.setAlignment(Pos.CENTER_LEFT);
            vbox.setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), new CornerRadii(10), Insets.EMPTY)));
            label.getStylesheets().add(getClass().getClassLoader().getResource("css/chatLabelFriend.css").toExternalForm());
            if(replyID == 0)vbox.getChildren().addAll(label);
            else vbox.getChildren().addAll(createReplyMessage(replyID), label);
            hbox.getChildren().addAll(vbox, replyButton);
            hbox.setOnMouseEntered(event -> {hbox.getChildren().get(1).setVisible(true);});
            hbox.setOnMouseExited(event -> {hbox.getChildren().get(1).setVisible(false);});
        }
        vbox.setPadding(new Insets(5, 10, 5, 10));
        label.setWrapText(true);
        label.setAlignment(Pos.TOP_LEFT);
        label.setMaxWidth(300);
        chatWindow.getChildren().addAll(hbox);
    }

    void loadMessages(int friendID){
        clearForward();
        chatLabel.setText("Chatting with " + srv.findUser(friendID).getName());
        activeChatID = friendID;
        makeDeleteButtonVisible();
        chatWindow.getChildren().clear();
        var rez = srv.findMessagesFromUsers(activeUser.getId(), friendID);
        rez.forEach(message -> addMessage(message.getId(), message.getMessage(), Objects.equals(message.getFromID(), activeUser.getId()), message.getReplyID()));
    }

    public void createMessage(ActionEvent actionEvent) {
        if(activeChatID > 0 && !chatInput.getText().isEmpty()){
            try {
                srv.addMessage(activeUser.getId(), activeChatID, chatInput.getText(), LocalDateTime.now(), replyID);
                forwardList.forEach(id -> srv.addMessage(activeUser.getId(), id, chatInput.getText(), LocalDateTime.now(), 0));
            }
            catch (Exception e) {
                chatInput.setText(e.getMessage());
                return;
            }
            update();
        }
        clearForward();
        chatInput.setText("");
        setReplyID(0);
    }

    public void setReplyMessage(){
        if(replyID == 0){
            replyLabel.setText("");
            replyLabel.setVisible(false);
            replyBackground.setVisible(false);
            closeReplyButton.setVisible(false);
        }
        else{
            replyLabel.getStylesheets().clear();
            replyLabel.setVisible(true);
            closeReplyButton.setVisible(true);
            replyBackground.setVisible(true);
            var message = srv.findMessage(replyID);
            System.out.println(message.getMessage());
            replyLabel.setText("Reply to:\n" + message.getMessage());
            if(Objects.equals(message.getFromID(), activeUser.getId())) {
                replyLabel.getStylesheets().add(getClass().getClassLoader().getResource("css/chatLabelUser.css").toExternalForm());
                replyBackground.setBackground(new Background(new BackgroundFill(Paint.valueOf("#777ee1"), new CornerRadii(10), Insets.EMPTY)));
            }
            else {
                replyBackground.setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), new CornerRadii(10), Insets.EMPTY)));
                replyLabel.getStylesheets().add(getClass().getClassLoader().getResource("css/chatLabelFriend.css").toExternalForm());
            }
            replyBackground.setPadding(new Insets(2, 10, 2, 10));
            replyBackground.setStyle("-fx-opacity: 0.75");
        }
    }

    public void closeReplyScreen(ActionEvent actionEvent) {
        setReplyID(0);
    }

    public void goToPreviousPage(ActionEvent actionEvent) {
        currentPage--;
        updateFriendList();
        if(currentPage == 0) previousButton.setDisable(true);
        nextButton.setDisable(false);
    }

    public void goToNextPage(ActionEvent actionEvent) {
        currentPage++;
        updateFriendList();
        if(currentPage == size/pageSize) nextButton.setDisable(true);
        previousButton.setDisable(false);
    }
}
