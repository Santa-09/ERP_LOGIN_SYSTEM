package com.erp.gui;

import com.erp.db.AttendanceDAO;
import com.erp.models.Attendance;
import com.erp.models.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewAttendancePanel extends JPanel {

    private Student student;
    private AttendanceDAO attendanceDAO;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JLabel percentageLabel;

    public ViewAttendancePanel(Student student) {
        this.student = student;
        attendanceDAO = new AttendanceDAO();
        initComponents();
        loadAttendance();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("My Attendance");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        percentageLabel = new JLabel();
        percentageLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(percentageLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Date", "Subject", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        attendanceTable = new JTable(tableModel);
        attendanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        attendanceTable.setRowHeight(35);
        attendanceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        attendanceTable.getTableHeader().setBackground(Color.decode("#3498DB"));
        attendanceTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadAttendance() {
        tableModel.setRowCount(0);
        var attendanceList = attendanceDAO.getAttendanceByStudent(student.getStudentId());

        int presentCount = 0;
        int totalCount = attendanceList.size();

        for (Attendance attendance : attendanceList) {
            Object[] row = {
                attendance.getAttendanceDate().toString(),
                attendance.getSubject(),
                attendance.getStatus()
            };
            tableModel.addRow(row);

            if ("Present".equals(attendance.getStatus())) {
                presentCount++;
            }
        }

        // Calculate and display percentage
        if (totalCount > 0) {
            double percentage = (presentCount * 100.0) / totalCount;
            percentageLabel.setText(String.format("Attendance: %.2f%% (%d/%d)", percentage, presentCount, totalCount));

            if (percentage >= 75) {
                percentageLabel.setForeground(Color.decode("#27AE60"));
            } else if (percentage >= 60) {
                percentageLabel.setForeground(Color.decode("#F39C12"));
            } else {
                percentageLabel.setForeground(Color.decode("#E74C3C"));
            }
        } else {
            percentageLabel.setText("No attendance records found");
            percentageLabel.setForeground(Color.GRAY);
        }
    }
}