package ui;
import model.*;
import service.BookingManager;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;



public class BookingPanel extends JPanel {

    private static final String CSV_HEADER = "bookingId,userId,eventId,createdAt,bookingStatus";
    private static final File BOOKINGS_FILE = new File("src\\bookings.csv");

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

        JPanel formPanel = new JPanel(new GridLayout(4, 2));

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
        formPanel.add(new JLabel(""));
        JButton importButton = new JButton("Import File");
        formPanel.add(importButton);

        JButton exportButton = new JButton("Export File");
        formPanel.add(exportButton);

        add(formPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        loadUsers();
        loadEvents();

        bookButton.addActionListener(e -> bookEvent());
        cancelButton.addActionListener(e -> cancelBooking());
        importButton.addActionListener(e -> importFile());
        exportButton.addActionListener(e -> exportFile());
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
    private void importFile() {
        List<Booking> importedBookings = new ArrayList<>();

        try (Scanner scanner = new Scanner(BOOKINGS_FILE)) {
            if (scanner.hasNextLine()) {
                scanner.nextLine().trim();
            }

            int lineNumber = 2;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] values = line.split(",", -1);

                    if (values.length != 5) {
                        throw new IllegalArgumentException("Invalid booking data on line " + lineNumber + ".");
                    }

                    String bookingId = values[0].trim();
                    String userId = values[1].trim();
                    String eventId = values[2].trim();
                    String createdAtText = values[3].trim();
                    String statusText = values[4].trim();

                    if (bookingId.isEmpty() || userId.isEmpty() || eventId.isEmpty() ||
                            createdAtText.isEmpty() || statusText.isEmpty()) {
                        throw new IllegalArgumentException("Missing booking data on line " + lineNumber + ".");
                    }

                    User matchedUser = null;
                    for (User user : users) {
                        if (user.getUserId().equals(userId)) {
                            matchedUser = user;
                            break;
                        }
                    }

                    if (matchedUser == null) {
                        throw new IllegalArgumentException("Unknown user on line " + lineNumber + ".");
                    }

                    Events matchedEvent = null;
                    for (Events event : events) {
                        if (event.getEventId().equals(eventId)) {
                            matchedEvent = event;
                            break;
                        }
                    }

                    if (matchedEvent == null) {
                        throw new IllegalArgumentException("Unknown event on line " + lineNumber + ".");
                    }

                    LocalDateTime createdAt;
                    try {
                        createdAt = LocalDateTime.parse(createdAtText);
                    } catch (Exception ex) {
                        throw new IllegalArgumentException("Invalid createdAt on line " + lineNumber + ".");
                    }

                    BookingStatus status;
                    if (statusText.equalsIgnoreCase("Confirmed")) {
                        status = BookingStatus.CONFIRMED;
                    } else if (statusText.equalsIgnoreCase("Waitlisted")) {
                        status = BookingStatus.WAITLISTED;
                    } else if (statusText.equalsIgnoreCase("Cancelled")) {
                        status = BookingStatus.CANCELLED;
                    } else {
                        throw new IllegalArgumentException("Invalid booking status on line " + lineNumber + ".");
                    }

                    Booking booking = new Booking(bookingId, matchedUser, matchedEvent,LocalDateTime.now(),status);
                    booking.setStatus(status);

                    Field createdAtField = Booking.class.getDeclaredField("createdAt");
                    createdAtField.setAccessible(true);
                    createdAtField.set(booking, createdAt);

                    importedBookings.add(booking);
                }
                lineNumber++;
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Could not open " + BOOKINGS_FILE.getPath());
            return;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        } catch (ReflectiveOperationException ex) {
            JOptionPane.showMessageDialog(this, "Could not restore bookings from " + BOOKINGS_FILE.getPath() + ".");
            return;
        }

        bookingManager.getAllBookings().clear();
        for (Events event : events) {
            event.getConfirmedBookings().clear();
            event.getWaitlist().clear();
        }

        for (Booking booking : importedBookings) {
            bookingManager.getAllBookings().add(booking);

            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                booking.getEvent().addConfirmedBooking(booking);
            } else if (booking.getStatus() == BookingStatus.WAITLISTED) {
                booking.getEvent().addToWaitlist(booking);
            }
        }

        loadUsers();
        loadEvents();
        resultArea.append("Imported " + importedBookings.size() + " bookings from " + BOOKINGS_FILE.getPath() + ".\n");
    }

    private void exportFile() {
        try (PrintWriter writer = new PrintWriter(BOOKINGS_FILE)) {
            writer.println(CSV_HEADER);

            for (Booking booking : bookingManager.getAllBookings()) {
                String statusText = "Cancelled";

                if (booking.getStatus() == BookingStatus.CONFIRMED) {
                    statusText = "Confirmed";
                } else if (booking.getStatus() == BookingStatus.WAITLISTED) {
                    statusText = "Waitlisted";
                }

                writer.println(
                        (booking.getBookingId() == null ? "" : booking.getBookingId().replace(",", " ")) + "," +
                                (booking.getUser().getUserId() == null ? "" : booking.getUser().getUserId().replace(",", " ")) + "," +
                                (booking.getEvent().getEventId() == null ? "" : booking.getEvent().getEventId().replace(",", " ")) + "," +
                                booking.getCreatedAt() + "," +
                                statusText
                );
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Could not save " + BOOKINGS_FILE.getPath() + ".");
            return;
        }

        resultArea.append("Exported " + bookingManager.getAllBookings().size() + " bookings to " + BOOKINGS_FILE.getPath() + ".\n");
    }


}