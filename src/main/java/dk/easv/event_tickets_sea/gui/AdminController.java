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
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        // Setup table columns
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        colEventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        colCoordinator.setCellValueFactory(new PropertyValueFactory<>("coordinator"));

        reloadUsersTable();
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
    private void handleAddUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-user-form.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Add New User");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        // Re-fetch users from DB so newly created records appear immediately.
        reloadUsersTable();
    }

    @FXML
    private void handleAssignCoordinator() {
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Event Selected", "Please select an event first.");
            return;
        }

        List<String> coordinatorUsernames = UserManager.getInstance().getCoordinators().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        if (coordinatorUsernames.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Coordinators", "No Coordinator Users Found", "Create at least one coordinator user first.");
            return;
        }

        String currentSelection = coordinatorUsernames.get(0);
        ChoiceDialog<String> dialog = new ChoiceDialog<>(currentSelection, coordinatorUsernames);
        dialog.setTitle("Assign Coordinator");
        dialog.setHeaderText("Assign coordinator for: " + selectedEvent.getEventName());
        dialog.setContentText("Coordinator username:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            boolean success = EventManager.getInstance().assignCoordinator(selectedEvent.getEventName(), result.get());
            if (success) {
                reloadEventsTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Could not assign coordinator", "Please check if selected coordinator exists and is active.");
            }
        }
    }

    @FXML
    private void handleDeleteEvent() {
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Event Selected", "Please select an event first.");
            return;
        }

        EventManager.getInstance().removeEvent(selectedEvent);
        reloadEventsTable();
    }

    private void reloadUsersTable() {
        usersTable.setItems(UserManager.getInstance().getUsers());
        usersTable.refresh();
    }

    private void reloadEventsTable() {
        eventsTable.setItems(EventManager.getInstance().getEvents());
        eventsTable.refresh();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
