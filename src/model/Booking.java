package model;

import java.time.LocalDateTime;

public class Booking {
    private String bookingId;
    private String userId;
    private String eventId;
    private LocalDateTime createdAt;
    private BookingStatus status;
    private User user;
    private Events event;

    // Constructor used by BookingManager
    public Booking(String bookingId, User user, Events event,
                   LocalDateTime createdAt, BookingStatus status) {
        this.bookingId = bookingId;
        this.user = user;
        this.userId = (user != null) ? user.getUserId() : null;
        this.event = event;
        this.eventId = (event != null) ? event.getEventId() : null;
        this.createdAt = createdAt;
        this.status = status;
    }

    // Getters
    public String getBookingId() { return bookingId; }
    public String getUserId() { return userId; }
    public String getEventId() { return eventId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public BookingStatus getStatus() { return status; }
    public User getUser() { return user; }
    public Events getEvent() { return event; }

    // Setters
    public void setStatus(BookingStatus status) { this.status = status; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}