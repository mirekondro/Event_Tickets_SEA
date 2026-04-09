package dk.easv.event_tickets_sea.db;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.event_tickets_sea.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * Data Access Object pro správu uživatelů
 */
public class UserDAO {
    private static UserDAO instance;
    private final DatabaseConnection dbConnection;

    private UserDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    /**
     * Ověří přihlášení uživatele
     */
    public User verifyLogin(String username, String password) {
        String query = "SELECT UserId, Username, Email, FullName, Role FROM Users WHERE Username = ? AND Password = ? AND IsActive = 1";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("Username"),
                        rs.getString("Role"),
                        rs.getString("Email"),
                        rs.getString("FullName")
                );
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Přidá nového uživatele
     */
    public boolean addUser(String username, String password, String email, String fullName, String role) {
        String query = "INSERT INTO Users (Username, Password, Email, FullName, Role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, fullName);
            stmt.setString(5, role);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Add user error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Získá všechny uživatele
     */
    public ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String query = "SELECT Username, Role, Email, FullName FROM Users WHERE IsActive = 1";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User(
                        rs.getString("Username"),
                        rs.getString("Role"),
                        rs.getString("Email"),
                        rs.getString("FullName")
                );
                users.add(user);
            }
        } catch (SQLServerException e) {
            System.err.println("Get all users error - SQL Server: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Get all users error: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Získá uživatele dle jména
     */
    public User getUserByUsername(String username) {
        String query = "SELECT Username, Role, Email, FullName FROM Users WHERE Username = ? AND IsActive = 1";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("Username"),
                        rs.getString("Role"),
                        rs.getString("Email"),
                        rs.getString("FullName")
                );
            }
        } catch (SQLException e) {
            System.err.println("Get user error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Aktualizuje uživatele
     */
    public boolean updateUser(String username, String email, String fullName) {
        String query = "UPDATE Users SET Email = ?, FullName = ? WHERE Username = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, fullName);
            stmt.setString(3, username);

            stmt.executeUpdate();
            return true;
        } catch (SQLServerException e) {
            System.err.println("Update user error - SQL Server: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Update user error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Smaže uživatele (deaktivuje)
     */
    public boolean deleteUser(String username) {
        String query = "UPDATE Users SET IsActive = 0 WHERE Username = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.executeUpdate();
            return true;
        } catch (SQLServerException e) {
            System.err.println("Delete user error - SQL Server: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Delete user error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Získá všechny koordinátory
     */
    public ObservableList<User> getCoordinators() {
        ObservableList<User> coordinators = FXCollections.observableArrayList();
        String query = "SELECT Username, Role, Email, FullName FROM Users WHERE (Role = 'coordinator' OR Role = 'admin') AND IsActive = 1";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User(
                        rs.getString("Username"),
                        rs.getString("Role"),
                        rs.getString("Email"),
                        rs.getString("FullName")
                );
                coordinators.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Get coordinators error: " + e.getMessage());
            e.printStackTrace();
        }
        return coordinators;
    }
}

