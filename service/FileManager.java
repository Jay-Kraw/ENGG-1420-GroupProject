package service;

import model.*;
import java.io.*;
import java.util.*;

public class FileManager {

    // ===============================
    // LOAD USERS
    // ===============================
    public List<User> loadUsers(String filePath) {

        List<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                String[] p = line.split(",");

                String userId = p[0];
                String name = p[1];
                String email = p[2];
                String type = p[3];

                User user;

                if (type.equalsIgnoreCase("Student")) {
                    user = new Student(userId, name, email, "S-" + userId, "General");
                }
                else if (type.equalsIgnoreCase("Staff")) {
                    user = new Staff(userId, name, email, "ST-" + userId, "Admin");
                }
                else {
                    user = new Guest(userId, name, email, "G-" + userId, "2026-12-31");
                }

                users.add(user);
            }

        } catch (Exception e) {
            System.out.println("Error loading users");
            e.printStackTrace();
        }

        return users;
    }

    // ===============================
    // LOAD EVENTS (FINAL FIX)
    // ===============================
    public List<Events> loadEvents(String filePath) {

        List<Events> events = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                // IMPORTANT: keep empty values
                String[] p = line.split(",", -1);

                // Convert E101 -> 101
                int eventId = Integer.parseInt(p[0].substring(1));

                String title = p[1];
                String dateTime = p[2];
                String location = p[3];
                int capacity = Integer.parseInt(p[4]);
                boolean status = p[5].equalsIgnoreCase("Active");
                String type = p[6];

                String topic = p[7];
                String speaker = p[8];

                int age = 0;

                if (!p[9].isEmpty()) {
                    try {
                        age = Integer.parseInt(p[9].replace("+", "").trim());
                    } catch (NumberFormatException e) {
                        // Handles "All Ages" or any text
                        age = 0; // default value
                    }
                }

                Events event;

                if (type.equalsIgnoreCase("Workshop")) {
                    event = new Workshop(title, dateTime, location, capacity, status, topic);
                }
                else if (type.equalsIgnoreCase("Seminar")) {
                    event = new Seminar(title, dateTime, location, capacity, status, speaker);
                }
                else {
                    event = new Concert(title, dateTime, location, capacity, status, age);
                }

                // set ID from CSV
                event.setEventId(eventId);

                events.add(event);
            }

        } catch (Exception e) {
            System.out.println("Error loading events");
            e.printStackTrace();
        }

        return events;
    }

    // ===============================
    // LOAD BOOKINGS (FINAL FIX)
    // ===============================
    public List<Booking> loadBookings(String filePath,
                                      List<User> users,
                                      List<Events> events) {

        List<Booking> bookings = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                String[] p = line.split(",");

                String bookingId = p[0];
                String userId = p[1];

                // Convert E101 -> 101
                int eventId = Integer.parseInt(p[2].substring(1));

                BookingStatus status = BookingStatus.valueOf(p[4].toUpperCase());

                // Find user
                User user = null;
                for (User u : users) {
                    if (u.getUserId().equals(userId)) {
                        user = u;
                        break;
                    }
                }

                // Find event
                Events event = null;
                for (Events e : events) {
                    if (e.getEventId() == eventId) {
                        event = e;
                        break;
                    }
                }

                Booking booking = new Booking(bookingId, user, event);
                booking.setStatus(status);

                bookings.add(booking);

                // Restore event lists
                if (event != null) {

                    if (status == BookingStatus.CONFIRMED) {
                        event.addConfirmedBooking(booking);
                    }
                    else if (status == BookingStatus.WAITLISTED) {
                        event.addToWaitlist(booking);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading bookings");
            e.printStackTrace();
        }

        return bookings;
    }

    // ===============================
    // SAVE BOOKINGS
    // ===============================
    public void saveBookings(String filePath, List<Booking> bookings) {

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            pw.println("bookingId,userId,eventId,createdAt,bookingStatus");

            for (Booking b : bookings) {

                // Convert 101 -> E101 when saving
                pw.println(b.getBookingId() + "," +
                        b.getUser().getUserId() + "," +
                        "E" + b.getEvent().getEventId() + "," +
                        b.getCreatedAt() + "," +
                        b.getStatus());
            }

        } catch (Exception e) {
            System.out.println("Error saving bookings");
            e.printStackTrace();
        }
    }
}