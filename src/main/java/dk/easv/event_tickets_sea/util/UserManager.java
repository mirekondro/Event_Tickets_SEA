package dk.easv.event_tickets_sea.util;

import dk.easv.event_tickets_sea.db.UserDAO;
import dk.easv.event_tickets_sea.model.User;
import javafx.collections.ObservableList;

public class UserManager {
    private static UserManager instance;
    private final UserDAO userDAO;
    private User loggedInUser;

    private UserManager() {
        this.userDAO = UserDAO.getInstance();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    /**
     * Ověří přihlášení uživatele
     */
    public boolean login(String username, String password) {
        User user = userDAO.verifyLogin(username, password);
        if (user != null) {
            this.loggedInUser = user;
            return true;
        }
        return false;
    }

    /**
     * Vrátí aktuálně přihlášeného uživatele
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Nastaví přihlášeného uživatele
     */
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    /**
     * Odhlásí uživatele
     */
    public void logout() {
        this.loggedInUser = null;
    }

    /**
     * Získá všechny uživatele z databáze
     */
    public ObservableList<User> getUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Přidá nového uživatele
     */
    public boolean addUser(String username, String password, String email, String fullName, String role) {
        return userDAO.addUser(username, password, email, fullName, role);
    }

    /**
     * Odstraní uživatele
     */
    public void removeUser(User user) {
        userDAO.deleteUser(user.getUsername());
    }

    /**
     * Získá všechny koordinátory
     */
    public ObservableList<User> getCoordinators() {
        return userDAO.getCoordinators();
    }
}