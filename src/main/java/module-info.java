module dk.easv.event_tickets_sea {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.easv.event_tickets_sea to javafx.fxml;
    exports dk.easv.event_tickets_sea;
}