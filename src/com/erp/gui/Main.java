package com.erp.gui;

import com.erp.db.DBConnection;
import com.erp.utils.Config;
import com.erp.utils.LoggerUtil;
import javax.swing.*;

public class Main {
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LoggerUtil.warning("Main", "Failed to set system look and feel");
        }
        
        // Test database connection
        LoggerUtil.info("Main", "Starting ERP System...");
        LoggerUtil.info("Main", "Application: " + Config.APP_NAME + " v" + Config.APP_VERSION);
        
        if (!DBConnection.testConnection()) {
            JOptionPane.showMessageDialog(null,
                "Failed to connect to database!\nPlease check your database configuration.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            LoggerUtil.error("Main", "Database connection failed. Exiting application.");
            System.exit(1);
        }
        
        LoggerUtil.info("Main", "Database connection successful");
        
        // Launch login frame
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            LoggerUtil.info("Main", "Login frame displayed");
        });
    }
}