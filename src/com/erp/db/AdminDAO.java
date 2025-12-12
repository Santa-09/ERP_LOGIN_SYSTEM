package com.erp.db;

import com.erp.models.Admin;
import com.erp.utils.LoggerUtil;
import java.sql.*;

public class AdminDAO {

    /**
     * Authenticate admin user
     */
    public Admin authenticate(String username, String password) {
        String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
                admin.setFullName(rs.getString("full_name"));
                admin.setEmail(rs.getString("email"));
                
                LoggerUtil.info("AdminDAO", "Admin authenticated successfully: " + username);
                return admin;
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("AdminDAO", "Error authenticating admin", e);
        }
        
        return null;
    }

    /**
     * Get admin by ID
     */
    public Admin getAdminById(int adminId) {
        String query = "SELECT * FROM admin WHERE admin_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, adminId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
                admin.setFullName(rs.getString("full_name"));
                admin.setEmail(rs.getString("email"));
                return admin;
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("AdminDAO", "Error getting admin by ID", e);
        }
        
        return null;
    }

    /**
     * Check if username exists
     */
    public boolean usernameExists(String username) {
        String query = "SELECT COUNT(*) FROM admin WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("AdminDAO", "Error checking username existence", e);
        }
        
        return false;
    }

    /**
     * Update admin password
     */
    public boolean updatePassword(int adminId, String newPassword) {
        String query = "UPDATE admin SET password = ? WHERE admin_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, adminId);
            
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("AdminDAO", "Admin password updated: " + adminId);
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("AdminDAO", "Error updating admin password", e);
        }
        
        return false;
    }
}