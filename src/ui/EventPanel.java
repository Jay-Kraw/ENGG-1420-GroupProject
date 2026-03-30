package ui;

import model.Events;
import model.Workshop;
import model.Seminar;
import model.Concert;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EventPanel extends JPanel {

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

        JPanel formPanel = new JPanel(new GridLayout(7, 2));

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

        add(formPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        addButton.addActionListener(e -> addEvent());
        displayEvents();
    }

    private void addEvent() {

        String eventId = eventIdField.getText();
        String title = titleField.getText();
        String date = dateField.getText();
        String location = locationField.getText();
        String type = (String) typeBox.getSelectedItem();

        int capacity;

        // ✅ SAFE input validation
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

        // Clear fields
        titleField.setText("");
        eventIdField.setText("");
        dateField.setText("");
        locationField.setText("");
        capacityField.setText("");
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