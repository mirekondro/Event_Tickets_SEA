package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText() != null ? usernameField.getText().toLowerCase() : "";
        String fxmlFile = username.contains("admin") ? "admin-dashboard.fxml" : "coordinator-dashboard.fxml";

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 1100, 720));
        stage.centerOnScreen();
    }
}