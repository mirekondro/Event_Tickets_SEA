package dk.easv.event_tickets_sea.db;

import dk.easv.event_tickets_sea.model.User;
import dk.easv.event_tickets_sea.model.Event;
import dk.easv.event_tickets_sea.util.UserManager;
import dk.easv.event_tickets_sea.util.EventManager;
import javafx.collections.ObservableList;
import java.util.Map;

/**
 * EXAMPLES - How to use DAOs
 * Příklady - Jak používat DAO třídy
 */
public class DAOUsageExamples {

    /**
     * USER MANAGEMENT EXAMPLES
     */
    @SuppressWarnings("unused")
    public static void userManagementExamples() {
        @SuppressWarnings("unused")
        UserDAO userDAO = UserDAO.getInstance();

        // 1. Login verification / Ověření přihlášení
        // User user = userDAO.verifyLogin("admin", "admin123");
        // if (user != null) {
        //     System.out.println("Login successful: " + user.getFullName());
        // }

        // 2. Add new user / Přidej nového uživatele
        // boolean success = userDAO.addUser(
        //     "johndoe",
        //     "password123",
        //     "john@example.com",
        //     "John Doe",
        //     "coordinator"
        // );

        // 3. Get all users / Načti všechny uživatele
        // ObservableList<User> users = userDAO.getAllUsers();
        // users.forEach(u -> System.out.println(u.getFullName()));

        // 4. Get user by username / Načti uživatele dle jména
        // User user = userDAO.getUserByUsername("admin");

        // 5. Update user / Aktualizuj uživatele
        // userDAO.updateUser("johndoe", "john.new@example.com", "John New Name");

        // 6. Delete user (deactivate) / Smaz uživatele (deaktivuj)
        // userDAO.deleteUser("johndoe");

        // 7. Get coordinators / Načti všechny koordinátory
        // ObservableList<User> coordinators = userDAO.getCoordinators();
    }

    /**
     * EVENT MANAGEMENT EXAMPLES
     */
    @SuppressWarnings("unused")
    public static void eventManagementExamples() {
        @SuppressWarnings("unused")
        EventDAO eventDAO = EventDAO.getInstance();

        // 1. Add new event / Přidej novou akci
        // boolean success = eventDAO.addEvent(
        //     "Tech Conference 2025",
        //     LocalDate.of(2025, 5, 10),
        //     LocalTime.of(9, 0),
        //     LocalDate.of(2025, 5, 12),
        //     LocalTime.of(17, 0),
        //     "Main Hall",
        //     "Underground parking available",
        //     "3 days of innovation",
        //     "admin"  // coordinator username
        // );

        // 2. Get all events / Načti všechny akce
        // ObservableList<Event> events = eventDAO.getAllEvents();
        // events.forEach(e -> System.out.println(e.getEventName()));

        // 3. Get events by coordinator / Načti akce konkrétního koordinátora
        // ObservableList<Event> coordEvents = eventDAO.getEventsByCoordinator("admin");

        // 4. Update event / Aktualizuj akci
        // boolean success = eventDAO.updateEvent(
        //     "Tech Conference 2025",
        //     LocalDate.of(2025, 5, 10),
        //     LocalTime.of(9, 0),
        //     LocalDate.of(2025, 5, 12),
        //     LocalTime.of(18, 0),  // updated end time
        //     "Main Hall",
        //     "Underground parking available",
        //     "Updated notes"
        // );

        // 5. Delete event (deactivate) / Smaz akci (deaktivuj)
        // boolean success = eventDAO.deleteEvent("Tech Conference 2025");
    }

    /**
     * TICKET MANAGEMENT EXAMPLES
     */
    @SuppressWarnings("unused")
    public static void ticketManagementExamples() {
        @SuppressWarnings("unused")
        TicketDAO ticketDAO = TicketDAO.getInstance();

        // 1. Add new ticket type / Přidej nový typ lístku
        // boolean success = ticketDAO.addTicket(
        //     "Tech Conference 2025",  // event name
        //     "VIP Pass",              // ticket type
        //     199.99,                  // price
        //     100                      // quantity available
        // );

        // 2. Sell ticket / Prodej lístek
        // boolean success = ticketDAO.sellTicket(
        //     "Tech Conference 2025",
        //     "VIP Pass",
        //     "Jane Smith",
        //     "jane@example.com",
        //     "admin"  // sold by username
        // );

        // 3. Get tickets for event / Načti lístky pro akci
        // ObservableList<Map<String, Object>> tickets = ticketDAO.getTicketsByEvent("Tech Conference 2025");
        // tickets.forEach(t -> {
        //     System.out.println("Type: " + t.get("TicketType"));
        //     System.out.println("Price: " + t.get("Price"));
        //     System.out.println("Available: " + (Integer)t.get("QuantityAvailable") - (Integer)t.get("QuantitySold"));
        // });

        // 4. Get sold tickets / Načti prodané lístky
        // ObservableList<Map<String, Object>> soldTickets = ticketDAO.getSoldTickets();

        // 5. Redeem ticket / Vyřaď lístek (používá se na místě akce)
        // boolean success = ticketDAO.redeemTicket("TKT-1234567890-5678", "admin");

        // 6. Get available tickets count / Počet dostupných lístků
        // int available = ticketDAO.getAvailableTickets("Tech Conference 2025", "VIP Pass");
    }

    /**
     * INTEGRATION WITH MANAGERS EXAMPLE
     */
    @SuppressWarnings("unused")
    public static void managerIntegrationExamples() {
        // UserManager - nový interface s databází
        @SuppressWarnings("unused")
        UserManager userManager = UserManager.getInstance();

        // 1. Login / Přihlášení
        // if (userManager.login("admin", "admin123")) {
        //     User currentUser = userManager.getLoggedInUser();
        //     System.out.println("Logged in as: " + currentUser.getFullName());
        // }

        // 2. Get all users / Načti všechny uživatele
        // ObservableList<User> users = userManager.getUsers();

        // 3. Add new user / Přidej nového uživatele
        // userManager.addUser("newuser", "password", "new@example.com", "New User", "coordinator");

        // 4. Logout / Odhlášení
        // userManager.logout();

        // EventManager - nový interface s databází
        @SuppressWarnings("unused")
        EventManager eventManager = EventManager.getInstance();

        // 1. Get all events / Načti všechny akce
        // ObservableList<Event> events = eventManager.getEvents();

        // 2. Get events by coordinator / Načti akce pro konkrétního koordinátora
        // String coordinatorUsername = userManager.getLoggedInUser().getUsername();
        // ObservableList<Event> myEvents = eventManager.getEventsByCoordinator(coordinatorUsername);

        // 3. Add new event / Přidej novou akci
        // eventManager.addEvent(
        //     "New Event",
        //     LocalDate.now(),
        //     LocalTime.now(),
        //     LocalDate.now().plusDays(1),
        //     LocalTime.now(),
        //     "Location",
        //     "Guidance",
        //     "Notes",
        //     coordinatorUsername
        // );
    }

    /**
     * DATABASE CONNECTION EXAMPLE
     */
    @SuppressWarnings("unused")
    public static void databaseConnectionExample() {
        @SuppressWarnings("unused")
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();

        try {
            // Get connection / Získej připojení
            // Connection conn = dbConnection.getConnection();
            // System.out.println("Connected to database!");

            // Connection is automatically managed
            // Připojení se automaticky spravuje

        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    /**
     * COMPLETE WORKFLOW EXAMPLE
     * Kompletní pracovní postup
     */
    @SuppressWarnings("unused")
    public static void completeWorkflowExample() {
        try {
            // 1. Login / Přihlášení
            UserManager userManager = UserManager.getInstance();
            if (!userManager.login("admin", "admin123")) {
                System.out.println("Login failed");
                return;
            }
            System.out.println("Logged in as: " + userManager.getLoggedInUser().getFullName());

            // 2. Create new event / Vytvoř novou akci
            EventManager eventManager = EventManager.getInstance();
            String coordinatorUsername = userManager.getLoggedInUser().getUsername();

            eventManager.addEvent(
                "Annual Meeting 2025",
                java.time.LocalDate.of(2025, 6, 15),
                java.time.LocalTime.of(14, 0),
                java.time.LocalDate.of(2025, 6, 15),
                java.time.LocalTime.of(17, 0),
                "Conference Room A",
                "Building 3",
                "Important annual meeting",
                coordinatorUsername
            );
            System.out.println("Event created successfully!");

            // 3. Add tickets for event / Přidej lístky na akci
            TicketDAO ticketDAO = TicketDAO.getInstance();
            ticketDAO.addTicket("Annual Meeting 2025", "Standard", 0.0, 150);
            ticketDAO.addTicket("Annual Meeting 2025", "VIP", 50.0, 30);
            System.out.println("Tickets added!");

            // 4. Sell some tickets / Prodej pár lístků
            ticketDAO.sellTicket("Annual Meeting 2025", "Standard", "John Doe", "john@example.com", coordinatorUsername);
            ticketDAO.sellTicket("Annual Meeting 2025", "VIP", "Jane Smith", "jane@example.com", coordinatorUsername);
            System.out.println("Tickets sold!");

            // 5. Get all events / Načti všechny akce
            eventManager.getEvents().forEach(e ->
                System.out.println("- " + e.getEventName() + " on " + e.getStartDate())
            );

            // 6. Logout / Odhlášení
            userManager.logout();
            System.out.println("Logged out");

        } catch (Exception e) {
            System.out.println("Error in workflow: " + e.getMessage());
            // Log error appropriately - printStackTrace is replaced with logging
            System.err.println("Workflow error: " + e);
        }
    }
}

