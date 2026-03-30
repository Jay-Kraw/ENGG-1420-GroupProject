package ui;

import model.*;
import service.BookingManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WaitlistPanel extends JPanel {

    private BookingManager bookingManager;
    private List<Events> events;

    private JComboBox<Events> eventBox;
    private JTextArea confirmedArea;
    private JTextArea waitlistArea;

    public WaitlistPanel(BookingManager bookingManager, List<Events> events) {

        this.bookingManager = bookingManager;
        this.events = events;

        setLayout(new BorderLayout());


        // Top Panel (Event Selector)

        JPanel topPanel = new JPanel(new BorderLayout());

        eventBox = new JComboBox<>();
        loadEvents();

        JButton refreshButton = new JButton("Refresh");

        topPanel.add(new JLabel("Select Event:"), BorderLayout.WEST);
        topPanel.add(eventBox, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);


        // Center Panel (Lists)

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        confirmedArea = new JTextArea();
        confirmedArea.setEditable(false);

        waitlistArea = new JTextArea();
        waitlistArea.setEditable(false);

        centerPanel.add(new JScrollPane(confirmedArea));
        centerPanel.add(new JScrollPane(waitlistArea));

        add(centerPanel, BorderLayout.CENTER);

        refreshButton.addActionListener(e -> displayRoster());
        eventBox.addActionListener(e -> displayRoster());
    }

    private void loadEvents() {
        eventBox.removeAllItems();
        for (Events event : events) {
            eventBox.addItem(event);
        }
    }

    private void displayRoster() {

        Events selectedEvent = (Events) eventBox.getSelectedItem();

        if (selectedEvent == null) {
            return;
        }

        confirmedArea.setText("=== CONFIRMED ===\n");
        waitlistArea.setText("=== WAITLIST ===\n");

        // Confirmed List
        for (Booking booking : selectedEvent.getConfirmedBookings()) {
            confirmedArea.append(
                    booking.getUser().getName()
                            + " | "
                            + booking.getStatus()
                            + "\n"
            );
        }

        // Waitlist (FIFO)
        for (Booking booking : selectedEvent.getWaitlist()) {
            waitlistArea.append(
                    booking.getUser().getName()
                            + " | "
                            + booking.getStatus()
                            + "\n"
            );
        }
    }
    public void refreshData() {
        loadEvents();
    }
}