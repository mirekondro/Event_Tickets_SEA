package dk.easv.event_tickets_sea.util;

import dk.easv.event_tickets_sea.model.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventManager {
    private static EventManager instance;
    private ObservableList<Event> events;

    private EventManager() {
        events = FXCollections.observableArrayList();

        // Add some sample events
        events.add(new Event(
                "EASV Spring Party",
                LocalDate.of(2025, 4, 15),
                LocalTime.of(20, 0),
                LocalDate.of(2025, 4, 16),
                LocalTime.of(2, 0),
                "EASV Campus, Building A",
                "Parking available at main entrance",
                "Bring good vibes and dancing shoes!",
                "John Doe"
        ));

        events.add(new Event(
                "Wine Tasting Evening",
                LocalDate.of(2025, 4, 22),
                LocalTime.of(18, 30),
                LocalDate.of(2025, 4, 22),
                LocalTime.of(22, 0),
                "EASV Lounge",
                "Take bus line 3",
                "Limited spots available",
                "Jane Smith"
        ));

        events.add(new Event(
                "Tech Conference 2025",
                LocalDate.of(2025, 5, 10),
                LocalTime.of(9, 0),
                LocalDate.of(2025, 5, 12),
                LocalTime.of(17, 0),
                "Main Hall",
                "Underground parking",
                "Three days of innovation and networking",
                "John Doe"
        ));
    }

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public ObservableList<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }
}
