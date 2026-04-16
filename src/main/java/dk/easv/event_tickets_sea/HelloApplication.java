package dk.easv.event_tickets_sea;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.LogManager;

public class HelloApplication extends Application {
    static {
        try {
            LogManager.getLogManager().readConfiguration(
                HelloApplication.class.getResourceAsStream("/logging.properties")
            );
        } catch (Exception e) {
            // Silently ignore if logging configuration cannot be loaded
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1020, 680);
        stage.setTitle("Event Tickets SEA");
        stage.setScene(scene);
        stage.show();
    }
}
