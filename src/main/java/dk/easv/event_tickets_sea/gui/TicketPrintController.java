package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.model.Category;
import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.util.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TicketPrintController {

    @FXML private Label categoryLabel;
    @FXML private Label eventNameLabel;
    @FXML private Label startLabel;
    @FXML private Label endLabel;
    @FXML private Label locationLabel;
    @FXML private Label guidanceLabel;
    @FXML private Label notesLabel;
    @FXML private Label customerNameLabel;
    @FXML private Label customerEmailLabel;
    @FXML private Label quantityLabel;
    @FXML private Label issuedByLabel;
    @FXML private Label ticketIdLabel;

    public void setTicketData(Event event, Category category, String customerName, String customerEmail, int quantity) {
        String issuedBy = UserManager.getInstance().getLoggedInUser() != null
                ? UserManager.getInstance().getLoggedInUser().getUsername()
                : "-";

        categoryLabel.setText(category != null ? category.getCategoryName().toUpperCase() : "-");
        eventNameLabel.setText(event != null ? event.getEventName() : "-");
        startLabel.setText(event != null ? event.getStartDateTimeFormatted() : "-");
        endLabel.setText(event != null ? event.getEndDateTimeFormatted() : "-");
        locationLabel.setText(event != null && event.getLocation() != null ? event.getLocation() : "-");
        guidanceLabel.setText("Guidance: " + (event != null && event.getLocationGuidance() != null && !event.getLocationGuidance().isEmpty()
                ? event.getLocationGuidance() : "-"));
        notesLabel.setText(event != null && event.getNotes() != null ? event.getNotes() : "-");
        customerNameLabel.setText(customerName != null && !customerName.isEmpty() ? customerName : "Customer");
        customerEmailLabel.setText(customerEmail != null && !customerEmail.isEmpty() ? customerEmail : "-");
        quantityLabel.setText("Qty: " + quantity);
        issuedByLabel.setText("Issued by: " + issuedBy);
        ticketIdLabel.setText("ID: " + generateTicketId());
    }

    private String generateTicketId() {
        return "TKT-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 10000);
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
