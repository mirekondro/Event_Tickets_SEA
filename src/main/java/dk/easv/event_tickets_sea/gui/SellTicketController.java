package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import dk.easv.event_tickets_sea.model.Category;
import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.model.User;
import dk.easv.event_tickets_sea.util.CategoryManager;
import dk.easv.event_tickets_sea.util.EmailService;
import dk.easv.event_tickets_sea.util.UserManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.lang.reflect.Method;
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
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));

        User loggedInUser = UserManager.getInstance().getLoggedInUser();
        issuedByLabel.setText(loggedInUser != null ? loggedInUser.getFullName() : "Unknown user");

        // Show category name + price in combobox
        ticketCategoryComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Category cat, boolean empty) {
                super.updateItem(cat, empty);
                setText(empty || cat == null ? null : cat.getCategoryName() + " — " + cat.getPriceFormatted());
            }
        });
        ticketCategoryComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Category cat, boolean empty) {
                super.updateItem(cat, empty);
                setText(empty || cat == null ? null : cat.getCategoryName() + " — " + cat.getPriceFormatted());
            }
        });
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
    private void handleSendEmail(ActionEvent event) {
        if (!validateInputs()) return;

        String ticketId = UUID.randomUUID().toString();
        String issuedBy = issuedByLabel.getText();
        Category category = ticketCategoryComboBox.getValue();
        String customerName  = customerNameField.getText().trim();
        String customerEmail = customerEmailField.getText().trim();
        int quantity         = quantitySpinner.getValue();

        // Disable button and show sending feedback
        Button btn = (Button) event.getSource();
        btn.setDisable(true);
        btn.setText("Sending...");

        // Send on a background thread so UI doesn't freeze
        new Thread(() -> {
            boolean success = EmailService.getInstance().sendTicketEmail(
                    this.event, category, customerName, customerEmail, quantity, issuedBy, ticketId);

            Platform.runLater(() -> {
                btn.setDisable(false);
                btn.setText("Send via Email (PDF)");

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Email Sent",
                            "Ticket sent successfully",
                            "A ticket confirmation has been sent to " + customerEmail);
                    // Close sell window after successful send
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Email Failed",
                            "Could not send email",
                            "Check that SMTP settings are configured in config/config.settings.");
                }
            });
        }).start();
    }

    @FXML
    private void handleGenerate(ActionEvent event) throws IOException {
        if (!validateInputs()) return;

        if (this.event == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Event", "No event selected",
                    "Please select an event from the dashboard first.");
            return;
        }

        String ticketId = UUID.randomUUID().toString();
        User loggedInUser = UserManager.getInstance().getLoggedInUser();
        String issuedBy = loggedInUser != null ? loggedInUser.getFullName() : "Unknown user";

        Stage sellStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        sellStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ticket-print-view.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Ticket Preview");
        stage.initModality(Modality.APPLICATION_MODAL);

        Object controller = fxmlLoader.getController();
        try {
            Method setTicketDataMethod = controller.getClass().getMethod(
                    "setTicketData", Event.class, String.class, String.class,
                    String.class, int.class, String.class, String.class);
            setTicketDataMethod.invoke(controller,
                    this.event,
                    customerNameField.getText().trim(),
                    customerEmailField.getText().trim(),
                    ticketCategoryComboBox.getValue() != null
                            ? ticketCategoryComboBox.getValue().getCategoryName() : "Regular",
                    quantitySpinner.getValue(),
                    issuedBy,
                    ticketId);
        } catch (ReflectiveOperationException e) {
            showAlert(Alert.AlertType.ERROR, "UI Error", "Ticket preview failed",
                    "Could not pass ticket data to preview window.");
            return;
        }

        stage.show();
    }

    private boolean validateInputs() {
        if (customerNameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Missing name",
                    "Please enter customer full name.");
            return false;
        }
        if (customerEmailField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Missing email",
                    "Please enter customer email.");
            return false;
        }
        if (ticketCategoryComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Missing category",
                    "Please select a ticket category.");
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
