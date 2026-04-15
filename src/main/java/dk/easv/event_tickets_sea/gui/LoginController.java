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
        String username = usernameField.getText().trim();
        String password = getPassword();

        if (username.isEmpty() || password.isEmpty()) return;

        boolean success = UserManager.getInstance().login(username, password);

        if (!success) {
            showAlert(Alert.AlertType.ERROR, "Login Failed",
                    "Invalid credentials", "Username or password is incorrect.");
            return;
        }

        User user = UserManager.getInstance().getLoggedInUser();
        String fxmlFile = user.getRole().equals("admin")
                ? "admin-dashboard.fxml"
                : "coordinator-dashboard.fxml";

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 1100, 720));
        stage.centerOnScreen();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
