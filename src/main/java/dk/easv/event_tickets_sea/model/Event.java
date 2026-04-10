package dk.easv.event_tickets_sea.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private String eventName;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;      // Can be NULL
    private LocalTime endTime;      // Can be NULL
    private String location;
    private String locationGuidance;
    private String notes;
    private String coordinator;
    private ObservableList<String> coCoordinators;

    public Event(String eventName, LocalDate startDate, LocalTime startTime,
                 LocalDate endDate, LocalTime endTime, String location,
                 String locationGuidance, String notes, String coordinator) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;     // Can be NULL
        this.endTime = endTime;     // Can be NULL
        this.location = location;
        this.locationGuidance = locationGuidance;
        this.notes = notes;
        this.coordinator = coordinator;
        this.coCoordinators = FXCollections.observableArrayList();
    }

    // Getters and Setters
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationGuidance() {
        return locationGuidance;
    }

    public void setLocationGuidance(String locationGuidance) {
        this.locationGuidance = locationGuidance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(String coordinator) {
        this.coordinator = coordinator;
    }

    public ObservableList<String> getCoCoordinators() {
        return coCoordinators;
    }

    public void addCoCoordinator(String coCoordinator) {
        if (!coCoordinators.contains(coCoordinator) && !coCoordinator.equals(coordinator)) {
            coCoordinators.add(coCoordinator);
        }
    }

    public void removeCoCoordinator(String coCoordinator) {
        coCoordinators.remove(coCoordinator);
    }

    // Helper method for display - handles NULL values
    public String getStartDateTimeFormatted() {
        if (startDate != null && startTime != null) {
            return startDate.toString() + " " + startTime.toString();
        }
        if (startDate != null) {
            return startDate.toString();
        }
        return "";
    }

    // Helper method for display - handles NULL values
    public String getEndDateTimeFormatted() {
        if (endDate != null && endTime != null) {
            return endDate.toString() + " " + endTime.toString();
        }
        if (endDate != null) {
            return endDate.toString();
        }
        // If end date is NULL, return empty or "N/A"
        return "—";  // Em dash to show it's optional/not set
    }

    public String getCoCoordinatorsFormatted() {
        if (coCoordinators.isEmpty()) {
            return "None";
        }
        return String.join(", ", coCoordinators);
    }
}
