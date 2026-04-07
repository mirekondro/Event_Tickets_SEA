package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.util.EventManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventFormController {

    @FXML private TextField eventNameField;
    @FXML private DatePicker startDatePicker;
    @FXML private TextField startTimeField;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField endTimeField;
    @FXML private TextField locationField;
    @FXML private TextArea locationGuidanceArea;
    @FXML private TextArea notesArea;

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSaveEvent(ActionEvent event) {
        // Validate required fields
        if (!validateFields()) {
            return;
        }

        try {
            // Get values
            String eventName = eventNameField.getText().trim();
            LocalDate startDate = startDatePicker.getValue();
            LocalTime startTime = parseTime(startTimeField.getText().trim());
            LocalDate endDate = endDatePicker.getValue();
            LocalTime endTime = endTimeField.getText().trim().isEmpty() ? null : parseTime(endTimeField.getText().trim());
            String location = locationField.getText().trim();
            String locationGuidance = locationGuidanceArea.getText().trim();
            String notes = notesArea.getText().trim();

            // Create new event and add to EventManager
            Event newEvent = new Event(
                    eventName,
                    startDate,
                    startTime,
                    endDate,
                    endTime,
                    location,
                    locationGuidance,
                    notes,
                    "Current User" // TODO: Get from logged-in user
            );

            EventManager.getInstance().addEvent(newEvent);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Event Created",
                    "Event '" + eventName + "' has been created successfully!");

            // Close the window
            handleClose(event);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Invalid Input",
                    "Please check your time format (HH:MM)");
        }
    }

    private boolean validateFields() {
        if (eventNameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Missing Information", "Please enter the event name.");
            return false;
        }

        if (startDatePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Missing Information", "Please select a start date.");
            return false;
        }

        if (startTimeField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Missing Information", "Please enter a start time.");
            return false;
        }

        if (locationField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Missing Information", "Please enter the location.");
            return false;
        }

        if (notesArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Missing Information", "Please enter event notes.");
            return false;
        }

        return true;
    }

    private LocalTime parseTime(String timeStr) {
        // Parse time in format HH:MM
        String[] parts = timeStr.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid time format");
        }
        int hour = Integer.parseInt(parts[0].trim());
        int minute = Integer.parseInt(parts[1].trim());
        return LocalTime.of(hour, minute);
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
