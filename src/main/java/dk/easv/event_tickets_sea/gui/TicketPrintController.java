package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.model.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TicketPrintController {

    @FXML private Label categoryLabel;
    @FXML private Label eventNameLabel;
    @FXML private Label startDateTimeLabel;
    @FXML private Label endDateTimeLabel;
    @FXML private Label locationLabel;
    @FXML private Label guidanceLabel;
    @FXML private Label notesLabel;
    @FXML private Label customerNameLabel;
    @FXML private Label customerEmailLabel;
    @FXML private Label quantityLabel;
    @FXML private Label issuedByLabel;
    @FXML private Label ticketIdLabel;

    public void setTicketData(Event event,
                              String customerName,
                              String customerEmail,
                              String category,
                              int quantity,
                              String issuedBy,
                              String ticketId) {
        categoryLabel.setText((category == null ? "Regular" : category).toUpperCase() + " ADMISSION");
        eventNameLabel.setText(event.getEventName());
        startDateTimeLabel.setText(event.getStartDateTimeFormatted());
        endDateTimeLabel.setText(event.getEndDateTimeFormatted().isBlank() ? "N/A" : event.getEndDateTimeFormatted());
        locationLabel.setText(event.getLocation());
        guidanceLabel.setText("Guidance: " + (event.getLocationGuidance().isBlank() ? "N/A" : event.getLocationGuidance()));
        notesLabel.setText(event.getNotes().isBlank() ? "N/A" : event.getNotes());

        customerNameLabel.setText(customerName);
        customerEmailLabel.setText(customerEmail);
        quantityLabel.setText("Qty: " + quantity);
        issuedByLabel.setText("Issued by: " + issuedBy);
        ticketIdLabel.setText("ID: " + ticketId);
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

