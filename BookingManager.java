//Muhammad Umer controller BookingManager class
package controller;

import model.Booking;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BookingManager {
    // Private variable
    private ArrayList<Booking> bookings;

    // Constructor
    public BookingManager() {
        bookings = new ArrayList<>();
    }

    /**
     * Books an event for a user
     * @param userId ID of the user
     * @param eventId ID of the event
     * @param userMax Maximum confirmed bookings for this user type
     * @param capacity Event capacity
     * @param isActive Whether event is active
     * @return The new Booking object, or null if failed
     */
    public Booking bookEvent(String userId, String eventId, int userMax,
                             int capacity, boolean isActive) {

        // Check if event is active
        if (!isActive) {
            System.out.println("Error: Event not active");
            return null;
        }

        int userConfirmedCount = 0;
        int eventConfirmedCount = 0;

        // Loop through all existing bookings
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);

            // Check for duplicate booking (same user + same event, not cancelled)
            if (b.getUserId().equals(userId) && b.getEventId().equals(eventId)
                    && !b.getStatus().equals("CANCELLED")) {
                System.out.println("Error: Duplicate booking");
                return null;
            }

            // Count user's confirmed bookings
            if (b.getUserId().equals(userId) && b.getStatus().equals("CONFIRMED")) {
                userConfirmedCount++;
            }

            // Count event's confirmed bookings
            if (b.getEventId().equals(eventId) && b.getStatus().equals("CONFIRMED")) {
                eventConfirmedCount++;
            }
        }

        // Check if user reached their limit
        if (userConfirmedCount >= userMax) {
            System.out.println("Error: User reached max bookings (" + userMax + ")");
            return null;
        }

        // Determine status based on capacity
        String status;
        if (eventConfirmedCount < capacity) {
            status = "CONFIRMED";
            System.out.println("Booking CONFIRMED for user " + userId);
        } else {
            status = "WAITLISTED";
            System.out.println("Event full - user " + userId + " WAITLISTED");
        }

        // Create booking ID
        String bookingId = "B" + (bookings.size() + 1001);

        // Create and save booking
        Booking newBooking = new Booking(bookingId, userId, eventId,
                LocalDateTime.now(), status);
        bookings.add(newBooking);

        return newBooking;
    }

    /**
     * Cancels a booking and promotes first waitlisted if needed
     * @param bookingId ID of booking to cancel
     */
    public void cancelBooking(String bookingId) {
        // Find the booking
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);

            if (b.getBookingId().equals(bookingId)) {
                String eventId = b.getEventId();
                boolean wasConfirmed = b.getStatus().equals("CONFIRMED");

                // Cancel it
                b.setStatus("CANCELLED");
                System.out.println("Booking " + bookingId + " cancelled");

                // If it was confirmed, promote from waitlist
                if (wasConfirmed) {
                    // Find the earliest waitlisted booking for this event
                    Booking earliestWaitlisted = null;
                    LocalDateTime earliestTime = null;

                    for (int j = 0; j < bookings.size(); j++) {
                        Booking w = bookings.get(j);
                        if (w.getEventId().equals(eventId) &&
                                w.getStatus().equals("WAITLISTED")) {
                            //Check each booking to see which was created earlier
                            if (earliestWaitlisted == null ||
                                    w.getCreatedAt().isBefore(earliestTime)) {
                                earliestWaitlisted = w;
                                earliestTime = w.getCreatedAt();
                            }
                        }
                    }

                    // Promote if found
                    if (earliestWaitlisted != null) {
                        earliestWaitlisted.setStatus("CONFIRMED");
                        System.out.println("PROMOTED: User " +
                                earliestWaitlisted.getUserId() +
                                " to CONFIRMED for event " + eventId);
                    }
                }
                return;
            }
        }
        System.out.println("Error: Booking not found");
    }

    /**
     * Gets all confirmed bookings for an event
     * @param eventId ID of the event
     * @return ArrayList of confirmed bookings
     */
    public ArrayList<Booking> getConfirmedBookings(String eventId) {
        ArrayList<Booking> result = new ArrayList<>();

        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            if (b.getEventId().equals(eventId) && b.getStatus().equals("CONFIRMED")) {
                result.add(b);
            }
        }
        return result;
    }

    /**
     * Gets waitlist for an event in correct order (earliest first)
     * @param eventId ID of the event
     * @return ArrayList of waitlisted bookings in order
     */
    public ArrayList<Booking> getWaitlist(String eventId) {
        ArrayList<Booking> result = new ArrayList<>();

        // Collect all waitlisted bookings
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            if (b.getEventId().equals(eventId) && b.getStatus().equals("WAITLISTED")) {

                // Insert in order (earliest first)
                int pos = 0;
                while (pos < result.size()) {
                    if (b.getCreatedAt().isBefore(result.get(pos).getCreatedAt())) {
                        break;
                    }
                    pos++;
                }
                result.add(pos, b);
            }
        }
        return result;
    }

    /**
     * Gets all bookings for a user
     * @param userId ID of the user
     * @return ArrayList of user's bookings
     */
    public ArrayList<Booking> getUserBookings(String userId) {
        ArrayList<Booking> result = new ArrayList<>();

        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            if (b.getUserId().equals(userId)) {
                result.add(b);
            }
        }
        return result;
    }

    /**
     * Gets all bookings in the system
     * @return ArrayList of all bookings
     */
    public ArrayList<Booking> getAllBookings() {
        ArrayList<Booking> result = new ArrayList<>();
        for (int i = 0; i < bookings.size(); i++) {
            result.add(bookings.get(i));
        }
        return result;
    }

    /**
     * Loads bookings from CSV data
     * @param csvLines Array of CSV strings
     */
    public void loadFromCSV(String[] csvLines) {
        for (int i = 0; i < csvLines.length; i++) {
            String line = csvLines[i];
            if (line == null || line.equals("")) {
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length >= 5) {
                String bookingId = parts[0];
                String userId = parts[1];
                String eventId = parts[2];
                LocalDateTime createdAt = LocalDateTime.parse(parts[3]);

                String status;
                if (parts[4].equals("Confirmed")) {
                    status = "CONFIRMED";
                } else if (parts[4].equals("Waitlisted")) {
                    status = "WAITLISTED";
                } else {
                    status = "CANCELLED";
                }

                Booking b = new Booking(bookingId, userId, eventId, createdAt, status);
                bookings.add(b);
            }
        }
        System.out.println("Loaded " + bookings.size() + " bookings");
    }
}