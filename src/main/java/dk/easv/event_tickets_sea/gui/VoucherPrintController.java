package dk.easv.event_tickets_sea.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class VoucherPrintController {

    @FXML private Label descriptionLabel;
    @FXML private Label eventLabel;
    @FXML private Label codeLabel;
    @FXML private Label validityLabel;

    public void setVoucherData(String description, String eventName, String code) {
        descriptionLabel.setText(description);
        codeLabel.setText("ID: " + code);

        if (eventName != null && !eventName.isEmpty()) {
            eventLabel.setText("Valid for: " + eventName);
            validityLabel.setText("Valid for: " + eventName);
        } else {
            eventLabel.setText("Valid for: All Events");
            validityLabel.setText("Valid for all events");
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
