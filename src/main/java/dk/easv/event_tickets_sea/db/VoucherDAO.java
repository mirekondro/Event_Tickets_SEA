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
     * Creates vouchers and returns the generated codes.
     * eventName is optional — pass null for "valid for all events".
     */
    public ObservableList<String> createVouchers(String description, String eventName,
                                                 String createdByUsername, int quantity) {
        ObservableList<String> codes = FXCollections.observableArrayList();

        try (Connection conn = dbConnection.getConnection()) {
            for (int i = 0; i < quantity; i++) {
                String code = "VCH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

                String query;
                PreparedStatement stmt;

                if (eventName != null && !eventName.isEmpty()) {
                    // Linked to a specific event
                    query = "INSERT INTO dbo.Vouchers (VoucherCode, VoucherDescription, EventId, CreatedBy) " +
                            "SELECT ?, ?, e.EventId, u.UserId FROM Events e, Users u " +
                            "WHERE e.EventName = ? AND u.Username = ? AND e.IsActive = 1 AND u.IsActive = 1";
                    stmt = conn.prepareStatement(query);
                    stmt.setString(1, code);
                    stmt.setString(2, description);
                    stmt.setString(3, eventName);
                    stmt.setString(4, createdByUsername);
                } else {
                    // Valid for all events — EventId is NULL
                    query = "INSERT INTO dbo.Vouchers (VoucherCode, VoucherDescription, EventId, CreatedBy) " +
                            "SELECT ?, ?, NULL, u.UserId FROM Users u " +
                            "WHERE u.Username = ? AND u.IsActive = 1";
                    stmt = conn.prepareStatement(query);
                    stmt.setString(1, code);
                    stmt.setString(2, description);
                    stmt.setString(3, createdByUsername);
                }

                int rows = stmt.executeUpdate();
                stmt.close();

                if (rows > 0) {
                    codes.add(code);
                } else {
                    System.err.println("VoucherDAO: Insert returned 0 rows for code " + code);
                }
            }
        } catch (SQLException e) {
            System.err.println("Create vouchers error: " + e.getMessage());
            e.printStackTrace();
        }
        return codes;
    }

    /** Get all active vouchers */
    public ObservableList<Voucher> getAllVouchers() {
        ObservableList<Voucher> vouchers = FXCollections.observableArrayList();
        String query = "SELECT v.VoucherId, v.VoucherCode, v.VoucherDescription, " +
                "e.EventName, u.Username, v.IsRedeemed " +
                "FROM dbo.Vouchers v " +
                "LEFT JOIN Events e ON v.EventId = e.EventId " +
                "LEFT JOIN Users u ON v.CreatedBy = u.UserId " +
                "WHERE v.IsActive = 1 " +
                "ORDER BY v.VoucherId DESC";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                vouchers.add(new Voucher(
                        rs.getInt("VoucherId"),
                        rs.getString("VoucherCode"),
                        rs.getString("VoucherDescription"),
                        rs.getString("EventName"),   // null if all events
                        rs.getString("Username"),
                        rs.getBoolean("IsRedeemed")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Get all vouchers error: " + e.getMessage());
            e.printStackTrace();
        }
        return vouchers;
    }
}
