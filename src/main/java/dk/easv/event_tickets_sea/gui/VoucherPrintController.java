package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.util.QRCodeGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class VoucherPrintController {

    @FXML private Label descriptionLabel;
    @FXML private Label eventLabel;
    @FXML private Label codeLabel;
    @FXML private Label validityLabel;
    @FXML private ImageView qrCodeImageView;

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

        // Generate QR code from voucher code
        if (qrCodeImageView != null) {
            qrCodeImageView.setImage(QRCodeGenerator.generateQRCode(code, 140));
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
