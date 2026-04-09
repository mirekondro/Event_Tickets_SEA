package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import dk.easv.event_tickets_sea.model.User;
import dk.easv.event_tickets_sea.util.UserManager;
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
        String inputUsername = usernameField.getText();

        if (inputUsername == null || inputUsername.trim().isEmpty()) {
            return;
        }

        inputUsername = inputUsername.toLowerCase().trim();

        // Mapuj input na správné username v DB
        // Pokud zadá "admin" -> jdi na admin page a nastav username "admin"
        // Jinak -> jdi na coordinator page a nastav username "coordinator"
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

        // Vytvoř uživatele z DB dat a ulož ho do UserManager
        User user = new User(dbUsername, role, dbUsername + "@easv.dk", dbUsername.toUpperCase());
        UserManager.getInstance().setLoggedInUser(user);


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 1100, 720));
        stage.centerOnScreen();
    }
}