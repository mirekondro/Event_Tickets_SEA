package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.util.EventManager;
import dk.easv.event_tickets_sea.util.UserManager;
import dk.easv.event_tickets_sea.util.VoucherManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class VoucherController {

    @FXML private TextField descriptionField;
    @FXML private ComboBox<String> eventComboBox;
    @FXML private TextField quantityField;
    @FXML private CheckBox allEventsCheckBox;

    @FXML
    public void initialize() {
        for (Event e : EventManager.getInstance().getEvents()) {
            eventComboBox.getItems().add(e.getEventName());
        }

        allEventsCheckBox.selectedProperty().addListener((obs, old, selected) -> {
            eventComboBox.setDisable(selected);
            if (selected) eventComboBox.getSelectionModel().clearSelection();
        });
    }

    @FXML
    private void handleGenerate(ActionEvent event) throws IOException {
        if (!validateFields()) return;

        String description = descriptionField.getText().trim();
        String eventName   = allEventsCheckBox.isSelected() ? null : eventComboBox.getValue();
        int quantity       = Integer.parseInt(quantityField.getText().trim());
        String createdBy   = UserManager.getInstance().getLoggedInUser().getUsername();

        ObservableList<String> codes = VoucherManager.getInstance()
                .createVouchers(description, eventName, createdBy, quantity);

        if (codes.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error",
                    "Failed to create vouchers. Please try again.");
            return;
        }

        // Close the create form
        Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        thisStage.close();

        // Open ONE scrollable batch print window
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("voucher-batch-print.fxml"));
        Stage printStage = new Stage();
        printStage.setScene(new Scene(loader.load()));
        printStage.setTitle("Print Vouchers (" + codes.size() + ")");
        printStage.initModality(Modality.APPLICATION_MODAL);

        VoucherBatchPrintController controller = loader.getController();
        controller.setVouchers(description, eventName, List.copyOf(codes));

        printStage.show();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private boolean validateFields() {
        if (descriptionField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Missing Description",
                    "Please enter a description (e.g. 1 Free Beer).");
            return false;
        }
        if (!allEventsCheckBox.isSelected() && eventComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Missing Event",
                    "Please select an event or check Valid for all events.");
            return false;
        }
        String qtyText = quantityField.getText().trim();
        if (qtyText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Missing Quantity",
                    "Please enter how many vouchers to generate.");
            return false;
        }
        try {
            int qty = Integer.parseInt(qtyText);
            if (qty < 1 || qty > 500) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Quantity",
                        "Quantity must be between 1 and 500.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Quantity",
                    "Please enter a whole number.");
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
