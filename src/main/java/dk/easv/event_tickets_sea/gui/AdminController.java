package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.model.User;
import dk.easv.event_tickets_sea.util.EventManager;
import dk.easv.event_tickets_sea.util.UserManager;
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

public class AdminController {

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colEmail;

    @FXML private TableView<Event> eventsTable;
    @FXML private TableColumn<Event, String> colEventName;
    @FXML private TableColumn<Event, String> colCoordinator;

    @FXML
    public void initialize() {
        // Setup user table columns
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Load users into table
        usersTable.setItems(UserManager.getInstance().getUsers());

        // Setup events table columns
        colEventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        colCoordinator.setCellValueFactory(new PropertyValueFactory<>("coordinator"));

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
    private void handleAddUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-user-form.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Add New User");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait(); // Changed to showAndWait so table refreshes after closing
    }

    @FXML
    private void handleDeleteEvent() {
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            EventManager.getInstance().removeEvent(selectedEvent);
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Event Deleted",
                    "Event '" + selectedEvent.getEventName() + "' has been deleted.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "No Event Selected",
                    "Please select an event to delete.");
        }
    }

    @FXML
    private void handleAssignCoordinator() {
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            // TODO: Open dialog to assign coordinator
            showAlert(Alert.AlertType.INFORMATION, "Feature Coming Soon",
                    "Assign Coordinator",
                    "This feature will allow you to assign coordinators to events.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "No Event Selected",
                    "Please select an event to assign a coordinator.");
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
