package dk.easv.event_tickets_sea.util;

import dk.easv.event_tickets_sea.db.EventDAO;
import dk.easv.event_tickets_sea.model.Event;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventManager {
    private static EventManager instance;
    private final EventDAO eventDAO;

    private EventManager() {
        this.eventDAO = EventDAO.getInstance();
    }

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    /**
     * Získá všechny akce z databáze
     */
    public ObservableList<Event> getEvents() {
        return eventDAO.getAllEvents();
    }

    /**
     * Přidá novou akci
     */
    public boolean addEvent(String eventName, LocalDate startDate, LocalTime startTime,
                           LocalDate endDate, LocalTime endTime, String location,
                           String locationGuidance, String notes, String coordinatorUsername) {
        return eventDAO.addEvent(eventName, startDate, startTime, endDate, endTime, location, locationGuidance, notes, coordinatorUsername);
    }

    /**
     * Odstraní akci
     */
    public void removeEvent(Event event) {
        eventDAO.deleteEvent(event.getEventName());
    }

    /**
     * Získá akce pro konkrétního koordinátora
     */
    public ObservableList<Event> getEventsByCoordinator(String coordinatorUsername) {
        return eventDAO.getEventsByCoordinator(coordinatorUsername);
    }

    /**
     * Aktualizuje akci
     */
    public boolean updateEvent(String eventName, LocalDate startDate, LocalTime startTime,
                              LocalDate endDate, LocalTime endTime, String location,
                              String locationGuidance, String notes) {
        return eventDAO.updateEvent(eventName, startDate, startTime, endDate, endTime, location, locationGuidance, notes);
    }

    /**
     * Přiřadí hlavního koordinátora akci
     */
    public boolean assignCoordinator(String eventName, String coordinatorUsername) {
        return EventDAO.getInstance().assignCoordinator(eventName, coordinatorUsername);
    }
}
