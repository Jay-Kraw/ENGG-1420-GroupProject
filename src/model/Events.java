package model;

import java.util.*;

public class Events {

    private String eventId;
    private String title;
    private String dateTime;
    private String location;
    private int capacity;
    private Boolean status;   // true = Active, false = Cancelled

    // Booking management lists (required for workflow)
    protected List<Booking> confirmedBookings = new ArrayList<>();
    protected Queue<Booking> waitlist = new LinkedList<>();

    public Events(String eventId,String title, String dateTime, String location, int capacity, boolean status) {
        this.eventId = eventId;
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.capacity = capacity;
        this.status = status;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    // -------- Booking Workflow Methods (Required by Phase 1) --------

    public boolean isFull() {
        return confirmedBookings.size() >= capacity;
    }

    public boolean isCancelled() {
        return !status;
    }

    public void cancelEvent() {
        status = false;

        // When event is cancelled:
        // All confirmed + waitlisted bookings become cancelled
        for (Booking b : confirmedBookings) {
            b.setStatus(BookingStatus.CANCELLED);
        }

        for (Booking b : waitlist) {
            b.setStatus(BookingStatus.CANCELLED);
        }

        confirmedBookings.clear();
        waitlist.clear();
    }

    public void addConfirmedBooking(Booking booking) {
        confirmedBookings.add(booking);
    }

    public void addToWaitlist(Booking booking) {
        waitlist.add(booking);
    }

    public void removeBooking(Booking booking) {
        confirmedBookings.remove(booking);
        waitlist.remove(booking);
    }

    public List<Booking> getConfirmedBookings() {
        return confirmedBookings;
    }

    public Queue<Booking> getWaitlist() {
        return waitlist;
    }
    @Override
    public String toString() {
        return title;
    }
}