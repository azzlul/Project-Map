package javafx;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FriendRequestConfirmController {
    public Button button;

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
