package com.erp.gui;

import com.erp.db.AdmitCardDAO;
import com.erp.models.AdmitCard;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdmitCardPanel extends JPanel {

    private AdmitCardDAO admitCardDAO;
    private JTable admitCardTable;
    private DefaultTableModel tableModel;

    public AdmitCardPanel() {
        admitCardDAO = new AdmitCardDAO();
        initComponents();
        loadAdmitCards();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Admit Card Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshButton = createButton("Refresh", "#3498DB");
        refreshButton.addActionListener(e -> loadAdmitCards());

        JButton activeButton = createButton("Active Cards", "#27AE60");
        activeButton.addActionListener(e -> loadActiveAdmitCards());

        buttonPanel.add(activeButton);
        buttonPanel.add(refreshButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Roll No", "Student Name", "Exam Name", "Exam Date", "Semester", "Exam Center", "Seat No", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        admitCardTable = new JTable(tableModel);
        admitCardTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        admitCardTable.setRowHeight(35);
        admitCardTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        admitCardTable.getTableHeader().setBackground(Color.decode("#3498DB"));
        admitCardTable.getTableHeader().setForeground(Color.WHITE);

        // Add row coloring based on status
        admitCardTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    String status = (String) table.getValueAt(row, 7);
                    if ("Active".equals(status)) {
                        c.setBackground(new Color(39, 174, 96, 30));
                    } else if ("Expired".equals(status)) {
                        c.setBackground(new Color(149, 165, 166, 30));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(admitCardTable);
        add(scrollPane, BorderLayout.CENTER);
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

    private void loadAdmitCards() {
        tableModel.setRowCount(0);
        var admitCards = admitCardDAO.getAllAdmitCards();

        for (AdmitCard ac : admitCards) {
            Object[] row = {
                ac.getRollNumber(),
                ac.getStudentName(),
                ac.getExamName(),
                ac.getExamDate().toString(),
                ac.getSemester(),
                ac.getExamCenter(),
                ac.getSeatNumber(),
                ac.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void loadActiveAdmitCards() {
        tableModel.setRowCount(0);
        var admitCards = admitCardDAO.getActiveAdmitCards();

        for (AdmitCard ac : admitCards) {
            Object[] row = {
                ac.getRollNumber(),
                ac.getStudentName(),
                ac.getExamName(),
                ac.getExamDate().toString(),
                ac.getSemester(),
                ac.getExamCenter(),
                ac.getSeatNumber(),
                ac.getStatus()
            };
            tableModel.addRow(row);
        }
    }
}