package com.erp.services;

import com.erp.db.AttendanceDAO;
import com.erp.models.Attendance;
import com.erp.utils.LoggerUtil;
import java.sql.Date;
import java.util.List;

public class AttendanceService {
    
    private AttendanceDAO attendanceDAO;
    
    public AttendanceService() {
        this.attendanceDAO = new AttendanceDAO();
    }
    
    /**
     * Mark attendance
     */
    public boolean markAttendance(Attendance attendance) {
        try {
            LoggerUtil.info("AttendanceService", "Marking attendance for student: " + attendance.getStudentId());
            return attendanceDAO.markAttendance(attendance);
        } catch (Exception e) {
            LoggerUtil.error("AttendanceService", "Error marking attendance", e);
            return false;
        }
    }
    
    /**
     * Get attendance by student ID
     */
    public List<Attendance> getAttendanceByStudent(int studentId) {
        try {
            LoggerUtil.info("AttendanceService", "Fetching attendance for student: " + studentId);
            return attendanceDAO.getAttendanceByStudent(studentId);
        } catch (Exception e) {
            LoggerUtil.error("AttendanceService", "Error fetching attendance by student", e);
            return List.of();
        }
    }
    
    /**
     * Get all attendance records
     */
    public List<Attendance> getAllAttendance() {
        try {
            LoggerUtil.info("AttendanceService", "Fetching all attendance records");
            return attendanceDAO.getAllAttendance();
        } catch (Exception e) {
            LoggerUtil.error("AttendanceService", "Error fetching all attendance", e);
            return List.of();
        }
    }
    
    /**
     * Get attendance by date
     */
    public List<Attendance> getAttendanceByDate(Date date) {
        try {
            LoggerUtil.info("AttendanceService", "Fetching attendance for date: " + date);
            return attendanceDAO.getAttendanceByDate(date);
        } catch (Exception e) {
            LoggerUtil.error("AttendanceService", "Error fetching attendance by date", e);
            return List.of();
        }
    }
    
    /**
     * Get attendance percentage for a student
     */
    public double getAttendancePercentage(int studentId) {
        try {
            LoggerUtil.info("AttendanceService", "Calculating attendance percentage for student: " + studentId);
            return attendanceDAO.getAttendancePercentage(studentId);
        } catch (Exception e) {
            LoggerUtil.error("AttendanceService", "Error calculating attendance percentage", e);
            return 0.0;
        }
    }
    
    /**
     * Delete attendance record
     */
    public boolean deleteAttendance(int attendanceId) {
        try {
            LoggerUtil.info("AttendanceService", "Deleting attendance record: " + attendanceId);
            return attendanceDAO.deleteAttendance(attendanceId);
        } catch (Exception e) {
            LoggerUtil.error("AttendanceService", "Error deleting attendance", e);
            return false;
        }
    }
    
    /**
     * Validate attendance data
     */
    public String validateAttendance(Attendance attendance) {
        if (attendance.getStudentId() <= 0) {
            return "Invalid student ID";
        }
        if (attendance.getSubject() == null || attendance.getSubject().trim().isEmpty()) {
            return "Subject is required";
        }
        if (attendance.getAttendanceDate() == null) {
            return "Attendance date is required";
        }
        if (attendance.getStatus() == null || attendance.getStatus().trim().isEmpty()) {
            return "Status is required";
        }
        if (attendance.getMarkedBy() <= 0) {
            return "Invalid marked by admin ID";
        }
        return null; // No validation errors
    }
}