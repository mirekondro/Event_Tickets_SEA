package dk.easv.event_tickets_sea.db;

import dk.easv.event_tickets_sea.model.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Access Object pro správu akcí
 */
public class EventDAO {
    private static EventDAO instance;
    private final DatabaseConnection dbConnection;

    private EventDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public static EventDAO getInstance() {
        if (instance == null) {
            instance = new EventDAO();
        }
        return instance;
    }

    /**
     * Přidá novou akci (EndDate a EndTime můžou být NULL)
     */
    public boolean addEvent(String eventName, LocalDate startDate, LocalTime startTime,
                            LocalDate endDate, LocalTime endTime, String location,
                            String locationGuidance, String notes, String coordinatorUsername) {
        String query = "INSERT INTO Events (EventName, StartDate, StartTime, EndDate, EndTime, Location, LocationGuidance, Notes, CoordinatorId) " +
                "SELECT ?, ?, ?, ?, ?, ?, ?, ?, u.UserId FROM Users u WHERE u.Username = ? AND u.IsActive = 1";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, eventName);
            stmt.setDate(2, java.sql.Date.valueOf(startDate));
            stmt.setTime(3, java.sql.Time.valueOf(startTime));

            // Handle NULL for EndDate and EndTime
            if (endDate != null) {
                stmt.setDate(4, java.sql.Date.valueOf(endDate));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            if (endTime != null) {
                stmt.setTime(5, java.sql.Time.valueOf(endTime));
            } else {
                stmt.setNull(5, Types.TIME);
            }

            stmt.setString(6, location);
            stmt.setString(7, locationGuidance);
            stmt.setString(8, notes);
            stmt.setString(9, coordinatorUsername);

            int result = stmt.executeUpdate();
            if (result == 0) {
                System.err.println("Add event error: coordinator not found or inactive: " + coordinatorUsername);
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Add event error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Získá všechny akce
     */
    public ObservableList<Event> getAllEvents() {
        ObservableList<Event> events = FXCollections.observableArrayList();
        String query = "SELECT e.EventName, e.StartDate, e.StartTime, e.EndDate, e.EndTime, e.Location, e.LocationGuidance, e.Notes, u.FullName " +
                "FROM Events e " +
                "LEFT JOIN Users u ON e.CoordinatorId = u.UserId " +
                "WHERE e.IsActive = 1 " +
                "ORDER BY e.StartDate";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Handle NULL EndDate and EndTime
                Date endDateSql = rs.getDate("EndDate");
                Time endTimeSql = rs.getTime("EndTime");

                LocalDate endDate = (endDateSql != null) ? endDateSql.toLocalDate() : null;
                LocalTime endTime = (endTimeSql != null) ? endTimeSql.toLocalTime() : null;

                Event event = new Event(
                        rs.getString("EventName"),
                        rs.getDate("StartDate").toLocalDate(),
                        rs.getTime("StartTime").toLocalTime(),
                        endDate,  // Can be NULL
                        endTime,  // Can be NULL
                        rs.getString("Location"),
                        rs.getString("LocationGuidance"),
                        rs.getString("Notes"),
                        rs.getString("FullName")
                );
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Get all events error: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }

    /**
     * Získá akce pro konkrétního koordinátora
     */
    public ObservableList<Event> getEventsByCoordinator(String coordinatorUsername) {
        ObservableList<Event> events = FXCollections.observableArrayList();
        String query = "SELECT e.EventName, e.StartDate, e.StartTime, e.EndDate, e.EndTime, e.Location, e.LocationGuidance, e.Notes, u.FullName " +
                "FROM Events e " +
                "LEFT JOIN Users u ON e.CoordinatorId = u.UserId " +
                "WHERE e.CoordinatorId = (SELECT UserId FROM Users WHERE Username = ?) AND e.IsActive = 1 " +
                "ORDER BY e.StartDate";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, coordinatorUsername);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Handle NULL EndDate and EndTime
                Date endDateSql = rs.getDate("EndDate");
                Time endTimeSql = rs.getTime("EndTime");

                LocalDate endDate = (endDateSql != null) ? endDateSql.toLocalDate() : null;
                LocalTime endTime = (endTimeSql != null) ? endTimeSql.toLocalTime() : null;

                Event event = new Event(
                        rs.getString("EventName"),
                        rs.getDate("StartDate").toLocalDate(),
                        rs.getTime("StartTime").toLocalTime(),
                        endDate,  // Can be NULL
                        endTime,  // Can be NULL
                        rs.getString("Location"),
                        rs.getString("LocationGuidance"),
                        rs.getString("Notes"),
                        rs.getString("FullName")
                );
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Get events by coordinator error: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }

    /**
     * Smaže akci (deaktivuje)
     */
    public boolean deleteEvent(String eventName) {
        String query = "UPDATE Events SET IsActive = 0 WHERE EventName = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, eventName);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Delete event error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Aktualizuje akci (EndDate a EndTime můžou být NULL)
     */
    public boolean updateEvent(String eventName, LocalDate startDate, LocalTime startTime,
                               LocalDate endDate, LocalTime endTime, String location,
                               String locationGuidance, String notes) {
        String query = "UPDATE Events SET StartDate = ?, StartTime = ?, EndDate = ?, EndTime = ?, Location = ?, LocationGuidance = ?, Notes = ? WHERE EventName = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setTime(2, java.sql.Time.valueOf(startTime));

            // Handle NULL for EndDate and EndTime
            if (endDate != null) {
                stmt.setDate(3, java.sql.Date.valueOf(endDate));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            if (endTime != null) {
                stmt.setTime(4, java.sql.Time.valueOf(endTime));
            } else {
                stmt.setNull(4, Types.TIME);
            }

            stmt.setString(5, location);
            stmt.setString(6, locationGuidance);
            stmt.setString(7, notes);
            stmt.setString(8, eventName);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Update event error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Přiřadí hlavního koordinátora k akci dle username
     */
    public boolean assignCoordinator(String eventName, String coordinatorUsername) {
        String query = "UPDATE e SET e.CoordinatorId = u.UserId " +
                "FROM Events e " +
                "INNER JOIN Users u ON u.Username = ? AND u.IsActive = 1 " +
                "WHERE e.EventName = ? AND e.IsActive = 1";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, coordinatorUsername);
            stmt.setString(2, eventName);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Assign coordinator error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
