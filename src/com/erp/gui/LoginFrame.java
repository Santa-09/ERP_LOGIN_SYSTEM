package com.erp.gui;

import com.erp.models.Admin;
import com.erp.models.Student;
import com.erp.services.AuthService;
import com.erp.utils.Config;
import com.erp.utils.LoggerUtil;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private JButton loginButton;
    private AuthService authService;

    public LoginFrame() {
        authService = new AuthService();
        initComponents();
    }

    private void initComponents() {
        setTitle(Config.APP_NAME + " - Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.decode("#2C3E50"));
        headerPanel.setPreferredSize(new Dimension(500, 80));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JLabel titleLabel = new JLabel(Config.APP_NAME);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // User Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userTypeLabel = new JLabel("Login As:");
        userTypeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(userTypeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        userTypeCombo = new JComboBox<>(new String[]{"Admin", "Student"});
        userTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTypeCombo.setPreferredSize(new Dimension(250, 35));
        formPanel.add(userTypeCombo, gbc);

        // Username/Roll Number
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel usernameLabel = new JLabel("Username/Roll No:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(250, 35));
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(250, 35));
        formPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);

        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(250, 45));
        loginButton.setBackground(Color.decode("#3498DB"));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.addActionListener(e -> performLogin());

        formPanel.add(loginButton, gbc);

        // Add Enter key listener to password field
        passwordField.addActionListener(e -> performLogin());

        return formPanel;
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter username and password",
                "Input Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Show loading cursor
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        loginButton.setEnabled(false);

        SwingWorker<Object, Void> worker = new SwingWorker<Object, Void>() {
            @Override
            protected Object doInBackground() throws Exception {
                if ("Admin".equals(userType)) {
                    return authService.authenticateAdmin(username, password);
                } else {
                    return authService.authenticateStudent(username, password);
                }
            }

            @Override
            protected void done() {
                try {
                    Object result = get();

                    if (result != null) {
                        if (result instanceof Admin) {
                            Admin admin = (Admin) result;
                            LoggerUtil.info("LoginFrame", "Admin logged in: " + admin.getUsername());
                            dispose();
                            new AdminDashboard(admin).setVisible(true);
                        } else if (result instanceof Student) {
                            Student student = (Student) result;
                            LoggerUtil.info("LoginFrame", "Student logged in: " + student.getRollNumber());
                            dispose();
                            new StudentDashboard(student).setVisible(true);
                        }
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this,
                            "Invalid username or password",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                        passwordField.setText("");
                    }
                } catch (Exception e) {
                    LoggerUtil.error("LoginFrame", "Error during login", (Exception) e);
                    JOptionPane.showMessageDialog(LoginFrame.this,
                        "An error occurred during login",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                    loginButton.setEnabled(true);
                }
            }
        };

        worker.execute();
    }
}