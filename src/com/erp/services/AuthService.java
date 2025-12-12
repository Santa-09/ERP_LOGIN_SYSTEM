package com.erp.services;

import com.erp.db.AdminDAO;
import com.erp.db.StudentDAO;
import com.erp.models.Admin;
import com.erp.models.Student;
import com.erp.utils.LoggerUtil;

public class AuthService {
    
    private AdminDAO adminDAO;
    private StudentDAO studentDAO;
    
    public AuthService() {
        this.adminDAO = new AdminDAO();
        this.studentDAO = new StudentDAO();
    }
    
    /**
     * Authenticate admin user
     */
    public Admin authenticateAdmin(String username, String password) {
        try {
            LoggerUtil.info("AuthService", "Authenticating admin: " + username);
            return adminDAO.authenticate(username, password);
        } catch (Exception e) {
            LoggerUtil.error("AuthService", "Error authenticating admin", e);
            return null;
        }
    }
    
    /**
     * Authenticate student user
     */
    public Student authenticateStudent(String rollNumber, String password) {
        try {
            LoggerUtil.info("AuthService", "Authenticating student: " + rollNumber);
            return studentDAO.authenticate(rollNumber, password);
        } catch (Exception e) {
            LoggerUtil.error("AuthService", "Error authenticating student", e);
            return null;
        }
    }
    
    /**
     * Check if admin username exists
     */
    public boolean adminUsernameExists(String username) {
        try {
            return adminDAO.usernameExists(username);
        } catch (Exception e) {
            LoggerUtil.error("AuthService", "Error checking admin username", e);
            return false;
        }
    }
    
    /**
     * Update admin password
     */
    public boolean updateAdminPassword(int adminId, String newPassword) {
        try {
            LoggerUtil.info("AuthService", "Updating admin password: " + adminId);
            return adminDAO.updatePassword(adminId, newPassword);
        } catch (Exception e) {
            LoggerUtil.error("AuthService", "Error updating admin password", e);
            return false;
        }
    }
}