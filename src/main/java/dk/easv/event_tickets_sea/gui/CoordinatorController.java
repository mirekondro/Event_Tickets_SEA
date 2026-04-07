package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.util.EventManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class CoordinatorController {

    @FXML private TableView<Event> eventsTable;
    @FXML private TableColumn<Event, String> colEventName;
    @FXML private TableColumn<Event, String> colLocation;
    @FXML private TableColumn<Event, String> colStartDate;
    @FXML private TableColumn<Event, String> colEndDate;
    @FXML private TableColumn<Event, String> colNotes;

    @FXML
    public void initialize() {
        // Setup table columns
        colEventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDateTimeFormatted"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDateTimeFormatted"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));

        // Load events into table
        eventsTable.setItems(EventManager.getInstance().getEvents());
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 820, 520));
        stage.centerOnScreen();
    }

    @FXML
    private void handleCreateEvent() throws IOException {
        openModal("event-form.fxml", "Create New Event");
    }

    @FXML
    private void handleManageTickets() throws IOException {
        openModal("sell-ticket.fxml", "Sell / Print Tickets");
    }

    @FXML
    private void handleSpecialVouchers() throws IOException {
        openModal("voucher-print-view.fxml", "Special Voucher");
    }

    private void openModal(String fxmlFile, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait(); // Changed to showAndWait so table refreshes after closing
    }

    @FXML
    private void handleDeleteEvent() {
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            EventManager.getInstance().removeEvent(selectedEvent);
        }
    }

    @FXML
    private void handleViewAttendees() {
        // TODO: Implement view attendees
    }
}
