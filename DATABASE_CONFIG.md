# Database Configuration Guide / Průvodce konfigurací databáze

## 1. Database Setup / Nastavení databáze

### Create Database / Vytvoř databázi
1. Open SQL Server Management Studio
2. Run the script from: `database_setup.sql`
3. Database name: `event_ticket_system_sea`

### Initial Credentials / Výchozí přihlášení
- **Username:** admin
- **Password:** admin123

## 2. JDBC Connection Configuration / Konfigurace připojení JDBC

Edit the file: `src/main/java/dk/easv/event_tickets_sea/db/DatabaseConnection.java`

```java
private static final String SERVER = "localhost";      // Your SQL Server address
private static final String DATABASE = "event_ticket_system_sea";  // Database name
private static final String USER = "sa";              // SQL Server username
private static final String PASSWORD = "yourPassword"; // SQL Server password
private static final int PORT = 1433;                 // Default SQL Server port
```

### Default Configuration / Výchozí konfigurace:
```
Server: localhost
Port: 1433
Database: event_ticket_system_sea
User: sa
Password: [YOUR_SQL_SERVER_PASSWORD]
```

## 3. Running the Application / Spuštění aplikace

```bash
cd /Users/miroslavondrousek/IdeaProjects/sc01E2025/Event_Tickets_SEA
mvn clean javafx:run
```

## 4. Database Entities / Databázové tabulky

- **Users** - Uživatelé (admin, coordinator, user)
- **Events** - Akce/Události
- **EventCoCoordinators** - Spolukoordinátátoři
- **Tickets** - Lístky
- **SoldTickets** - Prodané lístky
- **Vouchers** - Vouchery
- **AuditLog** - Audit záznam

## 5. Available DAO Classes / Dostupné DAO třídy

- `UserDAO` - Správa uživatelů
- `EventDAO` - Správa akcí
- `TicketDAO` - Správa lístků
- `DatabaseConnection` - Správa připojení

## 6. Usage Examples / Příklady použití

### Login User / Přihlášení
```java
UserManager userManager = UserManager.getInstance();
if (userManager.login("admin", "admin123")) {
    User user = userManager.getLoggedInUser();
}
```

### Get All Events / Načíst všechny akce
```java
EventManager eventManager = EventManager.getInstance();
ObservableList<Event> events = eventManager.getEvents();
```

### Add New Event / Přidat novou akci
```java
eventManager.addEvent(
    "Event Name",
    LocalDate.of(2025, 4, 15),
    LocalTime.of(20, 0),
    LocalDate.of(2025, 4, 16),
    LocalTime.of(2, 0),
    "Location",
    "Guidance",
    "Notes",
    "coordinator_username"
);
```

### Sell Ticket / Prodat lístek
```java
TicketDAO ticketDAO = TicketDAO.getInstance();
ticketDAO.sellTicket(
    "Event Name",
    "Ticket Type",
    "Buyer Name",
    "buyer@email.com",
    "seller_username"
);
```

