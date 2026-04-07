package dk.easv.event_tickets_sea.util;

import dk.easv.event_tickets_sea.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserManager {
    private static UserManager instance;
    private ObservableList<User> users;

    private UserManager() {
        users = FXCollections.observableArrayList();

        // Add some sample users
        users.add(new User("johndoe", "Event Coordinator", "john@easv.dk", "John Doe"));
        users.add(new User("janesmith", "Event Coordinator", "jane@easv.dk", "Jane Smith"));
        users.add(new User("admin", "Admin", "admin@easv.dk", "Admin User"));
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }
}