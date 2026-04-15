package dk.easv.event_tickets_sea.db;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object pro správu lístků
 */
public class TicketDAO {
    private static TicketDAO instance;
    private final DatabaseConnection dbConnection;
    private String lastErrorMessage;

    private TicketDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public static TicketDAO getInstance() {
        if (instance == null) {
            instance = new TicketDAO();
        }
        return instance;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    /**
     * Přidá nový typ lístku na akci
     */
    public boolean addTicket(String eventName, String ticketType, double price, int quantity) {
        String query = "INSERT INTO Tickets (EventId, TicketType, Price, QuantityAvailable) " +
                       "SELECT EventId, ?, ?, ? FROM Events WHERE EventName = ? AND IsActive = 1";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, ticketType);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            stmt.setString(4, eventName);

            stmt.executeUpdate();
            return true;
        } catch (SQLServerException e) {
            System.err.println("Add ticket error - SQL Server: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Add ticket error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Prodá lístek
     */
    public boolean sellTicket(String eventName, String ticketType, String buyerName, String buyerEmail, String soldByUsername) {
        return sellTicketAndGetCode(eventName, ticketType, buyerName, buyerEmail, soldByUsername) != null;
    }

    /**
     * Prodá lístek a vrátí ticket code pro email potvrzení
     */
    public String sellTicketAndGetCode(String eventName, String ticketType, String buyerName, String buyerEmail, String soldByUsername) {
        lastErrorMessage = null;
        try (Connection conn = dbConnection.getConnection()) {
            Integer ticketId = resolveTicketId(conn, eventName, ticketType);
            if (ticketId == null) {
                lastErrorMessage = "No available ticket for selected event/category.";
                return null;
            }
            return sellTicketByIdInternal(conn, ticketId, buyerName, buyerEmail, soldByUsername);
        } catch (SQLServerException e) {
            lastErrorMessage = "SQL Server error: " + e.getMessage();
            System.err.println("Sell ticket error - SQL Server: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            lastErrorMessage = "Database error: " + e.getMessage();
            System.err.println("Sell ticket error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Prodá lístek podle TicketId a vrátí ticket code.
     */
    public String sellTicketByIdAndGetCode(int ticketId, String buyerName, String buyerEmail, String soldByUsername) {
        lastErrorMessage = null;
        try (Connection conn = dbConnection.getConnection()) {
            return sellTicketByIdInternal(conn, ticketId, buyerName, buyerEmail, soldByUsername);
        } catch (SQLServerException e) {
            lastErrorMessage = "SQL Server error: " + e.getMessage();
            System.err.println("Sell ticket by id error - SQL Server: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            lastErrorMessage = "Database error: " + e.getMessage();
            System.err.println("Sell ticket by id error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private Integer resolveTicketId(Connection conn, String eventName, String ticketType) throws SQLException {
        String ticketQuery = "SELECT TOP 1 t.TicketId FROM Tickets t " +
                "JOIN Events e ON t.EventId = e.EventId " +
                "WHERE e.EventName = ? AND t.TicketType = ? AND t.IsActive = 1 AND t.QuantitySold < t.QuantityAvailable " +
                "ORDER BY t.TicketId";

        try (PreparedStatement stmt = conn.prepareStatement(ticketQuery)) {
            stmt.setString(1, eventName);
            stmt.setString(2, ticketType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("TicketId");
                }
            }
        }
        return null;
    }

    private Integer resolveSoldById(Connection conn, String soldByUsername) throws SQLException {
        String soldByQuery = "SELECT COALESCE((SELECT UserId FROM Users WHERE Username = ? AND IsActive = 1), " +
                "(SELECT TOP 1 UserId FROM Users WHERE IsActive = 1 ORDER BY UserId)) AS SoldById";

        try (PreparedStatement stmt = conn.prepareStatement(soldByQuery)) {
            stmt.setString(1, soldByUsername);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int soldById = rs.getInt("SoldById");
                    return rs.wasNull() ? null : soldById;
                }
            }
        }
        return null;
    }

    private String sellTicketByIdInternal(Connection conn, int ticketId, String buyerName, String buyerEmail, String soldByUsername) throws SQLException {
        Integer soldById = resolveSoldById(conn, soldByUsername);
        if (soldById == null) {
            lastErrorMessage = "No active seller user found in database.";
            return null;
        }

        String ticketCode = generateTicketCode();
        String call = "{call sp_SellTicket(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(call)) {
            stmt.setInt(1, ticketId);
            stmt.setString(2, buyerName);
            stmt.setString(3, buyerEmail);
            stmt.setInt(4, soldById);
            stmt.setString(5, ticketCode);
            stmt.execute();
        }
        return ticketCode;
    }

    /**
     * Získá lístky pro akci
     */
    public ObservableList<Map<String, Object>> getTicketsByEvent(String eventName) {
        ObservableList<Map<String, Object>> tickets = FXCollections.observableArrayList();
        String query = "SELECT t.TicketId, t.TicketType, t.Price, t.QuantityAvailable, t.QuantitySold " +
                       "FROM Tickets t " +
                       "JOIN Events e ON t.EventId = e.EventId " +
                       "WHERE e.EventName = ? AND t.IsActive = 1";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, eventName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> ticket = new HashMap<>();
                ticket.put("TicketId", rs.getInt("TicketId"));
                ticket.put("TicketType", rs.getString("TicketType"));
                ticket.put("Price", rs.getDouble("Price"));
                ticket.put("QuantityAvailable", rs.getInt("QuantityAvailable"));
                ticket.put("QuantitySold", rs.getInt("QuantitySold"));
                tickets.add(ticket);
            }
        } catch (SQLServerException e) {
            System.err.println("Get tickets by event error - SQL Server: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Get tickets by event error: " + e.getMessage());
            e.printStackTrace();
        }
        return tickets;
    }

    /**
     * Získá prodané lístky
     */
    public ObservableList<Map<String, Object>> getSoldTickets() {
        ObservableList<Map<String, Object>> soldTickets = FXCollections.observableArrayList();
        String query = "SELECT st.SoldTicketId, st.TicketCode, st.BuyerName, st.BuyerEmail, t.TicketType, t.Price, st.SaleDate, st.IsRedeemed " +
                       "FROM SoldTickets st " +
                       "JOIN Tickets t ON st.TicketId = t.TicketId " +
                       "ORDER BY st.SaleDate DESC";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Map<String, Object> ticket = new HashMap<>();
                ticket.put("SoldTicketId", rs.getInt("SoldTicketId"));
                ticket.put("TicketCode", rs.getString("TicketCode"));
                ticket.put("BuyerName", rs.getString("BuyerName"));
                ticket.put("BuyerEmail", rs.getString("BuyerEmail"));
                ticket.put("TicketType", rs.getString("TicketType"));
                ticket.put("Price", rs.getDouble("Price"));
                ticket.put("SaleDate", rs.getTimestamp("SaleDate"));
                ticket.put("IsRedeemed", rs.getBoolean("IsRedeemed"));
                soldTickets.add(ticket);
            }
        } catch (SQLServerException e) {
            System.err.println("Get sold tickets error - SQL Server: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Get sold tickets error: " + e.getMessage());
            e.printStackTrace();
        }
        return soldTickets;
    }

    /**
     * Vyřadí lístek (označí jako vybrán)
     */
    public boolean redeemTicket(String ticketCode, String redeemedByUsername) {
        String query = "UPDATE SoldTickets SET IsRedeemed = 1, RedeemedAt = GETDATE(), " +
                       "RedeemedBy = (SELECT UserId FROM Users WHERE Username = ?) " +
                       "WHERE TicketCode = ? AND IsRedeemed = 0";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, redeemedByUsername);
            stmt.setString(2, ticketCode);

            stmt.executeUpdate();
            return true;
        } catch (SQLServerException e) {
            System.err.println("Redeem ticket error - SQL Server: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Redeem ticket error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Vrátí počet dostupných lístků pro konkrétní typ
     */
    public int getAvailableTickets(String eventName, String ticketType) {
        String query = "SELECT (t.QuantityAvailable - t.QuantitySold) as Available " +
                       "FROM Tickets t " +
                       "JOIN Events e ON t.EventId = e.EventId " +
                       "WHERE e.EventName = ? AND t.TicketType = ? AND t.IsActive = 1";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, eventName);
            stmt.setString(2, ticketType);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Available");
            }
        } catch (SQLServerException e) {
            System.err.println("Get available tickets error - SQL Server: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Get available tickets error: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Vygeneruje jedinečný kód lístku
     */
    private String generateTicketCode() {
        return "TKT-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 10000);
    }
}

