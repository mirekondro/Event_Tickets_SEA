package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import dk.easv.event_tickets_sea.model.User;
import dk.easv.event_tickets_sea.util.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error",
                    "Missing Username",
                    "Please enter your username.");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error",
                    "Missing Password",
                    "Please enter your password.");
            return;
        }

        // Try to login using database
        boolean loginSuccess = UserManager.getInstance().login(username.trim(), password.trim());

        if (loginSuccess) {
            User user = UserManager.getInstance().getLoggedInUser();

            // Determine which dashboard to open based on role
            String fxmlFile;
            if ("admin".equalsIgnoreCase(user.getRole())) {
                fxmlFile = "admin-dashboard.fxml";
            } else {
                fxmlFile = "coordinator-dashboard.fxml";
            }

            // Open appropriate dashboard
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load(), 1100, 720));
            stage.centerOnScreen();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed",
                    "Invalid Credentials",
                    "Username or password is incorrect. Please try again.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
