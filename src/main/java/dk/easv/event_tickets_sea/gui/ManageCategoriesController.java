package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;

import dk.easv.event_tickets_sea.model.Category;
import dk.easv.event_tickets_sea.util.CategoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ManageCategoriesController {

    @FXML private TextField categoryNameField;
    @FXML private TextField descriptionField;
    @FXML private TableView<Category> categoriesTable;
    @FXML private TableColumn<Category, String> colCategoryName;
    @FXML private TableColumn<Category, String> colDescription;

    private final ObservableList<Category> categoriesData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colCategoryName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        categoriesTable.setPlaceholder(new Label("No categories found."));
        categoriesTable.setItems(categoriesData);

        reloadCategories();
    }

    @FXML
    private void handleAddCategory(ActionEvent event) {
        String categoryName = categoryNameField.getText().trim();
        String description = descriptionField.getText().trim();

        if (categoryName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Missing Name", "Please enter a category name.");
            return;
        }

        boolean success = CategoryManager.getInstance().addCategory(categoryName, description);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Category Added", "Category '" + categoryName + "' has been added.");
            categoryNameField.clear();
            descriptionField.clear();
            reloadCategories();
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to Add", "Could not add category to database.");
        }
    }

    @FXML
    private void handleEditCategory(ActionEvent event) {
        Category selected = categoriesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Category Selected", "Please select a category to edit.");
            return;
        }

        categoryNameField.setText(selected.getCategoryName());
        descriptionField.setText(selected.getDescription());
    }

    @FXML
    private void handleDeleteCategory(ActionEvent event) {
        Category selected = categoriesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Category Selected", "Please select a category to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Category");
        confirm.setHeaderText("Delete Category?");
        confirm.setContentText("Are you sure you want to delete '" + selected.getCategoryName() + "'?");
        if (confirm.showAndWait().orElse(null) == ButtonType.OK) {
            CategoryManager.getInstance().removeCategory(selected);
            reloadCategories();
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void reloadCategories() {
        categoriesData.setAll(CategoryManager.getInstance().getCategories());
        categoriesTable.refresh();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

