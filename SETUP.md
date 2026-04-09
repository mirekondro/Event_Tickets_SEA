# Event Tickets SEA - Database Integration Setup

## Quick Start / Rychlý start

### Step 1: Create Database / Krok 1: Vytvoř databázi
1. Otevři **SQL Server Management Studio**
2. Spusť skript: `database_setup.sql` (Ctrl+Shift+E)
3. Databáze se vytvoří s názvem: `event_ticket_system_sea`

### Step 2: Update Database Connection / Krok 2: Aktualizuj připojení
Otevři soubor: `src/main/java/dk/easv/event_tickets_sea/db/DatabaseConnection.java`

Změň tyto údaje:
```java
private static final String SERVER = "localhost";
private static final String DATABASE = "event_ticket_system_sea";
private static final String USER = "sa";
private static final String PASSWORD = "tvoje_heslo_k_sql_serveru";
private static final int PORT = 1433;
```

### Step 3: Run Application / Krok 3: Spusť aplikaci
```bash
mvn clean javafx:run
```

### Step 4: Login / Krok 4: Přihlášení
- **Username:** admin
- **Password:** admin123

---

## Project Structure / Struktura projektu

```
src/main/java/dk/easv/event_tickets_sea/
├── db/
│   ├── DatabaseConnection.java  ← Database connection manager
│   ├── UserDAO.java            ← User database operations
│   ├── EventDAO.java           ← Event database operations
│   └── TicketDAO.java          ← Ticket database operations
├── util/
│   ├── UserManager.java        ← User management (now with DB)
│   └── EventManager.java       ← Event management (now with DB)
├── model/
│   ├── User.java
│   ├── Event.java
│   └── Ticket.java (pro budoucnost)
└── gui/
    ├── LoginController.java    ← Updated for DB authentication
    ├── AdminController.java
    ├── CoordinatorController.java
    ├── EventFormController.java ← Updated for DB
    ├── AddUserController.java   ← Updated for DB
    └── ... ostatní controllery
```

---

## Key Features / Klíčové funkce

✅ **All data is now stored in database** - Všechna data se teď ukládají v databázi
✅ **Login verification from database** - Ověřování přihlášení z databáze
✅ **Add/Edit/Delete events in DB** - Přidat/Upravit/Odstranit akce v DB
✅ **Add/Edit/Delete users in DB** - Přidat/Upravit/Odstranit uživatele v DB
✅ **Ticket management from DB** - Správa lístků z databáze
✅ **Audit logging** - Audit záznam akcí

---

## Database Schema / Databázové tabulky

- **Users** - ID, Username, Password, Email, FullName, Role, CreatedAt
- **Events** - ID, EventName, StartDate, StartTime, EndDate, EndTime, Location, Coordinator, etc.
- **Tickets** - ID, EventId, TicketType, Price, QuantityAvailable, QuantitySold
- **SoldTickets** - ID, TicketId, BuyerInfo, SaleDate, TicketCode, IsRedeemed
- **Vouchers** - ID, EventId, Code, Value, CreatedBy, RedeemedAt
- **AuditLog** - ID, UserId, Action, TableName, RecordId, ActionDate

---

## Troubleshooting / Řešení problémů

### "package java.sql is not visible"
✅ Vyřešeno - aktualizován `module-info.java`

### "Cannot connect to database"
- Zkontroluj SQL Server je běžící
- Zkontroluj connection string v `DatabaseConnection.java`
- Zkontroluj username/password

### "Username or password" incorrect
- Default login: `admin` / `admin123`
- Ověř, že se databáze vytvořila z SQL skriptu

---

## Next Steps / Další kroky

1. ✅ Database created
2. ✅ Connection configured
3. ✅ DAOs implemented
4. ⏳ Frontend updated to use database
5. ⏳ Modern UI design (pending)
6. ⏳ Events page for users (pending)
7. ⏳ Ticket buying system (pending)

---

## Support Files / Podpůrné soubory

- `database_setup.sql` - SQL skript pro vytvoření databáze
- `DATABASE_CONFIG.md` - Detailní konfigurace
- `pom.xml` - Maven configuration s JDBC driverem

Hotovo! 🎉 Teď si jenom změň heslo SQL Serveru v souboru `DatabaseConnection.java` a spusť aplikaci!

