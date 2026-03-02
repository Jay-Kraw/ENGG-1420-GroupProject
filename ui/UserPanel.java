package ui;

import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UserPanel extends JPanel {

    private List<User> users = new ArrayList<>();

    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JComboBox<String> typeBox;
    private JTextArea displayArea;

    public UserPanel(List<User> users) {
        this.users = users;

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2));

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

        add(formPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        addButton.addActionListener(e -> addUser());
    }

    private void addUser() {

        String id = idField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String type = (String) typeBox.getSelectedItem();

        User user = new User(id, name, email, type);
        users.add(user);

        displayUsers();
    }

    private void displayUsers() {

        displayArea.setText("");

        for (User u : users) {
            displayArea.append(u.getSummary() + "\n");
        }
    }
}