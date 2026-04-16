module dk.easv.event_tickets_sea {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires java.mail;
    requires com.google.zxing;
    requires com.google.zxing.javase;

    opens dk.easv.event_tickets_sea to javafx.fxml;
    opens dk.easv.event_tickets_sea.gui to javafx.fxml;
    opens dk.easv.event_tickets_sea.model to javafx.base;
    exports dk.easv.event_tickets_sea;
    exports dk.easv.event_tickets_sea.model;
    exports dk.easv.event_tickets_sea.util;
    exports dk.easv.event_tickets_sea.db;
}
