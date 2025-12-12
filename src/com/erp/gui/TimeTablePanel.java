package com.erp.gui;

import com.erp.db.TimeTableDAO;
import com.erp.models.TimeTable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TimeTablePanel extends JPanel {

    private TimeTableDAO timeTableDAO;
    private JTable timeTableTable;
    private DefaultTableModel tableModel;
    private JTextField courseField;
    private JSpinner semesterSpinner;
    private JTextField sectionField;

    public TimeTablePanel() {
        timeTableDAO = new TimeTableDAO();
        initComponents();
        loadAllTimeTables();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Time Table Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createButton("Add Entry", "#27AE60");
        addButton.addActionListener(e -> showAddTimeTableDialog());

        JButton refreshButton = createButton("Refresh", "#3498DB");
        refreshButton.addActionListener(e -> loadAllTimeTables());

        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        filterPanel.add(new JLabel("Course:"));
        courseField = new JTextField(15);
        filterPanel.add(courseField);

        filterPanel.add(new JLabel("Semester:"));
        semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        filterPanel.add(semesterSpinner);

        filterPanel.add(new JLabel("Section:"));
        sectionField = new JTextField(5);
        filterPanel.add(sectionField);

        JButton filterButton = createButton("Filter", "#3498DB");
        filterButton.addActionListener(e -> filterTimeTable());
        filterPanel.add(filterButton);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(filterPanel, BorderLayout.NORTH);

        String[] columns = {"Course", "Semester", "Section", "Day", "Time", "Subject", "Faculty", "Room"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        timeTableTable = new JTable(tableModel);
        timeTableTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        timeTableTable.setRowHeight(35);
        timeTableTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        timeTableTable.getTableHeader().setBackground(Color.decode("#3498DB"));
        timeTableTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(timeTableTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);
    }

    private JButton createButton(String text, String colorHex) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setBackground(Color.decode(colorHex));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(110, 35));
        return button;
    }

    private void loadAllTimeTables() {
        tableModel.setRowCount(0);
        var timeTables = timeTableDAO.getAllTimeTables();

        for (TimeTable tt : timeTables) {
            Object[] row = {
                tt.getCourse(),
                tt.getSemester(),
                tt.getSection(),
                tt.getDayOfWeek(),
                tt.getTimeSlot(),
                tt.getSubject(),
                tt.getFacultyName(),
                tt.getRoomNumber()
            };
            tableModel.addRow(row);
        }
    }

    private void filterTimeTable() {
        String course = courseField.getText().trim();
        int semester = (Integer) semesterSpinner.getValue();
        String section = sectionField.getText().trim();

        if (course.isEmpty() || section.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter course and section", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        var timeTables = timeTableDAO.getTimeTableByClass(course, semester, section);

        for (TimeTable tt : timeTables) {
            Object[] row = {
                tt.getCourse(),
                tt.getSemester(),
                tt.getSection(),
                tt.getDayOfWeek(),
                tt.getTimeSlot(),
                tt.getSubject(),
                tt.getFacultyName(),
                tt.getRoomNumber()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddTimeTableDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Time Table Entry", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField courseFieldDialog = new JTextField(20);
        JSpinner semesterSpinnerDialog = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        JTextField sectionFieldDialog = new JTextField(20);
        JComboBox<String> dayCombo = new JComboBox<>(new String[]{
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
        });
        JTextField timeSlotField = new JTextField(20);
        JTextField subjectField = new JTextField(20);
        JTextField facultyField = new JTextField(20);
        JTextField roomField = new JTextField(20);

        int row = 0;
        addFormField(formPanel, gbc, row++, "Course:", courseFieldDialog);
        addFormField(formPanel, gbc, row++, "Semester:", semesterSpinnerDialog);
        addFormField(formPanel, gbc, row++, "Section:", sectionFieldDialog);
        addFormField(formPanel, gbc, row++, "Day:", dayCombo);
        addFormField(formPanel, gbc, row++, "Time Slot:", timeSlotField);
        addFormField(formPanel, gbc, row++, "Subject:", subjectField);
        addFormField(formPanel, gbc, row++, "Faculty Name:", facultyField);
        addFormField(formPanel, gbc, row++, "Room Number:", roomField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = createButton("Save", "#27AE60");
        JButton cancelButton = createButton("Cancel", "#E74C3C");

        saveButton.addActionListener(e -> {
            TimeTable timeTable = new TimeTable();
            timeTable.setCourse(courseFieldDialog.getText().trim());
            timeTable.setSemester((Integer) semesterSpinnerDialog.getValue());
            timeTable.setSection(sectionFieldDialog.getText().trim());
            timeTable.setDayOfWeek((String) dayCombo.getSelectedItem());
            timeTable.setTimeSlot(timeSlotField.getText().trim());
            timeTable.setSubject(subjectField.getText().trim());
            timeTable.setFacultyName(facultyField.getText().trim());
            timeTable.setRoomNumber(roomField.getText().trim());

            if (timeTableDAO.addTimeTable(timeTable)) {
                JOptionPane.showMessageDialog(dialog, "Time table entry added successfully!");
                dialog.dispose();
                loadAllTimeTables();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add time table entry", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(field, gbc);
    }
}