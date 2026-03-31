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
    private JTextField dateField;
    private JTextField locationField;
    private JTextField capacityField;
    private JComboBox<String> typeBox;
    private JTextArea displayArea;

    // Search & Filter
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox<String> filterBox;
    private JButton filterButton;

    // ✅ IMPORT LISTENER
    private ImportListener importListener;

    public void setImportListener(ImportListener listener) {
        this.importListener = listener;
    }

    public EventPanel(List<Events> events) {
        this.events = events;

        setLayout(new BorderLayout());

        // =========================
        // FORM PANEL (Add Event)
        // =========================
        JPanel formPanel = new JPanel(new GridLayout(6, 2));

        formPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        formPanel.add(titleField);

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

        // =========================
        // DISPLAY AREA
        // =========================
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // =========================
        // SEARCH + FILTER PANEL
        // =========================
        JPanel searchPanel = new JPanel();

        searchPanel.add(new JLabel("Search Title:"));
        searchField = new JTextField(10);
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        searchPanel.add(new JLabel("Filter Type:"));
        filterBox = new JComboBox<>(new String[]{"All", "Workshop", "Seminar", "Concert"});
        searchPanel.add(filterBox);

        filterButton = new JButton("Filter");
        searchPanel.add(filterButton);

        add(searchPanel, BorderLayout.SOUTH);

        // =========================
        // IMPORT PANEL (NEW)
        // =========================
        JPanel importPanel = new JPanel();

        JTextField importField = new JTextField(20);
        JButton importButton = new JButton("Import");

        importPanel.add(new JLabel("Add Event:"));
        importPanel.add(importField);
        importPanel.add(importButton);

        add(importPanel, BorderLayout.EAST);

        // =========================
        // BUTTON ACTIONS
        // =========================

        // Add event
        addButton.addActionListener(e -> addEvent());

        // Search
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().toLowerCase();

            List<Events> results = new ArrayList<>();

            for (Events event : events) {
                if (event.getTitle().toLowerCase().contains(keyword)) {
                    results.add(event);
                }
            }

            displayEvents(results);
        });

        // Filter
        filterButton.addActionListener(e -> {
            String selectedType = (String) filterBox.getSelectedItem();

            List<Events> results = new ArrayList<>();

            if (selectedType.equals("All")) {
                results = events;
            } else {
                for (Events event : events) {

                    if (selectedType.equals("Workshop") && event instanceof Workshop) {
                        results.add(event);
                    }
                    else if (selectedType.equals("Seminar") && event instanceof Seminar) {
                        results.add(event);
                    }
                    else if (selectedType.equals("Concert") && event instanceof Concert) {
                        results.add(event);
                    }
                }
            }

            displayEvents(results);
        });

        // Import button
        importButton.addActionListener(e -> {
            String data = importField.getText();

            if (importListener != null) {
                importListener.onImport(data);
            } else {
                JOptionPane.showMessageDialog(this, "Import listener not set!");
            }
        });

        // Initial display
        displayEvents();
    }

    // =========================
    // ADD EVENT
    // =========================
    private void addEvent() {

        String title = titleField.getText();
        String date = dateField.getText();
        String location = locationField.getText();
        String type = (String) typeBox.getSelectedItem();

        int capacity;

        try {
            capacity = Integer.parseInt(capacityField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity must be a number!");
            return;
        }

        Events event;

        switch (type) {
            case "Workshop":
                event = new Workshop(title, date, location, capacity, true, "General");
                break;

            case "Seminar":
                event = new Seminar(title, date, location, capacity, true, "Guest Speaker");
                break;

            case "Concert":
                event = new Concert(title, date, location, capacity, true, 18);
                break;

            default:
                event = new Events(title, date, location, capacity, true);
        }

        events.add(event);
        displayEvents();

        // Clear fields
        titleField.setText("");
        dateField.setText("");
        locationField.setText("");
        capacityField.setText("");
    }

    // =========================
    // DISPLAY METHODS
    // =========================
    private void displayEvents() {
        displayEvents(events);
    }

    private void displayEvents(List<Events> eventsList) {

        displayArea.setText("");

        for (Events e : eventsList) {

            String extraInfo = "";

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

    // =========================
    // REFRESH (FOR MAINAPP)
    // =========================
    public void refreshData() {
        displayEvents();
    }
}