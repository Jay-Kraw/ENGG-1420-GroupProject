package model;

public class Guest extends User {
    private String guestCode;
    private String expiryDate;

    public Guest(String userId, String name, String email, String guestCode, String expiryDate) {
        super(userId, name, email, "model.Guest");
        this.guestCode = guestCode;
        this.expiryDate = expiryDate;
    }

    //Setters
    public void setGuestCode() {
        this.guestCode = guestCode;
    }

    public void setExpiryDate() {
        this.expiryDate = expiryDate;
    }

    //Getters
    public String getGuestCode() {
        return guestCode;
    }
    public String getExpiryDate() {
        return expiryDate;
    }
}