package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import dk.easv.event_tickets_sea.model.Category;
import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.util.CategoryManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class SellTicketController {

    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextField customerNameField;
    @FXML private TextField customerEmailField;
    @FXML private TextField quantityField;

    private Event selectedEvent;

    @FXML
    public void initialize() {
        // Show category name + price in combobox
        categoryComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Category cat, boolean empty) {
                super.updateItem(cat, empty);
                setText(empty || cat == null ? null : cat.getCategoryName() + " — " + cat.getPriceFormatted());
            }
        });
        categoryComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Category cat, boolean empty) {
                super.updateItem(cat, empty);
                setText(empty || cat == null ? null : cat.getCategoryName() + " — " + cat.getPriceFormatted());
            }
        });
    }

    /** Called from CoordinatorController to pass the selected event */
    public void setEvent(Event event) {
        this.selectedEvent = event;
        if (event != null) {
            categoryComboBox.setItems(CategoryManager.getInstance().getCategories(event.getEventName()));
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleGenerate(ActionEvent event) throws IOException {
        Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
        String customerName  = customerNameField.getText().trim();
        String customerEmail = customerEmailField.getText().trim();
        String qtyText       = quantityField.getText().trim();

        if (selectedEvent == null) {
            showAlert("Missing Event", "No event was passed. Please close and select an event first.");
            return;
        }
        if (selectedCategory == null) {
            showAlert("Missing Category", "Please select a ticket category.");
            return;
        }
        if (customerName.isEmpty()) {
            showAlert("Missing Name", "Please enter the customer's full name.");
            return;
        }
        if (customerEmail.isEmpty()) {
            showAlert("Missing Email", "Please enter the customer's email.");
            return;
        }

        int quantity = 1;
        if (!qtyText.isEmpty()) {
            try {
                quantity = Integer.parseInt(qtyText);
                if (quantity < 1) {
                    showAlert("Invalid Quantity", "Quantity must be at least 1.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Quantity", "Please enter a whole number for quantity.");
                return;
            }
        }

        // Close sell window and open print preview with data
        Stage sellStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        sellStage.close();

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("ticket-print-view.fxml"));
        Stage printStage = new Stage();
        printStage.setScene(new Scene(loader.load()));
        printStage.setTitle("Ticket Preview");
        printStage.initModality(Modality.APPLICATION_MODAL);

        TicketPrintController controller = loader.getController();
        controller.setTicketData(selectedEvent, selectedCategory, customerName, customerEmail, quantity);

        printStage.show();
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
