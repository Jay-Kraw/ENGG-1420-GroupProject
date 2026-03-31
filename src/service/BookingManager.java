package service;

import model.Booking;
import model.BookingStatus;
import model.User;
import model.Events;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingManager {
    private ArrayList<Booking> bookings;

    public BookingManager() {
        bookings = new ArrayList<>();
    }

    // Fixed signature: Now accepts bookingId, user, and event
    public Booking createBooking(String bookingId, User user, Events event) {
        if (user == null || event == null) return null;

        // 1. Check if event is cancelled
        if (event.isCancelled()) {
            System.out.println("Error: Event is cancelled.");
            return null;
        }

        // 2. Check User Booking Limits
        int userConfirmedCount = 0;
        for (Booking b : bookings) {
            if (b.getUserId().equals(user.getUserId()) && b.getStatus() != BookingStatus.CANCELLED) {
                userConfirmedCount++;
            }
        }

        if (userConfirmedCount >= user.getMaxBookings()) {
            System.out.println("Error: User reached limit.");
            return null;
        }

        // 3. Determine Status based on Event Capacity
        BookingStatus status = event.isFull() ? BookingStatus.WAITLISTED : BookingStatus.CONFIRMED;

        // 4. Create and store booking
        Booking newBooking = new Booking(bookingId, user, event, LocalDateTime.now(), status);
        bookings.add(newBooking);

        // 5. Update the Event's internal lists
        if (status == BookingStatus.CONFIRMED) {
            event.addConfirmedBooking(newBooking);
        } else {
            event.addToWaitlist(newBooking);
        }

        return newBooking;
    }

    public void cancelBooking(Booking booking) {
        if (booking == null) return;
        booking.setStatus(BookingStatus.CANCELLED);
        Events event = booking.getEvent();
        if (event != null) {
            event.removeBooking(booking);
            // If a spot opened up, move the first person from the waitlist
            if (!event.isFull() && !event.getWaitlist().isEmpty()) {
                Booking promoted = event.getWaitlist().poll();
                promoted.setStatus(BookingStatus.CONFIRMED);
                event.addConfirmedBooking(promoted);
            }
        }
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }
}