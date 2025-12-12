package com.erp.db;

import com.erp.models.Attendance;
import com.erp.utils.LoggerUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    /**
     * Mark attendance
     */
    public boolean markAttendance(Attendance attendance) {
        String query = "INSERT INTO attendance (student_id, attendance_date, subject, status, marked_by) " +
                      "VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE status = ?, marked_by = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, attendance.getStudentId());
            pstmt.setDate(2, Date.valueOf(attendance.getAttendanceDate()));
            pstmt.setString(3, attendance.getSubject());
            pstmt.setString(4, attendance.getStatus());
            pstmt.setInt(5, attendance.getMarkedBy());
            pstmt.setString(6, attendance.getStatus());
            pstmt.setInt(7, attendance.getMarkedBy());
            
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("AttendanceDAO", "Attendance marked for student: " + attendance.getStudentId());
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("AttendanceDAO", "Error marking attendance", e);
        }
        
        return false;
    }

    /**
     * Get attendance by student ID
     */
    public List<Attendance> getAttendanceByStudent(int studentId) {
        List<Attendance> attendanceList = new ArrayList<>();
        String query = "SELECT a.*, s.full_name, s.roll_number FROM attendance a " +
                      "JOIN student s ON a.student_id = s.student_id " +
                      "WHERE a.student_id = ? ORDER BY a.attendance_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                attendanceList.add(extractAttendanceFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("AttendanceDAO", "Error getting attendance by student", e);
        }
        
        return attendanceList;
    }

    /**
     * Get all attendance records
     */
    public List<Attendance> getAllAttendance() {
        List<Attendance> attendanceList = new ArrayList<>();
        String query = "SELECT a.*, s.full_name, s.roll_number FROM attendance a " +
                      "JOIN student s ON a.student_id = s.student_id " +
                      "ORDER BY a.attendance_date DESC, s.roll_number";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                attendanceList.add(extractAttendanceFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("AttendanceDAO", "Error getting all attendance", e);
        }
        
        return attendanceList;
    }

    /**
     * Get attendance by date
     */
    public List<Attendance> getAttendanceByDate(Date date) {
        List<Attendance> attendanceList = new ArrayList<>();
        String query = "SELECT a.*, s.full_name, s.roll_number FROM attendance a " +
                      "JOIN student s ON a.student_id = s.student_id " +
                      "WHERE a.attendance_date = ? ORDER BY s.roll_number";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setDate(1, date);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                attendanceList.add(extractAttendanceFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("AttendanceDAO", "Error getting attendance by date", e);
        }
        
        return attendanceList;
    }

    /**
     * Get attendance percentage for a student
     */
    public double getAttendancePercentage(int studentId) {
        String query = "SELECT " +
                      "COUNT(*) as total, " +
                      "SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) as present " +
                      "FROM attendance WHERE student_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int total = rs.getInt("total");
                int present = rs.getInt("present");
                
                if (total > 0) {
                    return (present * 100.0) / total;
                }
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("AttendanceDAO", "Error calculating attendance percentage", e);
        }
        
        return 0.0;
    }

    /**
     * Delete attendance record
     */
    public boolean deleteAttendance(int attendanceId) {
        String query = "DELETE FROM attendance WHERE attendance_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, attendanceId);
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("AttendanceDAO", "Attendance deleted: " + attendanceId);
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("AttendanceDAO", "Error deleting attendance", e);
        }
        
        return false;
    }

    /**
     * Extract Attendance object from ResultSet
     */
    private Attendance extractAttendanceFromResultSet(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setAttendanceId(rs.getInt("attendance_id"));
        attendance.setStudentId(rs.getInt("student_id"));
        
        Date date = rs.getDate("attendance_date");
        if (date != null) {
            attendance.setAttendanceDate(date.toLocalDate());
        }
        
        attendance.setSubject(rs.getString("subject"));
        attendance.setStatus(rs.getString("status"));
        attendance.setMarkedBy(rs.getInt("marked_by"));
        
        // Additional fields
        attendance.setStudentName(rs.getString("full_name"));
        attendance.setRollNumber(rs.getString("roll_number"));
        
        return attendance;
    }
}