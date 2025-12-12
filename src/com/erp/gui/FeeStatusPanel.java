package com.erp.gui;

import com.erp.db.FeeDAO;
import com.erp.models.Fee;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;

public class FeeStatusPanel extends JPanel {

    private FeeDAO feeDAO;
    private JTable feeTable;
    private DefaultTableModel tableModel;

    public FeeStatusPanel() {
        feeDAO = new FeeDAO();
        initComponents();
        loadFees();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Fee Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshButton = createButton("Refresh", "#3498DB");
        refreshButton.addActionListener(e -> loadFees());

        JButton pendingButton = createButton("Pending Fees", "#F39C12");
        pendingButton.addActionListener(e -> loadPendingFees());

        buttonPanel.add(pendingButton);
        buttonPanel.add(refreshButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Roll No", "Student Name", "Semester", "Total Amount", "Paid Amount", "Due Amount", "Status", "Due Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        feeTable = new JTable(tableModel);
        feeTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        feeTable.setRowHeight(35);
        feeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        feeTable.getTableHeader().setBackground(Color.decode("#3498DB"));
        feeTable.getTableHeader().setForeground(Color.WHITE);

        // Add row coloring based on status
        feeTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    String status = (String) table.getValueAt(row, 6);
                    if ("Paid".equals(status)) {
                        c.setBackground(new Color(39, 174, 96, 30));
                    } else if ("Overdue".equals(status)) {
                        c.setBackground(new Color(231, 76, 60, 30));
                    } else if ("Partial".equals(status)) {
                        c.setBackground(new Color(243, 156, 18, 30));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(feeTable);
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
        button.setPreferredSize(new Dimension(130, 35));
        return button;
    }

    private void loadFees() {
        tableModel.setRowCount(0);
        var fees = feeDAO.getAllFees();

        for (Fee fee : fees) {
            Object[] row = {
                fee.getRollNumber(),
                fee.getStudentName(),
                fee.getSemester(),
                "₹" + fee.getTotalAmount(),
                "₹" + fee.getPaidAmount(),
                "₹" + fee.getDueAmount(),
                fee.getPaymentStatus(),
                fee.getDueDate().toString()
            };
            tableModel.addRow(row);
        }
    }

    private void loadPendingFees() {
        tableModel.setRowCount(0);
        var fees = feeDAO.getPendingFees();

        for (Fee fee : fees) {
            Object[] row = {
                fee.getRollNumber(),
                fee.getStudentName(),
                fee.getSemester(),
                "₹" + fee.getTotalAmount(),
                "₹" + fee.getPaidAmount(),
                "₹" + fee.getDueAmount(),
                fee.getPaymentStatus(),
                fee.getDueDate().toString()
            };
            tableModel.addRow(row);
        }
    }
}