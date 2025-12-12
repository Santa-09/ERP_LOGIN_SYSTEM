package com.erp.db;

import com.erp.models.Fee;
import com.erp.utils.LoggerUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeeDAO {

    /**
     * Add fee record
     */
    public boolean addFee(Fee fee) {
        String query = "INSERT INTO fee (student_id, semester, total_amount, paid_amount, " +
                      "due_amount, due_date, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, fee.getStudentId());
            pstmt.setInt(2, fee.getSemester());
            pstmt.setBigDecimal(3, fee.getTotalAmount());
            pstmt.setBigDecimal(4, fee.getPaidAmount());
            pstmt.setBigDecimal(5, fee.getDueAmount());
            pstmt.setDate(6, Date.valueOf(fee.getDueDate()));
            pstmt.setString(7, fee.getPaymentStatus());
            
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("FeeDAO", "Fee record added for student: " + fee.getStudentId());
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("FeeDAO", "Error adding fee", e);
        }
        
        return false;
    }

    /**
     * Update fee payment
     */
    public boolean updateFeePayment(Fee fee) {
        String query = "UPDATE fee SET paid_amount = ?, due_amount = ?, payment_status = ?, " +
                      "last_payment_date = ? WHERE fee_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setBigDecimal(1, fee.getPaidAmount());
            pstmt.setBigDecimal(2, fee.getDueAmount());
            pstmt.setString(3, fee.getPaymentStatus());
            pstmt.setDate(4, fee.getLastPaymentDate() != null ? 
                Date.valueOf(fee.getLastPaymentDate()) : null);
            pstmt.setInt(5, fee.getFeeId());
            
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("FeeDAO", "Fee payment updated: " + fee.getFeeId());
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("FeeDAO", "Error updating fee payment", e);
        }
        
        return false;
    }

    /**
     * Get fee records by student ID
     */
    public List<Fee> getFeeByStudent(int studentId) {
        List<Fee> feeList = new ArrayList<>();
        String query = "SELECT f.*, s.full_name, s.roll_number FROM fee f " +
                      "JOIN student s ON f.student_id = s.student_id " +
                      "WHERE f.student_id = ? ORDER BY f.semester DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                feeList.add(extractFeeFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("FeeDAO", "Error getting fee by student", e);
        }
        
        return feeList;
    }

    /**
     * Get all fee records
     */
    public List<Fee> getAllFees() {
        List<Fee> feeList = new ArrayList<>();
        String query = "SELECT f.*, s.full_name, s.roll_number FROM fee f " +
                      "JOIN student s ON f.student_id = s.student_id " +
                      "ORDER BY f.due_date DESC, s.roll_number";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                feeList.add(extractFeeFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("FeeDAO", "Error getting all fees", e);
        }
        
        return feeList;
    }

    /**
     * Get pending fees
     */
    public List<Fee> getPendingFees() {
        List<Fee> feeList = new ArrayList<>();
        String query = "SELECT f.*, s.full_name, s.roll_number FROM fee f " +
                      "JOIN student s ON f.student_id = s.student_id " +
                      "WHERE f.payment_status IN ('Pending', 'Partial', 'Overdue') " +
                      "ORDER BY f.due_date";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                feeList.add(extractFeeFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("FeeDAO", "Error getting pending fees", e);
        }
        
        return feeList;
    }

    /**
     * Delete fee record
     */
    public boolean deleteFee(int feeId) {
        String query = "DELETE FROM fee WHERE fee_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, feeId);
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("FeeDAO", "Fee deleted: " + feeId);
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("FeeDAO", "Error deleting fee", e);
        }
        
        return false;
    }

    /**
     * Extract Fee object from ResultSet
     */
    private Fee extractFeeFromResultSet(ResultSet rs) throws SQLException {
        Fee fee = new Fee();
        fee.setFeeId(rs.getInt("fee_id"));
        fee.setStudentId(rs.getInt("student_id"));
        fee.setSemester(rs.getInt("semester"));
        fee.setTotalAmount(rs.getBigDecimal("total_amount"));
        fee.setPaidAmount(rs.getBigDecimal("paid_amount"));
        fee.setDueAmount(rs.getBigDecimal("due_amount"));
        
        Date dueDate = rs.getDate("due_date");
        if (dueDate != null) {
            fee.setDueDate(dueDate.toLocalDate());
        }
        
        fee.setPaymentStatus(rs.getString("payment_status"));
        
        Date lastPaymentDate = rs.getDate("last_payment_date");
        if (lastPaymentDate != null) {
            fee.setLastPaymentDate(lastPaymentDate.toLocalDate());
        }
        
        // Additional fields
        fee.setStudentName(rs.getString("full_name"));
        fee.setRollNumber(rs.getString("roll_number"));
        
        return fee;
    }
}