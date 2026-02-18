package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class CoordinatorController {
    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 820, 520));
        stage.centerOnScreen();
    }

    @FXML private void handleCreateEvent() throws IOException { openModal("event-form.fxml", "Create New Event"); }
    @FXML private void handleManageTickets() throws IOException { openModal("sell-ticket.fxml", "Sell / Print Tickets"); }
    @FXML private void handleSpecialVouchers() throws IOException { openModal("voucher-print-view.fxml", "Special Voucher"); }

    private void openModal(String fxmlFile, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML private void handleDeleteEvent() {}
    @FXML private void handleViewAttendees() {}
}