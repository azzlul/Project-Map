package javafx;

import Domain.User;
import Exceptions.RepositoryException;
import Exceptions.ServiceException;
import Service.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddScreenController {

    public TextField requestField;
    public ServiceUser srv;
    public User user;
    public Label errorLabel;

    public void setSrv(ServiceUser srv) {
        this.srv = srv;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void sendFriendRequest(ActionEvent actionEvent) {
        try {
            var user2 = srv.findUserByName(requestField.getText()).get();
            srv.addFriendRequest(user.getId(), user2.getId());
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/friendRequestConfirm.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/friendRequestConfirm.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            Stage stage2 = (Stage) requestField.getScene().getWindow();
            stage2.close();
        }
        catch (ServiceException | RepositoryException e) {
            errorLabel.setText(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
