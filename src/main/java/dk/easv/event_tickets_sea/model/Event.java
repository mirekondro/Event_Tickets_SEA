package dk.easv.event_tickets_sea.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private String eventName;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private String location;
    private String locationGuidance;
    private String notes;
    private String coordinator;

    public Event(String eventName, LocalDate startDate, LocalTime startTime,
                 LocalDate endDate, LocalTime endTime, String location,
                 String locationGuidance, String notes, String coordinator) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.location = location;
        this.locationGuidance = locationGuidance;
        this.notes = notes;
        this.coordinator = coordinator;
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

    // Helper method for display
    public String getStartDateTimeFormatted() {
        if (startDate != null && startTime != null) {
            return startDate.toString() + " " + startTime.toString();
        }
        return startDate != null ? startDate.toString() : "";
    }

    public String getEndDateTimeFormatted() {
        if (endDate != null && endTime != null) {
            return endDate.toString() + " " + endTime.toString();
        }
        return endDate != null ? endDate.toString() : "";
    }
}
