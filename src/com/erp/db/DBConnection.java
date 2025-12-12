package com.erp.db;

import com.erp.utils.Config;
import com.erp.utils.LoggerUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    private static Connection connection = null;

    /**
     * Get database connection
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(Config.DB_DRIVER);
                connection = DriverManager.getConnection(
                    Config.DB_URL, 
                    Config.DB_USER, 
                    Config.DB_PASSWORD
                );
                LoggerUtil.info("DBConnection", "Database connection established successfully");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            LoggerUtil.error("DBConnection", "Database driver not found", e);
            return null;
        } catch (SQLException e) {
            LoggerUtil.error("DBConnection", "Failed to establish database connection", e);
            return null;
        }
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LoggerUtil.info("DBConnection", "Database connection closed successfully");
            }
        } catch (SQLException e) {
            LoggerUtil.error("DBConnection", "Failed to close database connection", e);
        }
    }

    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            LoggerUtil.error("DBConnection", "Connection test failed", e);
            return false;
        }
    }

    private DBConnection() {
        // Private constructor to prevent instantiation
    }
}