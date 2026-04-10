package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.util.EventManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

        reloadEventsTable();
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
        reloadEventsTable();
    }

    @FXML
    private void handleManageTickets() throws IOException {
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "No Event Selected",
                    "Please select an event before selling/printing tickets.");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sell-ticket.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Sell / Print Tickets");
        stage.initModality(Modality.APPLICATION_MODAL);

        SellTicketController controller = fxmlLoader.getController();
        controller.setEvent(selectedEvent);

        stage.showAndWait();
    }

    @FXML
    private void handleSpecialVouchers() throws IOException {
        openModal("voucher-print-view.fxml", "Special Voucher");
    }

    @FXML
    private void handleAssignCoCoordinator() throws IOException {
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            // Open add co-coordinator dialog
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-co-coordinator-form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Assign Co-Coordinator");
            stage.initModality(Modality.APPLICATION_MODAL);

            // Pass the selected event to the controller
            AddCoCoordinatorController controller = fxmlLoader.getController();
            controller.setEvent(selectedEvent);

            stage.showAndWait();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "No Event Selected",
                    "Please select an event to assign a co-coordinator.");
        }
    }

    private void openModal(String fxmlFile, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void handleDeleteEvent() {
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            EventManager.getInstance().removeEvent(selectedEvent);
            reloadEventsTable();
        }
    }

    private void reloadEventsTable() {
        eventsTable.setItems(EventManager.getInstance().getEvents());
        eventsTable.refresh();
    }

    @FXML
    private void handleViewAttendees() {
        // TODO: Implement view attendees
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
