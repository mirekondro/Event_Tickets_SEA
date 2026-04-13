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
     * Přidá novou kategorii
     */
    public boolean addCategory(String categoryName, String description) {
        String query = "INSERT INTO Categories (CategoryName, Description) VALUES (?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, categoryName);
            stmt.setString(2, description != null ? description : "");

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Add category error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Získá všechny kategorie
     */
    public ObservableList<Category> getAllCategories() {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        String query = "SELECT CategoryId, CategoryName, Description FROM Categories WHERE IsActive = 1 ORDER BY CategoryName";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("CategoryId"),
                        rs.getString("CategoryName"),
                        rs.getString("Description")
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
     * Smaže kategorii (deaktivuje)
     */
    public boolean deleteCategory(int categoryId) {
        String query = "UPDATE Categories SET IsActive = 0 WHERE CategoryId = ?";

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
     * Aktualizuje kategorii
     */
    public boolean updateCategory(int categoryId, String categoryName, String description) {
        String query = "UPDATE Categories SET CategoryName = ?, Description = ? WHERE CategoryId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, categoryName);
            stmt.setString(2, description != null ? description : "");
            stmt.setInt(3, categoryId);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Update category error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}

