package dk.easv.event_tickets_sea.model;

public class User {
    private String username;
    private String role;
    private String email;
    private String fullName;

    public User(String username, String role, String email, String fullName) {
        this.username = username;
        this.role = role;
        this.email = email;
        this.fullName = fullName;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
