package model;

public class User {

    private String userId;
    private String name;
    private String email;
    private String type; // Student, Staff, Guest

    public User(String userId, String name, String email, String type) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.type = type;
    }

    // -------- Setters --------
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type) {
        this.type = type;
    }

    // -------- Getters --------
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    // -------- Booking Limit (Required by Handout) --------
    public int getMaxBookings() {

        switch (type.toLowerCase()) {
            case "student":
                return 3;
            case "staff":
                return 5;
            case "guest":
                return 1;
            default:
                return 0;
        }
    }

    // -------- Summary --------
    public String getSummary() {
        return "User: " + name +
                " | ID: " + userId +
                " | Type: " + type +
                " | Email: " + email;
    }
    @Override
    public String toString() {
        return name;
    }
}