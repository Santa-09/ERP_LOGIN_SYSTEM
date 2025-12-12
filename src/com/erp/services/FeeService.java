package com.erp.services;

import com.erp.db.FeeDAO;
import com.erp.models.Fee;
import com.erp.utils.LoggerUtil;
import java.util.List;

public class FeeService {
    
    private FeeDAO feeDAO;
    
    public FeeService() {
        this.feeDAO = new FeeDAO();
    }
    
    /**
     * Add fee record
     */
    public boolean addFee(Fee fee) {
        try {
            LoggerUtil.info("FeeService", "Adding fee record for student: " + fee.getStudentId());
            return feeDAO.addFee(fee);
        } catch (Exception e) {
            LoggerUtil.error("FeeService", "Error adding fee", e);
            return false;
        }
    }
    
    /**
     * Update fee payment
     */
    public boolean updateFeePayment(Fee fee) {
        try {
            LoggerUtil.info("FeeService", "Updating fee payment: " + fee.getFeeId());
            return feeDAO.updateFeePayment(fee);
        } catch (Exception e) {
            LoggerUtil.error("FeeService", "Error updating fee payment", e);
            return false;
        }
    }
    
    /**
     * Get fee records by student ID
     */
    public List<Fee> getFeeByStudent(int studentId) {
        try {
            LoggerUtil.info("FeeService", "Fetching fees for student: " + studentId);
            return feeDAO.getFeeByStudent(studentId);
        } catch (Exception e) {
            LoggerUtil.error("FeeService", "Error fetching fees by student", e);
            return List.of();
        }
    }
    
    /**
     * Get all fee records
     */
    public List<Fee> getAllFees() {
        try {
            LoggerUtil.info("FeeService", "Fetching all fee records");
            return feeDAO.getAllFees();
        } catch (Exception e) {
            LoggerUtil.error("FeeService", "Error fetching all fees", e);
            return List.of();
        }
    }
    
    /**
     * Get pending fees
     */
    public List<Fee> getPendingFees() {
        try {
            LoggerUtil.info("FeeService", "Fetching pending fees");
            return feeDAO.getPendingFees();
        } catch (Exception e) {
            LoggerUtil.error("FeeService", "Error fetching pending fees", e);
            return List.of();
        }
    }
    
    /**
     * Delete fee record
     */
    public boolean deleteFee(int feeId) {
        try {
            LoggerUtil.info("FeeService", "Deleting fee record: " + feeId);
            return feeDAO.deleteFee(feeId);
        } catch (Exception e) {
            LoggerUtil.error("FeeService", "Error deleting fee", e);
            return false;
        }
    }
    
    /**
     * Calculate fee statistics
     */
    public FeeStatistics calculateFeeStatistics() {
        try {
            List<Fee> allFees = getAllFees();
            double totalAmount = 0;
            double totalPaid = 0;
            double totalDue = 0;
            
            for (Fee fee : allFees) {
                totalAmount += fee.getTotalAmount().doubleValue();
                totalPaid += fee.getPaidAmount().doubleValue();
                totalDue += fee.getDueAmount().doubleValue();
            }
            
            return new FeeStatistics(totalAmount, totalPaid, totalDue, allFees.size());
        } catch (Exception e) {
            LoggerUtil.error("FeeService", "Error calculating fee statistics", e);
            return new FeeStatistics(0, 0, 0, 0);
        }
    }
    
    /**
     * Validate fee data
     */
    public String validateFee(Fee fee) {
        if (fee.getStudentId() <= 0) {
            return "Invalid student ID";
        }
        if (fee.getSemester() < 1 || fee.getSemester() > 8) {
            return "Semester must be between 1 and 8";
        }
        if (fee.getTotalAmount() == null || fee.getTotalAmount().doubleValue() <= 0) {
            return "Total amount must be greater than 0";
        }
        if (fee.getDueDate() == null) {
            return "Due date is required";
        }
        if (fee.getPaymentStatus() == null || fee.getPaymentStatus().trim().isEmpty()) {
            return "Payment status is required";
        }
        return null; // No validation errors
    }
    
    /**
     * Inner class for fee statistics
     */
    public static class FeeStatistics {
        public final double totalAmount;
        public final double totalPaid;
        public final double totalDue;
        public final int totalRecords;
        
        public FeeStatistics(double totalAmount, double totalPaid, double totalDue, int totalRecords) {
            this.totalAmount = totalAmount;
            this.totalPaid = totalPaid;
            this.totalDue = totalDue;
            this.totalRecords = totalRecords;
        }
    }
}