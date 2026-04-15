package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.model.Category;
import dk.easv.event_tickets_sea.model.User;
import dk.easv.event_tickets_sea.db.TicketDAO;
import dk.easv.event_tickets_sea.util.CategoryManager;
import dk.easv.event_tickets_sea.util.TicketEmailService;
import dk.easv.event_tickets_sea.util.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SellTicketController {
    @FXML private Label selectedEventLabel;
    @FXML private Label issuedByLabel;
    @FXML private TextField customerNameField;
    @FXML private TextField customerEmailField;
    @FXML private ComboBox<Category> ticketCategoryComboBox;
    @FXML private Spinner<Integer> quantitySpinner;

    private Event event;

    @FXML
    public void initialize() {
        // Categories are loaded after event is selected.
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));

        User loggedInUser = UserManager.getInstance().getLoggedInUser();
        issuedByLabel.setText(loggedInUser != null ? loggedInUser.getFullName() : "Unknown user");
    }

    public void setEvent(Event event) {
        this.event = event;
        selectedEventLabel.setText(event != null ? event.getEventName() : "No event selected");
        if (event != null) {
            ticketCategoryComboBox.setItems(CategoryManager.getInstance().getCategories(event.getEventName()));
            ticketCategoryComboBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleGenerate(ActionEvent event) throws IOException {
        if (!validateInputs()) {
            return;
        }

        if (this.event == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Event", "No event selected", "Please select an event from dashboard first.");
            return;
        }

        String ticketId = UUID.randomUUID().toString();
        User loggedInUser = UserManager.getInstance().getLoggedInUser();
        String issuedBy = loggedInUser != null ? loggedInUser.getFullName() : "Unknown user";

        handleCancel(event);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ticket-print-view.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Ticket Preview");
        stage.initModality(Modality.APPLICATION_MODAL);

        Object controller = fxmlLoader.getController();
        try {
            Method setTicketDataMethod = controller.getClass().getMethod(
                    "setTicketData",
                    Event.class,
                    String.class,
                    String.class,
                    String.class,
                    int.class,
                    String.class,
                    String.class
            );
            setTicketDataMethod.invoke(
                    controller,
                    this.event,
                    customerNameField.getText().trim(),
                    customerEmailField.getText().trim(),
                    ticketCategoryComboBox.getValue() != null ? ticketCategoryComboBox.getValue().getCategoryName() : "Regular",
                    quantitySpinner.getValue(),
                    issuedBy,
                    ticketId
            );
        } catch (ReflectiveOperationException e) {
            showAlert(Alert.AlertType.ERROR, "UI Error", "Ticket preview failed", "Could not pass ticket data to preview window.");
            return;
        }

        stage.show();
    }

    @FXML
    private void handleSendEmail(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        if (this.event == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Event", "No event selected", "Please select an event from dashboard first.");
            return;
        }

        Category selectedCategory = ticketCategoryComboBox.getValue();
        if (selectedCategory == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Category", "No ticket category selected", "Please choose a ticket category first.");
            return;
        }

        User loggedInUser = UserManager.getInstance().getLoggedInUser();
        String soldByUsername = loggedInUser != null ? loggedInUser.getUsername() : null;
        if (soldByUsername == null || soldByUsername.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Missing User", "No logged in user", "Please log in again before selling tickets.");
            return;
        }

        int quantity = quantitySpinner.getValue();
        List<String> soldTicketCodes = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            String ticketCode = TicketDAO.getInstance().sellTicketByIdAndGetCode(
                    selectedCategory.getCategoryId(),
                    customerNameField.getText().trim(),
                    customerEmailField.getText().trim(),
                    soldByUsername
            );
            if (ticketCode == null) {
                break;
            }
            soldTicketCodes.add(ticketCode);
        }

        if (soldTicketCodes.isEmpty()) {
            String dbReason = TicketDAO.getInstance().getLastErrorMessage();
            String details = "Ticket was not saved to the database.";
            if (dbReason != null && !dbReason.isBlank()) {
                details += "\n\nReason: " + dbReason;
            }
            showAlert(Alert.AlertType.ERROR, "Sale Failed", "Could not sell ticket", details);
            return;
        }

        String allCodes = String.join(", ", soldTicketCodes);

        try {
            TicketEmailService.getInstance().sendTicketEmail(
                    customerEmailField.getText().trim(),
                    customerNameField.getText().trim(),
                    this.event,
                    selectedCategory,
                    soldTicketCodes.size(),
                    allCodes,
                    loggedInUser != null ? loggedInUser.getFullName() : "Unknown user"
            );

            String message = "Ticket code(s) " + allCodes + " were sent to " + customerEmailField.getText().trim() + ".";
            if (soldTicketCodes.size() < quantity) {
                message += "\n\nOnly " + soldTicketCodes.size() + " out of " + quantity + " tickets were sold (insufficient availability).";
            }
            showAlert(Alert.AlertType.INFORMATION, "Email Sent", "Ticket emailed successfully", message);
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.WARNING, "Ticket Saved", "Email not configured", e.getMessage() + "\n\nThe ticket was sold and stored in the database, but the email could not be sent.");
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Email Failed", "Could not send ticket email", e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (customerNameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Missing name", "Please enter customer full name.");
            return false;
        }
        if (customerEmailField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Missing email", "Please enter customer email.");
            return false;
        }
        if (ticketCategoryComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Missing category", "Please select ticket category.");
            return false;
        }
        if (quantitySpinner.getValue() == null || quantitySpinner.getValue() < 1) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid quantity", "Please select at least 1 ticket.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}