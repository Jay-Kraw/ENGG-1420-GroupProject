package ui;

import model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserPanel extends JPanel {

    private static final String CSV_HEADER = "userId,name,email,type";
    private static final File USERS_FILE = new File("src\\users.csv");

    private List<User> users = new ArrayList<>();

    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JComboBox<String> typeBox;
    private JTextArea displayArea;

    public UserPanel(List<User> users) {
        this.users = users;

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2));

        formPanel.add(new JLabel("User ID:"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("User Type:"));
        typeBox = new JComboBox<>(new String[]{"Student", "Staff", "Guest"});
        formPanel.add(typeBox);

        JButton addButton = new JButton("Add User");
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

        addButton.addActionListener(e -> addUser());
        importButton.addActionListener(e -> importFile());
        exportButton.addActionListener(e -> exportFile());
        displayUsers();
    }


    private void addUser() {

        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String type = ((String) typeBox.getSelectedItem());

        if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All user fields are required.");
            return;
        }

        User user = new User(id, name, email, type);
        users.add(user);

        displayUsers();
        clearFields();
    }


    private void importFile() {
        List<User> importedUsers = new ArrayList<>();

        try (Scanner scanner = new Scanner(USERS_FILE)) {
            //reads in each line of code and skips the first line
            if (scanner.hasNextLine()) {
                String firstLine = scanner.nextLine().trim();
            }
            //reads in rest of list
            int lineNumber = 2;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    importedUsers.add(parseUser(line, lineNumber));
                }
                lineNumber++;
            }
        } catch (FileNotFoundException ex) { //if file could not be found
            JOptionPane.showMessageDialog(this, "Could not open " + USERS_FILE.getPath());
            return;
        }

        //clears the array, and refreshes with new imported users
        users.clear();
        users.addAll(importedUsers);
        displayUsers();
        //confirmation message
        JOptionPane.showMessageDialog(this, "Imported " + importedUsers.size() + " users from " + USERS_FILE.getPath() + ".");
    }

    private void exportFile() {
        //print writer initialization
        try (PrintWriter writer = new PrintWriter(USERS_FILE)) { //prints to final variable USERS_FILE (users.csv)
            writer.println(CSV_HEADER); //first prints the HEADER

            for (User user : users) {//for loop to print rest of list to file
                writer.println(
                        csvValue(user.getUserId()) + "," + csvValue(user.getName()) + "," + csvValue(user.getEmail()) + "," + csvValue((user.getType()))
                );
            }
        } catch (FileNotFoundException ex) { //if file not found
            JOptionPane.showMessageDialog(this, "Could not save " + USERS_FILE.getPath() + ".");
            return;
        }

        JOptionPane.showMessageDialog(this, "Exported " + users.size() + " users to " + USERS_FILE.getPath() + ".");
    }

    private void displayUsers() {

        displayArea.setText("");

        for (User u : users) {
            displayArea.append(u.getSummary() + "\n");
        }
    }

    private User parseUser(String line, int lineNumber) {
        String[] values = line.split(",", -1); //converts each user into an array of values

        if (values.length != 4) { //if too many values, throw exception
            throw new IllegalArgumentException("Invalid user data on line " + lineNumber + ".");
        }

        String id = values[0].trim();
        String name = values[1].trim();
        String email = values[2].trim();
        String type = (values[3].trim());

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || type.isEmpty()) { //if empty
            throw new IllegalArgumentException("Missing user data on line " + lineNumber + ".");
        }

        return new User(id, name, email, type);
    }

    //method to replace spaces with commas
    private String csvValue(String value) {
        return value == null ? "" : value.replace(",", " ");
    }

    private void clearFields() {//clear method
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        typeBox.setSelectedIndex(0);
    }

}
