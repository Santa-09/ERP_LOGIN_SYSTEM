package com.erp.gui;

import com.erp.db.StudentDAO;
import com.erp.models.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class StudentDetailsPanel extends JPanel {

    private StudentDAO studentDAO;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public StudentDetailsPanel() {
        studentDAO = new StudentDAO();
        initComponents();
        loadStudents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Student Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createButton("Add Student", "#27AE60");
        addButton.addActionListener(e -> showAddStudentDialog());

        JButton refreshButton = createButton("Refresh", "#3498DB");
        refreshButton.addActionListener(e -> loadStudents());

        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        searchField = new JTextField(30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton searchButton = createButton("Search", "#3498DB");
        searchButton.addActionListener(e -> searchStudents());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Roll Number", "Name", "Email", "Phone", "Course", "Semester", "Section", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Only Actions column is editable
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setRowHeight(35);
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        studentTable.getTableHeader().setBackground(Color.decode("#3498DB"));
        studentTable.getTableHeader().setForeground(Color.WHITE);

        // Add button renderer and editor for Actions column
        studentTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        studentTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(studentTable);
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
        button.setPreferredSize(new Dimension(120, 35));
        return button;
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        var students = studentDAO.getAllStudents();

        for (Student student : students) {
            Object[] row = {
                student.getStudentId(),
                student.getRollNumber(),
                student.getFullName(),
                student.getEmail(),
                student.getPhone(),
                student.getCourse(),
                student.getSemester(),
                student.getSection(),
                "Edit | Delete"
            };
            tableModel.addRow(row);
        }
    }

    private void searchStudents() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            loadStudents();
            return;
        }

        tableModel.setRowCount(0);
        var students = studentDAO.searchStudents(keyword);

        for (Student student : students) {
            Object[] row = {
                student.getStudentId(),
                student.getRollNumber(),
                student.getFullName(),
                student.getEmail(),
                student.getPhone(),
                student.getCourse(),
                student.getSemester(),
                student.getSection(),
                "Edit | Delete"
            };
            tableModel.addRow(row);
        }
    }

    private void showAddStudentDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Student", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField rollNumberField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField courseField = new JTextField(20);
        JSpinner semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        JTextField sectionField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        int row = 0;
        addFormField(formPanel, gbc, row++, "Roll Number:", rollNumberField);
        addFormField(formPanel, gbc, row++, "Full Name:", nameField);
        addFormField(formPanel, gbc, row++, "Email:", emailField);
        addFormField(formPanel, gbc, row++, "Phone:", phoneField);
        addFormField(formPanel, gbc, row++, "Course:", courseField);
        addFormField(formPanel, gbc, row++, "Semester:", semesterSpinner);
        addFormField(formPanel, gbc, row++, "Section:", sectionField);
        addFormField(formPanel, gbc, row++, "Password:", passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = createButton("Save", "#27AE60");
        JButton cancelButton = createButton("Cancel", "#E74C3C");

        saveButton.addActionListener(e -> {
            Student student = new Student();
            student.setRollNumber(rollNumberField.getText().trim());
            student.setPassword(new String(passwordField.getPassword()));
            student.setFullName(nameField.getText().trim());
            student.setEmail(emailField.getText().trim());
            student.setPhone(phoneField.getText().trim());
            student.setCourse(courseField.getText().trim());
            student.setSemester((Integer) semesterSpinner.getValue());
            student.setSection(sectionField.getText().trim());
            student.setAdmissionDate(LocalDate.now());

            if (studentDAO.addStudent(student)) {
                JOptionPane.showMessageDialog(dialog, "Student added successfully!");
                dialog.dispose();
                loadStudents();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add student", "Error", JOptionPane.ERROR_MESSAGE);
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

    // Button Renderer for table
    class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();
            JButton editBtn = new JButton("Edit");
            JButton deleteBtn = new JButton("Delete");

            editBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            deleteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));

            add(editBtn);
            add(deleteBtn);
            return this;
        }
    }

    // Button Editor for table
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton editBtn;
        private JButton deleteBtn;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

            editBtn = new JButton("Edit");
            deleteBtn = new JButton("Delete");

            editBtn.addActionListener(e -> editStudent());
            deleteBtn.addActionListener(e -> deleteStudent());

            panel.add(editBtn);
            panel.add(deleteBtn);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            return panel;
        }

        private void editStudent() {
            int row = studentTable.getSelectedRow();
            if (row >= 0) {
                int studentId = (Integer) tableModel.getValueAt(row, 0);
                // Implement edit functionality
                JOptionPane.showMessageDialog(null, "Edit student ID: " + studentId);
            }
            fireEditingStopped();
        }

        private void deleteStudent() {
            int row = studentTable.getSelectedRow();
            if (row >= 0) {
                int studentId = (Integer) tableModel.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this student?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (studentDAO.deleteStudent(studentId)) {
                        JOptionPane.showMessageDialog(null, "Student deleted successfully!");
                        loadStudents();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete student", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            fireEditingStopped();
        }
    }
}