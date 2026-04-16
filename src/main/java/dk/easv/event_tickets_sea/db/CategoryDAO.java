package dk.easv.event_tickets_sea.db;

import dk.easv.event_tickets_sea.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CategoryDAO {
    private static CategoryDAO instance;
    private final DatabaseConnection dbConnection;

    private CategoryDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public static CategoryDAO getInstance() {
        if (instance == null) {
            instance = new CategoryDAO();
        }
        return instance;
    }

    /** Get all ticket categories (rows in Tickets) for a specific event */
    public ObservableList<Category> getAllCategories(String eventName) {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        String query = "SELECT t.TicketId, t.TicketType, t.Price, t.QuantityAvailable, t.QuantitySold " +
                "FROM Tickets t " +
                "JOIN Events e ON t.EventId = e.EventId " +
                "WHERE e.EventName = ? AND t.IsActive = 1 " +
                "ORDER BY t.TicketType";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, eventName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int available = rs.getInt("QuantityAvailable") - rs.getInt("QuantitySold");
                categories.add(new Category(
                        rs.getInt("TicketId"),
                        rs.getString("TicketType"),
                        "Price: " + rs.getDouble("Price") + " | Available: " + available,
                        rs.getDouble("Price"),
                        rs.getInt("QuantityAvailable")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Get all categories error: " + e.getMessage());
        }
        return categories;
    }

    /** Add a new ticket type row linked to an event */
    public boolean addCategory(String eventName, String categoryName, String description, double price, int quantity) {
        String query = "INSERT INTO Tickets (EventId, TicketType, Price, QuantityAvailable) " +
                "SELECT EventId, ?, ?, ? FROM Events WHERE EventName = ? AND IsActive = 1";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, categoryName);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            stmt.setString(4, eventName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Add category error: " + e.getMessage());
        }
        return false;
    }

    /** Update an existing ticket type row */
    public boolean updateCategory(int ticketId, String categoryName, String description, double price, int quantity) {
        String query = "UPDATE Tickets SET TicketType = ?, Price = ?, QuantityAvailable = ? WHERE TicketId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, categoryName);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            stmt.setInt(4, ticketId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update category error: " + e.getMessage());
        }
        return false;
    }

    /** Soft-delete a ticket type row */
    public boolean deleteCategory(int ticketId) {
        String query = "UPDATE Tickets SET IsActive = 0 WHERE TicketId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, ticketId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete category error: " + e.getMessage());
        }
        return false;
    }
}
