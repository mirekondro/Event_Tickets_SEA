package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.util.EventManager;
import dk.easv.event_tickets_sea.util.UserManager;
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

            // OPTIONAL: End Date and End Time - can be NULL!
            LocalDate endDate = endDatePicker.getValue(); // NULL if not selected
            LocalTime endTime = null;

            // Parse End Time only if provided
            if (!endTimeField.getText().trim().isEmpty()) {
                endTime = parseTime(endTimeField.getText().trim());
            }

            String location = locationField.getText().trim();
            String locationGuidance = locationGuidanceArea.getText().trim();
            String notes = notesArea.getText().trim();

            // Get coordinator from logged-in user
            String coordinatorUsername = UserManager.getInstance().getLoggedInUser().getUsername();

            System.out.println("Creating event:");
            System.out.println("  Start: " + startDate + " " + startTime);
            System.out.println("  End: " + (endDate != null ? endDate : "NULL") + " " + (endTime != null ? endTime : "NULL"));

            // Add event to database
            boolean success = EventManager.getInstance().addEvent(
                    eventName,
                    startDate,
                    startTime,
                    endDate,      // Can be NULL
                    endTime,      // Can be NULL
                    location,
                    locationGuidance,
                    notes,
                    coordinatorUsername
            );

            if (success) {
                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Event Created",
                        "Event '" + eventName + "' has been created successfully!");

                // Close the window
                handleClose(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Database Error",
                        "Failed to create event in database. Check console for details.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Invalid Input",
                    "Error: " + e.getMessage());
            e.printStackTrace();
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
                    "Missing Information", "Please enter a start time (e.g., 18:00).");
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

        // Validate start time format
        try {
            parseTime(startTimeField.getText().trim());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Invalid Time Format",
                    "Start time must be in format HH:MM (e.g., 18:00).");
            return false;
        }

        // Validate end time format (only if provided)
        if (!endTimeField.getText().trim().isEmpty()) {
            try {
                parseTime(endTimeField.getText().trim());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Validation Error",
                        "Invalid Time Format",
                        "End time must be in format HH:MM (e.g., 22:00).");
                return false;
            }
        }

        return true;
    }

    private LocalTime parseTime(String timeStr) {
        // Parse time in format HH:MM
        String[] parts = timeStr.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid time format. Use HH:MM (e.g., 18:00)");
        }
        int hour = Integer.parseInt(parts[0].trim());
        int minute = Integer.parseInt(parts[1].trim());

        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be between 0 and 59");
        }

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
