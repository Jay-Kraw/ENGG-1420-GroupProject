package ui;

import model.*;
import service.BookingManager;
import service.FileManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainApp {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Campus Event Booking System");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // =============================
        // FILE MANAGER
        // =============================
        FileManager fm = new FileManager();

        List<User> users = fm.loadUsers("users.csv");
        List<Events> events = fm.loadEvents("events.csv");
        List<Booking> bookings = fm.loadBookings("bookings.csv", users, events);

        System.out.println("Users: " + users.size());
        System.out.println("Events: " + events.size());
        System.out.println("Bookings: " + bookings.size());

        // =============================
        // BACKEND
        // =============================
        BookingManager bookingManager = new BookingManager();

        for (Booking b : bookings) {
            bookingManager.getAllBookings().add(b);
        }

        // =============================
        // PANELS
        // =============================
        UserPanel userPanel = new UserPanel(users);
        EventPanel eventPanel = new EventPanel(events);
        BookingPanel bookingPanel = new BookingPanel(bookingManager, users, events);
        WaitlistPanel waitlistPanel = new WaitlistPanel(bookingManager, events);

        // =============================
        // IMPORT LISTENER (FIXED)
        // =============================
        eventPanel.setImportListener(data -> {

            System.out.println("Imported:\n" + data);

            try {
                String[] lines = data.split("\\n");

                for (String line : lines) {

                    String[] parts = line.split(",");

                    // Ensure correct format
                    if (parts.length >= 5) {

                        String title = parts[0].trim();
                        String dateTime = parts[1].trim();
                        String location = parts[2].trim();

                        int capacity = Integer.parseInt(parts[3].trim());

                        // Handle true/false safely
                        boolean status = parts[4].trim().equalsIgnoreCase("true");

                        // Create event using YOUR constructor
                        Events newEvent = new Events(
                                title,
                                dateTime,
                                location,
                                capacity,
                                status
                        );

                        events.add(newEvent);
                    }
                }

                // Refresh UI
                eventPanel.refreshData();

                JOptionPane.showMessageDialog(frame,
                        "Events Imported Successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(frame,
                        "Import Failed: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // =============================
        // MENU
        // =============================
        JPanel menuPanel = new JPanel(new GridLayout(4, 1));

        JButton userButton = new JButton("User Management");
        JButton eventButton = new JButton("Event Management");
        JButton bookingButton = new JButton("Booking Management");
        JButton waitlistButton = new JButton("Waitlist Management");

        menuPanel.add(userButton);
        menuPanel.add(eventButton);
        menuPanel.add(bookingButton);
        menuPanel.add(waitlistButton);

        // =============================
        // CONTENT
        // =============================
        JPanel contentPanel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel(
                "Welcome to Campus Event Booking System",
                SwingConstants.CENTER
        );

        contentPanel.add(welcomeLabel, BorderLayout.CENTER);

        frame.add(menuPanel, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);

        // =============================
        // BUTTON ACTIONS
        // =============================
        userButton.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(userPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        eventButton.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(eventPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        bookingButton.addActionListener(e -> {
            bookingPanel.refreshData();

            contentPanel.removeAll();
            contentPanel.add(bookingPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        waitlistButton.addActionListener(e -> {
            waitlistPanel.refreshData();

            contentPanel.removeAll();
            contentPanel.add(waitlistPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        // =============================
        // SAVE ON EXIT
        // =============================
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            fm.saveBookings("bookings.csv", bookingManager.getAllBookings());
        }));

        frame.setVisible(true);
    }
}