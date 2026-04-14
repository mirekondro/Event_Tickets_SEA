package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.model.Category;
import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.util.CategoryManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddCategoryController {

    @FXML private Label formTitleLabel;
    @FXML private TextField categoryNameField;
    @FXML private TextField descriptionField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private Button saveButton;

    private Category existingCategory;
    private Event selectedEvent;

    private final CategoryManager categoryManager = CategoryManager.getInstance();

    /** Call with null for Add mode, or a Category for Edit mode */
    public void setCategory(Category category, Event event) {
        this.existingCategory = category;
        this.selectedEvent = event;

        if (category != null) {
            // Edit mode
            formTitleLabel.setText("Edit Category");
            saveButton.setText("Save Changes");
            categoryNameField.setText(category.getCategoryName());
            descriptionField.setText(category.getDescription());
            priceField.setText(String.valueOf(category.getPrice()));
            quantityField.setText(String.valueOf(category.getQuantity()));
        } else {
            // Add mode
            formTitleLabel.setText("Add Category");
            saveButton.setText("Add Category");
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateFields()) return;

        String name        = categoryNameField.getText().trim();
        String description = descriptionField.getText().trim();
        double price       = Double.parseDouble(priceField.getText().trim());
        int    quantity    = Integer.parseInt(quantityField.getText().trim());

        boolean success;
        if (existingCategory == null) {
            success = categoryManager.addCategory(selectedEvent.getEventName(), name, description, price, quantity);
        } else {
            success = categoryManager.updateCategory(existingCategory.getCategoryId(), name, description, price, quantity);
        }

        if (success) {
            closeWindow(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error",
                    "Failed to save the category. Please try again.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow(event);
    }

    private boolean validateFields() {
        if (categoryNameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Missing Name",
                    "Please enter a name for the category.");
            return false;
        }
        String priceText = priceField.getText().trim();
        if (priceText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Missing Price",
                    "Please enter a price. Use 0 for free tickets.");
            return false;
        }
        try {
            if (Double.parseDouble(priceText) < 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Price",
                        "Price cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Price",
                    "Please enter a valid number (e.g. 99.99).");
            return false;
        }
        String quantityText = quantityField.getText().trim();
        if (quantityText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Missing Quantity",
                    "Please enter the available quantity.");
            return false;
        }
        try {
            if (Integer.parseInt(quantityText) < 1) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Quantity",
                        "Quantity must be at least 1.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Quantity",
                    "Please enter a whole number (e.g. 100).");
            return false;
        }
        return true;
    }

    private void closeWindow(ActionEvent event) {
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
