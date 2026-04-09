# 🔧 FIX pro Database Error - Vytvoření defaultních uživatelů

## Co je problém?
Když přidáváš event, aplikace hledá uživatele s username `admin` nebo `coordinator` v tabulce `Users`.
Pokud tam nejsou, insert selže → "Database Error".

## Řešení: Vložit uživatele do DB

Spusť tento SQL skript v **SQL Server Management Studio**:

```sql
USE event_ticket_system_sea;

-- Kontrola a vložení admin uživatele
IF NOT EXISTS (SELECT 1 FROM Users WHERE Username = 'admin')
BEGIN
    INSERT INTO Users (Username, Password, Email, FullName, Role, IsActive)
    VALUES ('admin', 'admin123', 'admin@easv.dk', 'Administrator', 'admin', 1);
    PRINT 'Admin user created';
END
ELSE
    PRINT 'Admin user already exists';

-- Kontrola a vložení coordinator uživatele
IF NOT EXISTS (SELECT 1 FROM Users WHERE Username = 'coordinator')
BEGIN
    INSERT INTO Users (Username, Password, Email, FullName, Role, IsActive)
    VALUES ('coordinator', 'coordinator123', 'coordinator@easv.dk', 'Coordinator User', 'coordinator', 1);
    PRINT 'Coordinator user created';
END
ELSE
    PRINT 'Coordinator user already exists';

-- Ověř že jsou tam uživatelé
SELECT UserId, Username, Role, IsActive FROM Users;
```

## Postup:
1. **Otevři SQL Server Management Studio**
2. **Připoj se na server** s `event_ticket_system_sea` databází
3. **Zkopíruj výše uvedený SQL skript**
4. **Vlepš do New Query okna**
5. **Klikni Execute (Ctrl+E)**
6. ✅ **Měl bys vidět "Admin user created" a "Coordinator user created"**

## Pak v aplikaci:
1. **Spusť aplikaci:**
   ```bash
   mvn clean javafx:run
   ```

2. **Login:**
   - Zadej: `admin` → půjdeš na **admin dashboard**
   - Zadej: cokoliv jiného (např. `coordinator`, `user`, atd.) → půjdeš na **coordinator dashboard**

3. **Přidej event:**
   - Měl by se uložit **bez "Database Error"** ✅

---

## Ověření v SQL:
```sql
-- Zkontroluj uživatele
SELECT * FROM Users;

-- Zkontroluj vytvořené eventy
SELECT * FROM Events;
```

---

Když je tohle hotovo, event se bude přidávat bez problému! 🚀

