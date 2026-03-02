package ui;

import model.*;
import service.BookingManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainApp {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Campus Event Booking System");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // =============================
        // Shared Backend Data
        // =============================
        BookingManager bookingManager = new BookingManager();
        List<User> users = new ArrayList<>();
        List<Events> events = new ArrayList<>();

        // =============================
        // Create Panels
        // =============================
        UserPanel userPanel = new UserPanel(users);
        EventPanel eventPanel = new EventPanel(events);
        BookingPanel bookingPanel = new BookingPanel(bookingManager, users, events);

        // =============================
        // Left Menu
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
        WaitlistPanel waitlistPanel =
                new WaitlistPanel(bookingManager, events);
        // =============================
        // Main Content Panel
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
        // Button Actions
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
            contentPanel.removeAll();
            contentPanel.add(bookingPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        waitlistButton.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(waitlistPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        frame.setVisible(true);
    }
}