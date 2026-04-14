package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import dk.easv.event_tickets_sea.model.Category;
import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.util.CategoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class ManageCategoriesController {

    @FXML private Label selectedEventLabel;
    @FXML private TextField categoryNameField;
    @FXML private TableView<Category> categoriesTable;
    @FXML private TableColumn<Category, String> colCategoryName;
    @FXML private TableColumn<Category, String> colDescription;
    @FXML private TableColumn<Category, String> colPrice;
    @FXML private TableColumn<Category, Integer> colQuantity;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final ObservableList<Category> categoriesData = FXCollections.observableArrayList();
    private Event selectedEvent;

    private final CategoryManager categoryManager = CategoryManager.getInstance();

    public void setEvent(Event event) {
        this.selectedEvent = event;
        if (event != null) {
            selectedEventLabel.setText(event.getEventName());
        }
        loadData();
    }

    @FXML
    public void initialize() {
        colCategoryName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("priceFormatted"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        categoriesTable.setItems(categoriesData);

        editButton.setDisable(true);
        deleteButton.setDisable(true);
        categoriesTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            editButton.setDisable(selected == null);
            deleteButton.setDisable(selected == null);
        });
    }

    private void loadData() {
        if (selectedEvent != null) {
            categoriesData.setAll(categoryManager.getCategories(selectedEvent.getEventName()));
        }
    }

    @FXML
    private void handleAdd() throws IOException {
        openForm(null);
    }

    @FXML
    private void handleEdit() throws IOException {
        Category selected = categoriesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openForm(selected);
        }
    }

    @FXML
    private void handleDelete() {
        Category selected = categoriesTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Category");
        confirm.setHeaderText("Delete '" + selected.getCategoryName() + "'?");
        confirm.setContentText("This action cannot be undone.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                categoryManager.removeCategory(selected);
                loadData();
            }
        });
    }

    private void openForm(Category existing) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("add-category.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle(existing == null ? "Add Category" : "Edit Category");
        stage.initModality(Modality.APPLICATION_MODAL);

        AddCategoryController controller = loader.getController();
        controller.setCategory(existing, selectedEvent);

        stage.showAndWait();
        loadData();
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
