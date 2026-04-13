package dk.easv.event_tickets_sea.db;

import dk.easv.event_tickets_sea.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * Data Access Object pro správu kategorií
 */
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

    /**
     * Přidá novou kategorii jako nový ticket type pro konkrétní event
     */
    public boolean addCategory(String eventName, String categoryName) {
        String query = "INSERT INTO Tickets (EventId, TicketType, Price, QuantityAvailable, QuantitySold, IsActive) " +
                "SELECT e.EventId, ?, 0, 0, 0, 1 FROM Events e WHERE e.EventName = ? AND e.IsActive = 1";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, categoryName);
            stmt.setString(2, eventName);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Add category error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Získá všechny kategorie (ticket typy) pro konkrétní event
     */
    public ObservableList<Category> getAllCategories(String eventName) {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        String query = "SELECT t.TicketId, t.TicketType, t.Price, t.QuantityAvailable, t.QuantitySold " +
                "FROM Tickets t " +
                "JOIN Events e ON e.EventId = t.EventId " +
                "WHERE e.EventName = ? AND e.IsActive = 1 AND t.IsActive = 1 " +
                "ORDER BY t.TicketType";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, eventName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String description = "Price: " + rs.getDouble("Price") +
                        " | Available: " + (rs.getInt("QuantityAvailable") - rs.getInt("QuantitySold"));
                Category category = new Category(
                        rs.getInt("TicketId"),
                        rs.getString("TicketType"),
                        description
                );
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Get all categories error: " + e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * Smaže kategorii (deaktivuje ticket type)
     */
    public boolean deleteCategory(int categoryId) {
        String query = "UPDATE Tickets SET IsActive = 0 WHERE TicketId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, categoryId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Delete category error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Aktualizuje kategorii (ticket type)
     */
    public boolean updateCategory(int categoryId, String categoryName) {
        String query = "UPDATE Tickets SET TicketType = ? WHERE TicketId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, categoryName);
            stmt.setInt(2, categoryId);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Update category error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}

