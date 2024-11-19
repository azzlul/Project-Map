package javafx;

import Domain.User;
import Service.ServiceUser;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddScreen {

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
            Stage stage = (Stage) requestField.getScene().getWindow();
            stage.close();
        }
        catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }
}
