package com.erp.gui;

import com.erp.models.Student;
import com.erp.utils.Config;
import com.erp.db.AttendanceDAO;
import com.erp.db.FeeDAO;
import com.erp.db.TimeTableDAO;
import com.erp.db.AdmitCardDAO;
import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {

    private Student student;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public StudentDashboard(Student student) {
        this.student = student;
        initComponents();
    }

    private void initComponents() {
        setTitle(Config.APP_NAME + " - Student Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebarPanel = createSidebarPanel();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Add panels
        contentPanel.add(createProfilePanel(), "PROFILE");
        contentPanel.add(createAttendanceViewPanel(), "ATTENDANCE");
        contentPanel.add(createFeeViewPanel(), "FEES");
        contentPanel.add(createTimeTableViewPanel(), "TIMETABLE");
        contentPanel.add(createAdmitCardViewPanel(), "ADMITCARD");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(Color.decode("#2C3E50"));
        headerPanel.setPreferredSize(new Dimension(1200, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel(Config.APP_NAME + " - Student Portal");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        JLabel userLabel = new JLabel(student.getFullName() + " (" + student.getRollNumber() + ")");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        logoutButton.setBackground(Color.decode("#E74C3C"));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());

        userPanel.add(userLabel);
        userPanel.add(Box.createHorizontalStrut(20));
        userPanel.add(logoutButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(Color.decode("#34495E"));
        sidebarPanel.setPreferredSize(new Dimension(220, 640));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        String[] menuItems = {
            "My Profile",
            "My Attendance",
            "Fee Status",
            "Time Table",
            "Admit Card"
        };

        String[] cardNames = {
            "PROFILE",
            "ATTENDANCE",
            "FEES",
            "TIMETABLE",
            "ADMITCARD"
        };

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuItems[i], cardNames[i]);
            sidebarPanel.add(menuButton);
            sidebarPanel.add(Box.createVerticalStrut(5));
        }

        return sidebarPanel;
    }

    private JButton createMenuButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.decode("#34495E"));
        button.setMaximumSize(new Dimension(220, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode("#2C3E50"));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode("#34495E"));
            }
        });

        button.addActionListener(e -> cardLayout.show(contentPanel, cardName));

        return button;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 15));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        addDetailRow(detailsPanel, "Roll Number:", student.getRollNumber());
        addDetailRow(detailsPanel, "Full Name:", student.getFullName());
        addDetailRow(detailsPanel, "Email:", student.getEmail());
        addDetailRow(detailsPanel, "Phone:", student.getPhone());
        addDetailRow(detailsPanel, "Course:", student.getCourse());
        addDetailRow(detailsPanel, "Semester:", String.valueOf(student.getSemester()));
        addDetailRow(detailsPanel, "Section:", student.getSection());
        addDetailRow(detailsPanel, "Admission Date:",
            student.getAdmissionDate() != null ? student.getAdmissionDate().toString() : "N/A");

        panel.add(detailsPanel, BorderLayout.CENTER);

        return panel;
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel valueComp = new JLabel(value != null ? value : "N/A");
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(labelComp);
        panel.add(valueComp);
    }

    private JPanel createAttendanceViewPanel() {
        return new ViewAttendancePanel(student);
    }

    private JPanel createFeeViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Fee Status");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Fee details will be loaded from database
        JPanel feePanel = new JPanel();
        feePanel.setLayout(new BoxLayout(feePanel, BoxLayout.Y_AXIS));
        feePanel.setBackground(Color.WHITE);

        FeeDAO feeDAO = new FeeDAO();
        var fees = feeDAO.getFeeByStudent(student.getStudentId());

        if (fees.isEmpty()) {
            JLabel noFeeLabel = new JLabel("No fee records found");
            noFeeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            feePanel.add(noFeeLabel);
        } else {
            for (var fee : fees) {
                JPanel feeCard = new JPanel(new GridLayout(0, 2, 10, 10));
                feeCard.setBackground(Color.WHITE);
                feeCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                feeCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

                addDetailRow(feeCard, "Semester:", String.valueOf(fee.getSemester()));
                addDetailRow(feeCard, "Total Amount:", "₹" + fee.getTotalAmount());
                addDetailRow(feeCard, "Paid Amount:", "₹" + fee.getPaidAmount());
                addDetailRow(feeCard, "Due Amount:", "₹" + fee.getDueAmount());
                addDetailRow(feeCard, "Payment Status:", fee.getPaymentStatus());
                addDetailRow(feeCard, "Due Date:", fee.getDueDate().toString());

                feePanel.add(feeCard);
                feePanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(feePanel);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTimeTableViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("My Time Table");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        TimeTableDAO timeTableDAO = new TimeTableDAO();
        var timeTables = timeTableDAO.getTimeTableByClass(
            student.getCourse(),
            student.getSemester(),
            student.getSection()
        );

        if (timeTables.isEmpty()) {
            JLabel noDataLabel = new JLabel("No timetable available");
            noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            String[] columns = {"Day", "Time", "Subject", "Faculty", "Room"};
            Object[][] data = new Object[timeTables.size()][5];

            for (int i = 0; i < timeTables.size(); i++) {
                var tt = timeTables.get(i);
                data[i][0] = tt.getDayOfWeek();
                data[i][1] = tt.getTimeSlot();
                data[i][2] = tt.getSubject();
                data[i][3] = tt.getFacultyName();
                data[i][4] = tt.getRoomNumber();
            }

            JTable table = new JTable(data, columns);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.setRowHeight(30);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            table.getTableHeader().setBackground(Color.decode("#3498DB"));
            table.getTableHeader().setForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel createAdmitCardViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("My Admit Cards");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        AdmitCardDAO admitCardDAO = new AdmitCardDAO();
        var admitCards = admitCardDAO.getAdmitCardsByStudent(student.getStudentId());

        if (admitCards.isEmpty()) {
            JLabel noDataLabel = new JLabel("No admit cards available");
            noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            cardsPanel.setBackground(Color.WHITE);

            for (var admitCard : admitCards) {
                JPanel card = new JPanel(new GridLayout(0, 2, 10, 10));
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

                addDetailRow(card, "Exam Name:", admitCard.getExamName());
                addDetailRow(card, "Exam Date:", admitCard.getExamDate().toString());
                addDetailRow(card, "Semester:", String.valueOf(admitCard.getSemester()));
                addDetailRow(card, "Exam Center:", admitCard.getExamCenter());
                addDetailRow(card, "Seat Number:", admitCard.getSeatNumber());
                addDetailRow(card, "Status:", admitCard.getStatus());

                cardsPanel.add(card);
                cardsPanel.add(Box.createVerticalStrut(10));
            }

            JScrollPane scrollPane = new JScrollPane(cardsPanel);
            scrollPane.setBorder(null);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        return panel;
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}