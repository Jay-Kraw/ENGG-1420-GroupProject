package service;

import model.*;
import java.util.*;

public class BookingManager {

    // List storing all bookings in the system
    private List<Booking> bookings = new ArrayList<>();

    // NEW: List storing all events (needed for search/filter)
    private List<Events> events = new ArrayList<>();

    // Optional constructor if you want to pass events from main
    public BookingManager(List<Events> events) {
        this.events = events;
    }

    // Default constructor (if already used in your project)
    public BookingManager() {}

    public Booking createBooking(String bookingId, User user, Events event) {

        // Check if the event is cancelled before allowing booking
        if (event.isCancelled()) {
            throw new IllegalStateException("Event is cancelled.");
        }

        // Prevent duplicate bookings using IDs (safer)
        for (Booking b : bookings) {
            if (b.getUser().getUserId() == user.getUserId() &&
                    b.getEvent().getEventId() == event.getEventId() &&
                    b.getStatus() != BookingStatus.CANCELLED) {
                throw new IllegalStateException("User already booked this event.");
            }
        }

        // Count how many confirmed bookings the user already has
        int activeBookings = 0;
        for (Booking b : bookings) {
            if (b.getUser().equals(user) &&
                    b.getStatus() == BookingStatus.CONFIRMED) {
                activeBookings++;
            }
        }

        // Enforce booking limit based on user type
        if (activeBookings >= user.getMaxBookings()) {
            throw new IllegalStateException("User exceeded maximum confirmed bookings.");
        }

        // Create new booking object
        Booking booking = new Booking(bookingId, user, event);

        // If event is full → add to waitlist, otherwise confirm booking
        if (event.isFull()) {
            booking.setStatus(BookingStatus.WAITLISTED);
            event.addToWaitlist(booking);
        } else {
            booking.setStatus(BookingStatus.CONFIRMED);
            event.addConfirmedBooking(booking);
        }

        // Store booking in system
        bookings.add(booking);
        return booking;
    }

    public void cancelBooking(Booking booking) {

        // If already cancelled, do nothing
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return;
        }

        // Mark booking as cancelled
        booking.setStatus(BookingStatus.CANCELLED);

        Events event = booking.getEvent();

        // Remove booking from event's confirmed list
        event.removeBooking(booking);

        // Automatically promote the first user from waitlist (FIFO)
        if (!event.getWaitlist().isEmpty()) {
            Booking promoted = event.getWaitlist().poll();
            promoted.setStatus(BookingStatus.CONFIRMED);
            event.addConfirmedBooking(promoted);
        }
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }

    // ===============================
    // PHASE 2: SEARCH FUNCTIONALITY
    // ===============================

    // Returns all events (used for "All" filter)
    public List<Events> getAllEvents() {
        return events;
    }

    // Search events by title (partial + case-insensitive)
    public List<Events> searchEventsByTitle(String keyword) {
        List<Events> result = new ArrayList<>();

        // If search box is empty, return all events
        if (keyword == null || keyword.trim().isEmpty()) {
            return events;
        }

        for (Events e : events) {
            // Convert both to lowercase for case-insensitive comparison
            if (e.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(e);
            }
        }

        return result;
    }

    // Filter events based on type (Workshop, Seminar, Concert)
    public List<Events> filterEventsByType(String type) {
        List<Events> result = new ArrayList<>();

        for (Events e : events) {

            // Check event type using instanceof (clean OOP approach)
            if (type.equalsIgnoreCase("Workshop") && e instanceof Workshop) {
                result.add(e);
            }
            else if (type.equalsIgnoreCase("Seminar") && e instanceof Seminar) {
                result.add(e);
            }
            else if (type.equalsIgnoreCase("Concert") && e instanceof Concert) {
                result.add(e);
            }
        }

        return result;
    }
}