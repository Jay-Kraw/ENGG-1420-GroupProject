package service;

import model.*;
import java.util.*;

public class BookingManager {

    private List<Booking> bookings = new ArrayList<>();

    public Booking createBooking(String bookingId, User user, Events event) {

        // 1️⃣ Event cancelled check
        if (event.isCancelled()) {
            throw new IllegalStateException("Event is cancelled.");
        }

        // 2️⃣ Duplicate booking check
        for (Booking b : bookings) {
            if (b.getUser().equals(user) &&
                    b.getEvent().equals(event) &&
                    b.getStatus() != BookingStatus.CANCELLED) {
                throw new IllegalStateException("User already booked this event.");
            }
        }

        // 3️⃣ Booking limit by user type
        int activeBookings = 0;
        for (Booking b : bookings) {
            if (b.getUser().equals(user) &&
                    b.getStatus() == BookingStatus.CONFIRMED) {
                activeBookings++;
            }
        }

        if (activeBookings >= user.getMaxBookings()) {
            throw new IllegalStateException("User exceeded maximum confirmed bookings.");
        }

        Booking booking = new Booking(bookingId, user, event);

        // 4️⃣ Capacity check
        if (event.isFull()) {
            booking.setStatus(BookingStatus.WAITLISTED);
            event.addToWaitlist(booking);
        } else {
            booking.setStatus(BookingStatus.CONFIRMED);
            event.addConfirmedBooking(booking);
        }

        bookings.add(booking);
        return booking;
    }

    public void cancelBooking(Booking booking) {

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return;
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Events event = booking.getEvent();

        event.removeBooking(booking);

        // 5️⃣ Automatic promotion from waitlist
        if (!event.getWaitlist().isEmpty()) {
            Booking promoted = event.getWaitlist().poll();
            promoted.setStatus(BookingStatus.CONFIRMED);
            event.addConfirmedBooking(promoted);
        }
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }
}