package dk.easv.event_tickets_sea.db;

import dk.easv.event_tickets_sea.model.Voucher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.UUID;

public class VoucherDAO {
    private static VoucherDAO instance;
    private final DatabaseConnection dbConnection;

    private VoucherDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public static VoucherDAO getInstance() {
        if (instance == null) {
            instance = new VoucherDAO();
        }
        return instance;
    }

    /**
     * Creates one or more vouchers and returns the generated codes.
     * eventName is optional — pass null for "valid for all events".
     */
    public ObservableList<String> createVouchers(String description, String eventName, String createdByUsername, int quantity) {
        ObservableList<String> codes = FXCollections.observableArrayList();

        String query;
        if (eventName != null && !eventName.isEmpty()) {
            query = "INSERT INTO Vouchers (Code, Description, EventId, CreatedBy) " +
                    "SELECT ?, ?, e.EventId, u.UserId FROM Events e, Users u " +
                    "WHERE e.EventName = ? AND u.Username = ?";
        } else {
            query = "INSERT INTO Vouchers (Code, Description, EventId, CreatedBy) " +
                    "SELECT ?, ?, NULL, u.UserId FROM Users u WHERE u.Username = ?";
        }

        try (Connection conn = dbConnection.getConnection()) {
            for (int i = 0; i < quantity; i++) {
                String code = "VCH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, code);
                    stmt.setString(2, description);
                    if (eventName != null && !eventName.isEmpty()) {
                        stmt.setString(3, eventName);
                        stmt.setString(4, createdByUsername);
                    } else {
                        stmt.setString(3, createdByUsername);
                    }
                    stmt.executeUpdate();
                    codes.add(code);
                }
            }
        } catch (SQLException e) {
            System.err.println("Create vouchers error: " + e.getMessage());
            e.printStackTrace();
        }
        return codes;
    }

    /** Get all vouchers for display */
    public ObservableList<Voucher> getAllVouchers() {
        ObservableList<Voucher> vouchers = FXCollections.observableArrayList();
        String query = "SELECT v.VoucherId, v.Code, v.Description, e.EventName, u.Username, " +
                "CASE WHEN v.RedeemedAt IS NOT NULL THEN 1 ELSE 0 END as IsRedeemed " +
                "FROM Vouchers v " +
                "LEFT JOIN Events e ON v.EventId = e.EventId " +
                "LEFT JOIN Users u ON v.CreatedBy = u.UserId " +
                "ORDER BY v.VoucherId DESC";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                vouchers.add(new Voucher(
                        rs.getInt("VoucherId"),
                        rs.getString("Code"),
                        rs.getString("Description"),
                        rs.getString("EventName"),
                        rs.getString("Username"),
                        rs.getBoolean("IsRedeemed")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Get all vouchers error: " + e.getMessage());
        }
        return vouchers;
    }
}
