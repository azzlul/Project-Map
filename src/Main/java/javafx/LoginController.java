package javafx;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import utils.Observer;

public class LoginController implements Initializable {

    private ServiceUser srv;

    private  SceneSwitcher sceneSwitcher;
    @FXML
    private Label errorLabel;

    @FXML
    private ImageView startImage;

    @FXML
    private TextField loginText;

    void setSrv(ServiceUser serviceUser) {
        srv = serviceUser;
    }
    void setSceneSwitcher(SceneSwitcher sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void attemptLogin(ActionEvent actionEvent) throws IOException {
        var user = srv.findUserByName(loginText.getText());
        if (user.isPresent()) {
            sceneSwitcher.switchToFriendList(user.get());
            loginText.clear();
        }
        else{
            errorLabel.setText("User not found");
        }
    }

    public void attemptSignup(ActionEvent actionEvent) throws IOException {
        try{
            srv.addUser(loginText.getText());
            var user = srv.findUserByName(loginText.getText());
            sceneSwitcher.switchToFriendList(user.get());
            loginText.clear();
        }
        catch(Exception e){
            errorLabel.setText(e.getMessage());
        }
    }
}