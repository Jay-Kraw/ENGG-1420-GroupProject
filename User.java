public class User {
    private String userId;
    private String name;
    private String email;
    private String type;

    public User(String userId, String name, String email, String type) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.type = type;
    }

    //Setters
    public void setUserId() {
        this.userId = userId;
    }

    public void setName() {
        this.name = name;
    }

    public void setEmail() {
        this.email = email;
    }

    public void setType() {
        this.type = type;
    }

    //Getters
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

    //Summary method
    public String getSummary() {
        return "User: " + name + "ID: " + userId + "(" + type + ")" + "Email: " + email;
    }
}