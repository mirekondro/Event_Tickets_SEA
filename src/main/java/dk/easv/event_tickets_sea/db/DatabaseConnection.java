package dk.easv.event_tickets_sea.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton třída pro správu databázového připojení
 * Používá properties soubor pro konfiguraci
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private static final String PROP_FILE = "config/config.settings";
    private static String SERVER;
    private static String DATABASE;
    private static String USER;
    private static String PASSWORD;
    private static int PORT;

    static {
        try {
            Properties databaseProperties = new Properties();
            databaseProperties.load(new FileInputStream(PROP_FILE));

            SERVER = databaseProperties.getProperty("Server");
            DATABASE = databaseProperties.getProperty("Database");
            USER = databaseProperties.getProperty("User");
            PASSWORD = databaseProperties.getProperty("Password");
            PORT = Integer.parseInt(databaseProperties.getProperty("Port"));

            // Load driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (IOException e) {
            System.err.println("Failed to load database settings from " + PROP_FILE);
            e.printStackTrace();
            throw new RuntimeException("Failed to load database settings", e);
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load SQL Server JDBC driver");
            e.printStackTrace();
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
    }

    private DatabaseConnection() {
        // Private constructor for singleton
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        String connectionString = "jdbc:sqlserver://" + SERVER + ":" + PORT +
                ";databaseName=" + DATABASE +
                ";user=" + USER +
                ";password=" + PASSWORD +
                ";trustServerCertificate=true";

        return DriverManager.getConnection(connectionString);
    }

    public void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



