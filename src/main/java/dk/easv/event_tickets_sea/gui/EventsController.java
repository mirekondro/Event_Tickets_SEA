package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class EventsController {
    @FXML
    private Button loginBtn;

    @FXML
    private void handleGoToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 820, 520);
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        stage.setTitle("Event Tickets SEA");
        stage.setScene(scene);
    }

    @FXML
    private void handleBuyTicket() {
        // TODO: implement purchase flow
    }

    @FXML
    private void handleViewDetails() {
        // TODO: implement details flow
    }
}

