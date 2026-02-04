package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginBtn;

    @FXML
    private void handleLogin() throws IOException {
        // Minimal navigation to confirm wiring works.
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("coordinator-dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        stage.setTitle("Coordinator Dashboard");
        stage.setScene(scene);
    }
}

