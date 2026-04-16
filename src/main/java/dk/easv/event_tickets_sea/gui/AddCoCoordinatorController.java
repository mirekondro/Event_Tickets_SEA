package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.model.User;
import dk.easv.event_tickets_sea.util.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddCoCoordinatorController {

    @FXML private Label eventNameLabel;
    @FXML private Label primaryCoordinatorLabel;
    @FXML private ListView<String> coCoordinatorsList;
    @FXML private ComboBox<String> coordinatorComboBox;
    @FXML private Button addButton;
    @FXML private Button removeButton;

    private Event event;

    public void setEvent(Event event) {
        this.event = event;
        loadEventInfo();
        loadCoordinators();
        loadCoCoordinatorsList();
        setupListeners();
    }

    private void loadEventInfo() {
        if (event != null) {
            eventNameLabel.setText(event.getEventName());
            primaryCoordinatorLabel.setText(event.getCoordinator());
        }
    }

    private void loadCoordinators() {
        // Clear existing items
        coordinatorComboBox.getItems().clear();

        // Add all coordinators from UserManager (except primary coordinator and already added co-coordinators)
        for (User user : UserManager.getInstance().getUsers()) {
            // Only add Event Coordinators (not Admins)
            if ("coordinator".equals(user.getRole()) || "admin".equals(user.getRole())) {
                String coordinatorName = user.getFullName();
                // Don't add if it's the primary coordinator or already a co-coordinator
                if (!coordinatorName.equals(event.getCoordinator()) &&
                        !event.getCoCoordinators().contains(coordinatorName)) {
                    coordinatorComboBox.getItems().add(coordinatorName);
                }
            }
        }
    }

    private void loadCoCoordinatorsList() {
        if (event != null) {
            coCoordinatorsList.setItems(event.getCoCoordinators());
        }
    }

    private void setupListeners() {
        // Enable/disable remove button based on selection
        coCoordinatorsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            removeButton.setDisable(newVal == null);
        });
    }

    @FXML
    private void handleAdd() {
        // Validate selection
        if (coordinatorComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "No Selection",
                    "Please select a coordinator to add.");
            return;
        }

        String selectedCoordinator = coordinatorComboBox.getValue();

        // Add co-coordinator to event
        if (event != null) {
            event.addCoCoordinator(selectedCoordinator);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Co-Coordinator Added",
                    selectedCoordinator + " has been added as a co-coordinator.");

            // Refresh the combo box (remove the added coordinator)
            loadCoordinators();
        }
    }

    @FXML
    private void handleRemove() {
        String selectedCoordinator = coCoordinatorsList.getSelectionModel().getSelectedItem();

        if (selectedCoordinator != null && event != null) {
            event.removeCoCoordinator(selectedCoordinator);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Co-Coordinator Removed",
                    selectedCoordinator + " has been removed from this event.");

            // Refresh the combo box (add back the removed coordinator)
            loadCoordinators();
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
