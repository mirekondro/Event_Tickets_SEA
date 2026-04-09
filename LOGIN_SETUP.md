# ⚠️ DŮLEŽITÉ: Nastavení Databázového Připojení

## KROK 1: Najdi správné heslo SQL Serveru

Otevři soubor:
```
src/main/java/dk/easv/event_tickets_sea/db/DatabaseConnection.java
```

Na řádku 16 změň:
```java
private static final String PASSWORD = "yourPassword"; // ZMĚŇ TOTO!
```

Na tvé SQL Server heslo. Např:
```java
private static final String PASSWORD = "sa_password_123"; // Tvoje heslo
```

---

## KROK 2: Ověř nastavení v DatabaseConnection.java

```java
private static final String SERVER = "localhost";              // SQL Server adresa
private static final String DATABASE = "event_ticket_system_sea";  // Databáze jméno
private static final String USER = "sa";                       // SQL Server username
private static final String PASSWORD = "tvoje_heslo";          // ← MĚNÍŠ TADY!
private static final int PORT = 1433;                          // SQL Server port
```

### Kde najít SQL Server heslo?
1. Otevři **SQL Server Management Studio**
2. Pokud se píšeš heslo při připojování → to je ono
3. Nebo si ho můžeš resetovat přes SQL Server Configuration Manager

---

## KROK 3: Spusť aplikaci

```bash
cd /Users/miroslavondrousek/IdeaProjects/sc01E2025/Event_Tickets_SEA
mvn clean javafx:run
```

---

## KROK 4: Přihlášení

Pokud máš databázi vytvořenou ze skriptu `database_setup.sql`:

- **Username:** admin
- **Password:** admin123

---

## ❌ Pokud přihlášení stále nefunguje:

### 1. Zkontroluj, že databáze existuje
```sql
SELECT name FROM sys.databases WHERE name = 'event_ticket_system_sea';
```

### 2. Zkontroluj, že tabulka Users existuje
```sql
SELECT * FROM Users;
```

### 3. Zkontroluj, že admin uživatel existuje
```sql
SELECT * FROM Users WHERE Username = 'admin';
```

### 4. Pokud admin neexistuje, přidej ho:
```sql
INSERT INTO Users (Username, Password, Email, FullName, Role, IsActive)
VALUES ('admin', 'admin123', 'admin@easv.dk', 'Administrator', 'admin', 1);
```

### 5. Zkontroluj connection string
V konzoli by měla být chyba s SQL detaily. Přečti si ji pečlivě!

---

## 🔍 Debug: Kontrola logu chyb

Aplikace by měla vypsat chyby v konzoli. Hledej:
```
Login error: ...
```

Zkopíruj si tuto chybu a řeš jí podle toho.

---

## ✅ Když bude fungovat:

1. Aplikace začne vybírat data z databáze
2. Přihlášení bude ověřovat username a heslo z DB
3. Po přihlášení se otevře správný dashboard dle role

