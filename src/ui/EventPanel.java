package ui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EventPanel extends JPanel {

    private static final String CSV_HEADER = "eventId,title,dateTime,location,capacity,status,eventType,topic,speakerName,ageRestriction";
    private static final File EVENTS_FILE = new File("src\\events.csv");

    private List<Events> events = new ArrayList<>();
    private JTextField titleField;
    private JTextField eventIdField;
    private JTextField dateField;
    private JTextField locationField;
    private JTextField capacityField;
    private JComboBox<String> typeBox;
    private JTextArea displayArea;

    public EventPanel(List<Events> events) {
        this.events = events;

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(8, 2));

        formPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Event ID:"));
        eventIdField = new JTextField();
        formPanel.add(eventIdField);

        formPanel.add(new JLabel("Date/Time:"));
        dateField = new JTextField();
        formPanel.add(dateField);

        formPanel.add(new JLabel("Location:"));
        locationField = new JTextField();
        formPanel.add(locationField);

        formPanel.add(new JLabel("Capacity:"));
        capacityField = new JTextField();
        formPanel.add(capacityField);

        formPanel.add(new JLabel("Event Type:"));
        typeBox = new JComboBox<>(new String[]{"Workshop", "Seminar", "Concert"});
        formPanel.add(typeBox);

        JButton addButton = new JButton("Add Event");
        formPanel.add(addButton);

        formPanel.add(new JLabel(""));
        JButton importButton = new JButton("Import File");
        formPanel.add(importButton);

        JButton exportButton = new JButton("Export File");
        formPanel.add(exportButton);

        add(formPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        addButton.addActionListener(e -> addEvent());
        importButton.addActionListener(e -> importFile());
        exportButton.addActionListener(e -> exportFile());
        displayEvents();
    }

    private void addEvent() {

        String eventId = eventIdField.getText();
        String title = titleField.getText();
        String date = dateField.getText();
        String location = locationField.getText();
        String type = (String) typeBox.getSelectedItem();

        int capacity;

        //✅ SAFE input validation
        try {
            capacity = Integer.parseInt(capacityField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity must be a number!");
            return;
        }

        Events event;

        switch (type) {
            case "Workshop":
                event = new Workshop(eventId,title, date, location, capacity, true, "General");
                break;

            case "Seminar":
                event = new Seminar(eventId,title, date, location, capacity, true, "Guest Speaker");
                break;

            case "Concert":
                event = new Concert(eventId,title, date, location, capacity, true, "18");
                break;

            default:
                event = new Events(eventId,title, date, location, capacity, true);
        }

        events.add(event);
        displayEvents();
        clearFields();
    }

    private void importFile() {
        List<Events> importedEvents = new ArrayList<>();

        try (Scanner scanner = new Scanner(EVENTS_FILE)) {
            if (scanner.hasNextLine()) {
                scanner.nextLine().trim();
            }
            int lineNumber = 2;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    importedEvents.add(parseEvent(line, lineNumber));
                }
                lineNumber++;
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Could not open " + EVENTS_FILE.getPath());
            return;
        }

        events.clear();
        events.addAll(importedEvents);
        displayEvents();
        JOptionPane.showMessageDialog(this, "Imported " + importedEvents.size() + " events from " + EVENTS_FILE.getPath() + ".");
    }

    private void exportFile() {
        try (PrintWriter writer = new PrintWriter(EVENTS_FILE)) {
            writer.println(CSV_HEADER);

            for (Events event : events) {
                String eventType = "";
                String topic = "";
                String speakerName = "";
                String ageRestriction = "";

                if (event instanceof Workshop) {
                    eventType = "Workshop";
                    topic = ((Workshop) event).getTopic();
                } else if (event instanceof Seminar) {
                    eventType = "Seminar";
                    speakerName = ((Seminar) event).getSpeakerName();
                } else if (event instanceof Concert) {
                    eventType = "Concert";
                    ageRestriction = ((Concert) event).getAgeRestriction();
                }

                writer.println(
                        csvValue(event.getEventId()) + "," +
                                csvValue(event.getTitle()) + "," +
                                csvValue(event.getDateTime()) + "," +
                                csvValue(event.getLocation()) + "," +
                                event.getCapacity() + "," +
                                csvValue(event.getStatus() ? "Active" : "Cancelled") + "," +
                                csvValue(eventType) + "," +
                                csvValue(topic) + "," +
                                csvValue(speakerName) + "," +
                                csvValue(ageRestriction)
                );
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Could not save " + EVENTS_FILE.getPath() + ".");
            return;
        }

        JOptionPane.showMessageDialog(this, "Exported " + events.size() + " events to " + EVENTS_FILE.getPath() + ".");
    }

    private Events parseEvent(String line, int lineNumber) {
        String[] values = line.split(",", -1);

        if (values.length != 10) {
            throw new IllegalArgumentException("Invalid event data on line " + lineNumber + ".");
        }

        String eventId = values[0].trim();
        String title = values[1].trim();
        String dateTime = values[2].trim();
        String location = values[3].trim();
        String capacityText = values[4].trim();
        String statusText = values[5].trim();
        String eventType = values[6].trim();
        String topic = values[7].trim();
        String speakerName = values[8].trim();
        String ageRestriction = values[9].trim();

        if (eventId.isEmpty() || title.isEmpty() || dateTime.isEmpty() || location.isEmpty() ||
                capacityText.isEmpty() || statusText.isEmpty() || eventType.isEmpty()) {
            throw new IllegalArgumentException("Missing event data on line " + lineNumber + ".");
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityText);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid capacity on line " + lineNumber + ".");
        }

        boolean status;
        if (statusText.equalsIgnoreCase("Active")) {
            status = true;
        } else if (statusText.equalsIgnoreCase("Cancelled")) {
            status = false;
        } else {
            throw new IllegalArgumentException("Invalid status on line " + lineNumber + ".");
        }

        if (eventType.equalsIgnoreCase("Workshop")) {
            return new Workshop(eventId, title, dateTime, location, capacity, status, topic);
        } else if (eventType.equalsIgnoreCase("Seminar")) {
            return new Seminar(eventId, title, dateTime, location, capacity, status, speakerName);
        } else if (eventType.equalsIgnoreCase("Concert")) {
            return new Concert(eventId, title, dateTime, location, capacity, status, ageRestriction);
        }

        return new Events(eventId, title, dateTime, location, capacity, status);
    }

    private String csvValue(String value) {
        return value == null ? "" : value.replace(",", " ");
    }

    private void clearFields() {
        titleField.setText("");
        eventIdField.setText("");
        dateField.setText("");
        locationField.setText("");
        capacityField.setText("");
        typeBox.setSelectedIndex(0);
    }



    private void displayEvents() {

        displayArea.setText("");

        for (Events e : events) {

            String extraInfo = "";

            // ✅ Polymorphism display
            if (e instanceof Workshop) {
                extraInfo = " | Topic: " + ((Workshop) e).getTopic();
            }
            else if (e instanceof Seminar) {
                extraInfo = " | Speaker: " + ((Seminar) e).getSpeakerName();
            }
            else if (e instanceof Concert) {
                extraInfo = " | Age Restriction: " + ((Concert) e).getAgeRestriction();
            }

            displayArea.append(
                    e.getTitle() +
                            " | " + e.getDateTime() +
                            " | Location: " + e.getLocation() +
                            " | Capacity: " + e.getCapacity() +
                            " | Active: " + e.getStatus() +
                            extraInfo +
                            "\n"
            );
        }
    }
}
