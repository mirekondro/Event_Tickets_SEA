package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import dk.easv.event_tickets_sea.model.User;
import dk.easv.event_tickets_sea.util.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisible;

    private boolean passwordShown = false;

    @FXML
    private void handleTogglePassword() {
        if (passwordShown) {
            // Hide password: copy text back to PasswordField and hide TextField
            passwordField.setText(passwordVisible.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordVisible.setVisible(false);
            passwordVisible.setManaged(false);
        } else {
            // Show password: copy text to plain TextField and show it
            passwordVisible.setText(passwordField.getText());
            passwordVisible.setVisible(true);
            passwordVisible.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
        }
        passwordShown = !passwordShown;
    }

    private String getPassword() {
        return passwordShown ? passwordVisible.getText() : passwordField.getText();
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String inputUsername = usernameField.getText();
        String inputPassword = getPassword();

        if (inputUsername == null || inputUsername.trim().isEmpty()) {
            return;
        }

        inputUsername = inputUsername.toLowerCase().trim();

        String dbUsername;
        String role;
        String fxmlFile;

        if (inputUsername.contains("admin")) {
            dbUsername = "admin";
            role = "admin";
            fxmlFile = "admin-dashboard.fxml";
        } else {
            dbUsername = "coordinator";
            role = "coordinator";
            fxmlFile = "coordinator-dashboard.fxml";
        }

        User user = new User(dbUsername, role, dbUsername + "@easv.dk", dbUsername.toUpperCase());
        UserManager.getInstance().setLoggedInUser(user);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 1100, 720));
        stage.centerOnScreen();
    }
}
