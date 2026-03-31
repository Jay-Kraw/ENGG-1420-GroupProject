package ui;


import model.*;
import service.BookingManager;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) throws FileNotFoundException {

        String userFile,bookFile,eventFile;

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

        Scanner userScan = new Scanner(new File("src\\users.csv"/*userFile*/));
        String skip = userScan.nextLine();
        while (userScan.hasNextLine()) {
            String line = userScan.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] x = line.split(",");
            if (x.length < 4) {
                continue;
            }
            String id = x[0];
            String name = x[1];
            String email = x[2];
            String type = x[3];
            User user = new User(id, name, email, type);
            users.add(user);
        }
        userScan.close();

        Scanner eventsScan = new Scanner(new File("src\\events.csv"/*eventFile*/));
        skip = eventsScan.nextLine();
        while (eventsScan.hasNextLine()) {
            boolean Status = false;
            String line = eventsScan.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] x = line.split(",", -1);
            if (x.length < 10) {
                continue;
            }
            String id = x[0];
            String title = x[1];
            String datetime = x[2];
            String location = x[3];
            int capacity = Integer.parseInt(x[4]);
            if (x[5].equals("Active")) {
                Status = true;
            } else if (x[5].equals("Cancelled")) {
                Status = false;
            }
            String eventType = x[6];
            String Topic = x[7];
            String speakerName = x[8];
            String ageRestriction = x[9];
            if (eventType.equals("Workshop")) {
                Workshop workshop = new Workshop(id, title, datetime, location, capacity, Status, Topic);
                events.add(workshop);

            } else if (eventType.equals("Seminar")) {
                Seminar Seminar = new Seminar(id, title, datetime, location, capacity, Status, speakerName);
                events.add(Seminar);

            } else if (eventType.equals("Concert")) {

                Concert Concert = new Concert(id, title, datetime, location, capacity, Status, ageRestriction);
                events.add(Concert);
            }
        }
        eventsScan.close();


        Scanner bookScan = new Scanner(new File("src\\bookings.csv"));
        skip = bookScan.nextLine();
        Boolean Status = false;
        while (bookScan.hasNextLine()) {
            String line = bookScan.nextLine();
            String[] x = line.split(",");
            String BookingID = x[0];
            String userId = x[1];
            int foundUser = -1; // Default to -1 if not found
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserId().equals(userId)) {
                    foundUser = i;
                    break;
                }
            }
            String eventId = x[2];
            int foundevent = -1; // Default to -1 if not found
            for (int i = 0; i < events.size(); i++) {
                if (events.get(i).getEventId().equals(eventId)) {
                    foundevent = i;
                    break;
                }
            }
            String createdAt = x[3];
            if (x[4].equals("Active")) {
                Status = true;
            } else if (x[4].equals("Cancelled")) {
                Status = false;
            }
            if (foundUser != -1 && foundevent != -1) {
                bookingManager.createBooking(BookingID, users.get(foundUser), events.get(foundevent));
            }
        }
        bookScan.close();


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
        WaitlistPanel waitlistPanel = new WaitlistPanel(bookingManager, events);
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

    public static int findEventIndexById(ArrayList<Events> list, String searchId) {
        for (int i = 0; i < list.size(); i++) {
            // Get the object at the current index and check its ID
            if (list.get(i).getEventId().equalsIgnoreCase(searchId)) {
                return i; // Return the position (index)
            }
        }
        return -1; // Standard way to say "Not Found"
    }


}
