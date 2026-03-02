package model;

import java.time.LocalDateTime;

public class Booking {

    private String bookingId;
    private User user;
    private Events event;
    private LocalDateTime createdAt;
    private BookingStatus status;

    public Booking(String bookingId, User user, Events event) {
        this.bookingId = bookingId;
        this.user = user;
        this.event = event;
        this.createdAt = LocalDateTime.now();
        this.status = BookingStatus.CONFIRMED; // default, will adjust later
    }

    public String getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public Events getEvent() {
        return event;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}