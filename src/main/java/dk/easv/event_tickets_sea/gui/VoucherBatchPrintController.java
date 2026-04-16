package dk.easv.event_tickets_sea.gui;

import dk.easv.event_tickets_sea.util.QRCodeGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class VoucherBatchPrintController {

    @FXML private VBox vouchersContainer;
    @FXML private Label titleLabel;

    /**
     * Populates the scroll view with one voucher card per code.
     */
    public void setVouchers(String description, String eventName, List<String> codes) {
        titleLabel.setText("Vouchers: " + description + " (" + codes.size() + " total)");

        String eventText = (eventName != null && !eventName.isEmpty())
                ? "Valid for: " + eventName
                : "Valid for: All Events";

        for (int i = 0; i < codes.size(); i++) {
            String code = codes.get(i);

            // Voucher card
            HBox card = new HBox(24);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new Insets(16));
            card.setStyle("-fx-background-color: #EFF6FF; -fx-background-radius: 12px; -fx-border-color: #BFDBFE; -fx-border-radius: 12px;");

            // QR code
            ImageView qr = new ImageView();
            qr.setFitWidth(100);
            qr.setFitHeight(100);
            qr.setPreserveRatio(true);
            qr.setImage(QRCodeGenerator.generateQRCode(code, 100));

            // Text info
            VBox info = new VBox(6);
            info.setAlignment(Pos.CENTER_LEFT);

            Label numLabel = new Label("#" + (i + 1));
            numLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #6B7280;");

            Label descLabel = new Label(description);
            descLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1E3A8A;");

            Label eventLabel = new Label(eventText);
            eventLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #3B82F6;");

            Label codeLabel = new Label("ID: " + code);
            codeLabel.setStyle("-fx-font-family: 'Courier New', monospace; -fx-font-size: 11px; -fx-text-fill: #6B7280;");

            Label onceLabel = new Label("One-time use only");
            onceLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #EF4444; -fx-font-weight: bold;");

            info.getChildren().addAll(numLabel, descLabel, eventLabel, codeLabel, onceLabel);
            card.getChildren().addAll(qr, info);
            vouchersContainer.getChildren().add(card);

            // Separator between cards (not after last one)
            if (i < codes.size() - 1) {
                Separator sep = new Separator();
                sep.setPadding(new Insets(4, 0, 4, 0));
                vouchersContainer.getChildren().add(sep);
            }
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
