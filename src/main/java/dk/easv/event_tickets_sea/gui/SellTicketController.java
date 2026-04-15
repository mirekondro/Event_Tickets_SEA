package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.HelloApplication;
import dk.easv.event_tickets_sea.model.Category;
import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.util.CategoryManager;
import dk.easv.event_tickets_sea.util.EventManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class SellTicketController {

    @FXML private ComboBox<Event> eventComboBox;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextField customerNameField;
    @FXML private TextField customerEmailField;
    @FXML private TextField quantityField;

    @FXML
    public void initialize() {
        // Load all events
        eventComboBox.setItems(EventManager.getInstance().getEvents());

        // Show event name in combobox
        eventComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);
                setText(empty || event == null ? null : event.getEventName());
            }
        });
        eventComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);
                setText(empty || event == null ? null : event.getEventName());
            }
        });

        // When event is selected, load its categories
        eventComboBox.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            categoryComboBox.getItems().clear();
            if (selected != null) {
                categoryComboBox.setItems(CategoryManager.getInstance().getCategories(selected.getEventName()));
            }
        });

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

    public void setEvent(Event event) {
        if (event != null) {
            eventComboBox.getSelectionModel().select(event);
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleGenerate(ActionEvent event) throws IOException {
        handleCancel(event);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ticket-print-view.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Ticket Preview");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
