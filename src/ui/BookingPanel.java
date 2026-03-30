package ui;

import model.*;
import service.BookingManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.UUID;

public class BookingPanel extends JPanel {

    private BookingManager bookingManager;

    private JComboBox<User> userBox;
    private JComboBox<Events> eventBox;
    private JTextArea resultArea;

    private List<User> users;
    private List<Events> events;

    public BookingPanel(BookingManager bookingManager,
                        List<User> users,
                        List<Events> events) {

        this.bookingManager = bookingManager;
        this.users = users;
        this.events = events;

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2));

        userBox = new JComboBox<>();
        eventBox = new JComboBox<>();

        JButton bookButton = new JButton("Book Event");
        JButton cancelButton = new JButton("Cancel Booking");

        formPanel.add(new JLabel("Select User:"));
        formPanel.add(userBox);

        formPanel.add(new JLabel("Select Event:"));
        formPanel.add(eventBox);

        formPanel.add(bookButton);
        formPanel.add(cancelButton);

        add(formPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        loadUsers();
        loadEvents();

        bookButton.addActionListener(e -> bookEvent());
        cancelButton.addActionListener(e -> cancelBooking());
    }

    private void loadUsers() {
        userBox.removeAllItems();
        for (User user : users) {
            userBox.addItem(user);
        }
    }

    private void loadEvents() {
        eventBox.removeAllItems();
        for (Events event : events) {
            if (!event.isCancelled()) {
                eventBox.addItem(event);
            }
        }
    }

    private void bookEvent() {

        User user = (User) userBox.getSelectedItem();
        Events event = (Events) eventBox.getSelectedItem();

        if (user == null || event == null) {
            resultArea.append("Select both user and event.\n");
            return;
        }

        try {
            String bookingId = UUID.randomUUID().toString();

            Booking booking = bookingManager.createBooking(
                    bookingId,
                    user,
                    event
            );

            resultArea.append(
                    "Booking Created → Status: "
                            + booking.getStatus()
                            + "\n"
            );

        } catch (Exception ex) {
            resultArea.append("Error: " + ex.getMessage() + "\n");
        }
    }

    private void cancelBooking() {

        User user = (User) userBox.getSelectedItem();
        Events event = (Events) eventBox.getSelectedItem();

        if (user == null || event == null) {
            resultArea.append("Select both user and event.\n");
            return;
        }

        try {

            // Find active booking for this user + event
            for (Booking booking : bookingManager.getAllBookings()) {

                if (booking.getUser().equals(user) &&
                        booking.getEvent().equals(event) &&
                        booking.getStatus() != BookingStatus.CANCELLED) {

                    bookingManager.cancelBooking(booking);
                    resultArea.append("Booking Cancelled.\n");
                    return;
                }
            }

            resultArea.append("No active booking found.\n");

        } catch (Exception ex) {
            resultArea.append("Error: " + ex.getMessage() + "\n");
        }
    }
}