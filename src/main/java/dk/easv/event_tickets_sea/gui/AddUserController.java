package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.model.User;
import dk.easv.event_tickets_sea.util.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddUserController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Button createButton;

    @FXML
    public void initialize() {
        // Populate ComboBox with roles
        roleComboBox.getItems().addAll("coordinator", "admin");
        // Set default role
        roleComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCreateUser(ActionEvent event) {
        // Validate fields
        if (!validateFields()) {
            return;
        }

        // Get values
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        // Create new user in database
        boolean success = UserManager.getInstance().addUser(username, password, email, fullName, role);

        if (success) {
            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "User Created",
                    "User '" + fullName + "' has been created successfully!");

            // Close the window
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Database Error",
                    "Failed to create user. Username might already exist.");
        }
    }

    private boolean validateFields() {
        // Check if all fields are filled
        if (fullNameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Missing Information", "Please enter the full name.");
            return false;
        }

        if (emailField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Missing Information", "Please enter an email address.");
            return false;
        }

        // Validate email format
        if (!isValidEmail(emailField.getText().trim())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Invalid Email", "Please enter a valid email address.");
            return false;
        }

        if (usernameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Missing Information", "Please enter a username.");
            return false;
        }

        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Missing Information", "Please enter a password.");
            return false;
        }

        if (passwordField.getText().length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Weak Password", "Password must be at least 6 characters long.");
            return false;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Password Mismatch", "Passwords do not match.");
            return false;
        }

        if (roleComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Missing Information", "Please select a role.");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        // Simple email validation
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
