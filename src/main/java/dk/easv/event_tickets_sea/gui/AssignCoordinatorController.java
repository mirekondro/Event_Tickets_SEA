package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.model.User;
import dk.easv.event_tickets_sea.util.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AssignCoordinatorController {

    @FXML private Label eventNameLabel;
    @FXML private Label currentCoordinatorLabel;
    @FXML private ComboBox<String> coordinatorComboBox;
    @FXML private Button assignButton;

    private Event event;

    public void setEvent(Event event) {
        this.event = event;
        loadEventInfo();
        loadCoordinators();
    }

    private void loadEventInfo() {
        if (event != null) {
            eventNameLabel.setText(event.getEventName());
            currentCoordinatorLabel.setText(event.getCoordinator());
        }
    }

    private void loadCoordinators() {
        // Clear existing items
        coordinatorComboBox.getItems().clear();

        // Add all coordinators from UserManager
        for (User user : UserManager.getInstance().getUsers()) {
            // Only add Event Coordinators (not Admins)
            if ("Event Coordinator".equals(user.getRole())) {
                coordinatorComboBox.getItems().add(user.getFullName());
            }
        }

        // Select current coordinator if exists
        if (event != null && event.getCoordinator() != null) {
            coordinatorComboBox.getSelectionModel().select(event.getCoordinator());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAssign(ActionEvent actionEvent) {
        // Validate selection
        if (coordinatorComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "No Selection",
                    "Please select a coordinator.");
            return;
        }

        String selectedCoordinator = coordinatorComboBox.getValue();

        // Update the event's coordinator
        if (event != null) {
            event.setCoordinator(selectedCoordinator);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Coordinator Assigned",
                    "Event '" + event.getEventName() + "' has been assigned to " + selectedCoordinator);

            // Close the window
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
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
