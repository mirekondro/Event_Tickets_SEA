module dk.easv.event_tickets_sea {
    requires javafx.controls;
    requires javafx.fxml;

    opens dk.easv.event_tickets_sea to javafx.fxml;
    opens dk.easv.event_tickets_sea.gui to javafx.fxml;
    exports dk.easv.event_tickets_sea;
}