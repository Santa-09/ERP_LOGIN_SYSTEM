package com.erp.gui;

import com.erp.db.AttendanceDAO;
import com.erp.db.StudentDAO;
import com.erp.models.Admin;
import com.erp.models.Attendance;
import com.erp.models.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.sql.Date;

public class AttendancePanel extends JPanel {

    private Admin admin;
    private AttendanceDAO attendanceDAO;
    private StudentDAO studentDAO;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> courseCombo;
    private JSpinner semesterSpinner;
    private JTextField sectionField;
    private JTextField subjectField;
    private JSpinner dateSpinner;

    public AttendancePanel(Admin admin) {
        this.admin = admin;
        attendanceDAO = new AttendanceDAO();
        studentDAO = new StudentDAO();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Attendance Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JButton viewAllButton = createButton("View All Records", "#3498DB");
        viewAllButton.addActionListener(e -> loadAllAttendance());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(viewAllButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Filter Panel
        JPanel filterPanel = createFilterPanel();

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(filterPanel, BorderLayout.NORTH);

        String[] columns = {"Roll No", "Student Name", "Subject", "Date", "Status", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        attendanceTable = new JTable(tableModel);
        attendanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        attendanceTable.setRowHeight(35);
        attendanceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        attendanceTable.getTableHeader().setBackground(Color.decode("#3498DB"));
        attendanceTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // First row
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.setBackground(Color.WHITE);

        row1.add(new JLabel("Subject:"));
        subjectField = new JTextField(20);
        row1.add(subjectField);

        row1.add(new JLabel("Date:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new java.util.Date());
        row1.add(dateSpinner);

        JButton loadStudentsButton = createButton("Load Students", "#27AE60");
        loadStudentsButton.addActionListener(e -> loadStudentsForAttendance());
        row1.add(loadStudentsButton);

        JButton markAllPresentButton = createButton("Mark All Present", "#3498DB");
        markAllPresentButton.addActionListener(e -> markAllPresent());
        row1.add(markAllPresentButton);

        panel.add(row1);

        return panel;
    }

    private JButton createButton(String text, String colorHex) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(Color.decode(colorHex));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadStudentsForAttendance() {
        String subject = subjectField.getText().trim();

        if (subject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter subject name", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        var students = studentDAO.getAllStudents();

        java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();
        LocalDate selectedDate = new java.sql.Date(utilDate.getTime()).toLocalDate();

        for (Student student : students) {
            Object[] row = {
                student.getRollNumber(),
                student.getFullName(),
                subject,
                selectedDate.toString(),
                "Present",  // Default status
                student.getStudentId()
            };
            tableModel.addRow(row);
        }

        // Add combobox for status
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Present", "Absent", "Late"});
        attendanceTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusCombo));

        // Add save button
        JButton saveButton = createButton("Save Attendance", "#27AE60");
        saveButton.addActionListener(e -> saveAttendance());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);

        if (getComponentCount() > 2) {
            remove(2);
        }
        add(buttonPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void markAllPresent() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt("Present", i, 4);
        }
    }

    private void saveAttendance() {
        int savedCount = 0;
        int failedCount = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                int studentId = (Integer) tableModel.getValueAt(i, 5);
                String subject = (String) tableModel.getValueAt(i, 2);
                String dateStr = (String) tableModel.getValueAt(i, 3);
                String status = (String) tableModel.getValueAt(i, 4);

                Attendance attendance = new Attendance();
                attendance.setStudentId(studentId);
                attendance.setSubject(subject);
                attendance.setAttendanceDate(LocalDate.parse(dateStr));
                attendance.setStatus(status);
                attendance.setMarkedBy(admin.getAdminId());

                if (attendanceDAO.markAttendance(attendance)) {
                    savedCount++;
                } else {
                    failedCount++;
                }
            } catch (Exception e) {
                failedCount++;
            }
        }

        JOptionPane.showMessageDialog(this,
            "Attendance saved!\nSuccess: " + savedCount + "\nFailed: " + failedCount,
            "Save Complete",
            JOptionPane.INFORMATION_MESSAGE);

        tableModel.setRowCount(0);
        if (getComponentCount() > 2) {
            remove(2);
            revalidate();
            repaint();
        }
    }

    private void loadAllAttendance() {
        tableModel.setRowCount(0);
        var attendanceList = attendanceDAO.getAllAttendance();

        for (Attendance attendance : attendanceList) {
            Object[] row = {
                attendance.getRollNumber(),
                attendance.getStudentName(),
                attendance.getSubject(),
                attendance.getAttendanceDate().toString(),
                attendance.getStatus(),
                attendance.getStudentId()
            };
            tableModel.addRow(row);
        }

        if (getComponentCount() > 2) {
            remove(2);
            revalidate();
            repaint();
        }
    }
}