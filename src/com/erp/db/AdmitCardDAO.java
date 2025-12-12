package com.erp.db;

import com.erp.models.AdmitCard;
import com.erp.utils.LoggerUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdmitCardDAO {

    /**
     * Add admit card
     */
    public boolean addAdmitCard(AdmitCard admitCard) {
        String query = "INSERT INTO admit_card (student_id, exam_name, exam_date, semester, " +
                      "exam_center, seat_number, issued_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, admitCard.getStudentId());
            pstmt.setString(2, admitCard.getExamName());
            pstmt.setDate(3, Date.valueOf(admitCard.getExamDate()));
            pstmt.setInt(4, admitCard.getSemester());
            pstmt.setString(5, admitCard.getExamCenter());
            pstmt.setString(6, admitCard.getSeatNumber());
            pstmt.setDate(7, Date.valueOf(admitCard.getIssuedDate()));
            pstmt.setString(8, admitCard.getStatus());
            
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("AdmitCardDAO", "Admit card added for student: " + admitCard.getStudentId());
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("AdmitCardDAO", "Error adding admit card", e);
        }
        
        return false;
    }

    /**
     * Update admit card
     */
    public boolean updateAdmitCard(AdmitCard admitCard) {
        String query = "UPDATE admit_card SET exam_name = ?, exam_date = ?, semester = ?, " +
                      "exam_center = ?, seat_number = ?, status = ? WHERE admit_card_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, admitCard.getExamName());
            pstmt.setDate(2, Date.valueOf(admitCard.getExamDate()));
            pstmt.setInt(3, admitCard.getSemester());
            pstmt.setString(4, admitCard.getExamCenter());
            pstmt.setString(5, admitCard.getSeatNumber());
            pstmt.setString(6, admitCard.getStatus());
            pstmt.setInt(7, admitCard.getAdmitCardId());
            
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("AdmitCardDAO", "Admit card updated: " + admitCard.getAdmitCardId());
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("AdmitCardDAO", "Error updating admit card", e);
        }
        
        return false;
    }

    /**
     * Get admit cards by student ID
     */
    public List<AdmitCard> getAdmitCardsByStudent(int studentId) {
        List<AdmitCard> admitCardList = new ArrayList<>();
        String query = "SELECT ac.*, s.full_name, s.roll_number, s.course FROM admit_card ac " +
                      "JOIN student s ON ac.student_id = s.student_id " +
                      "WHERE ac.student_id = ? ORDER BY ac.exam_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                admitCardList.add(extractAdmitCardFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("AdmitCardDAO", "Error getting admit cards by student", e);
        }
        
        return admitCardList;
    }

    /**
     * Get all admit cards
     */
    public List<AdmitCard> getAllAdmitCards() {
        List<AdmitCard> admitCardList = new ArrayList<>();
        String query = "SELECT ac.*, s.full_name, s.roll_number, s.course FROM admit_card ac " +
                      "JOIN student s ON ac.student_id = s.student_id " +
                      "ORDER BY ac.exam_date DESC, s.roll_number";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                admitCardList.add(extractAdmitCardFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("AdmitCardDAO", "Error getting all admit cards", e);
        }
        
        return admitCardList;
    }

    /**
     * Get active admit cards
     */
    public List<AdmitCard> getActiveAdmitCards() {
        List<AdmitCard> admitCardList = new ArrayList<>();
        String query = "SELECT ac.*, s.full_name, s.roll_number, s.course FROM admit_card ac " +
                      "JOIN student s ON ac.student_id = s.student_id " +
                      "WHERE ac.status = 'Active' ORDER BY ac.exam_date";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                admitCardList.add(extractAdmitCardFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("AdmitCardDAO", "Error getting active admit cards", e);
        }
        
        return admitCardList;
    }

    /**
     * Delete admit card
     */
    public boolean deleteAdmitCard(int admitCardId) {
        String query = "DELETE FROM admit_card WHERE admit_card_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, admitCardId);
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("AdmitCardDAO", "Admit card deleted: " + admitCardId);
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("AdmitCardDAO", "Error deleting admit card", e);
        }
        
        return false;
    }

    /**
     * Extract AdmitCard object from ResultSet
     */
    private AdmitCard extractAdmitCardFromResultSet(ResultSet rs) throws SQLException {
        AdmitCard admitCard = new AdmitCard();
        admitCard.setAdmitCardId(rs.getInt("admit_card_id"));
        admitCard.setStudentId(rs.getInt("student_id"));
        admitCard.setExamName(rs.getString("exam_name"));
        
        Date examDate = rs.getDate("exam_date");
        if (examDate != null) {
            admitCard.setExamDate(examDate.toLocalDate());
        }
        
        admitCard.setSemester(rs.getInt("semester"));
        admitCard.setExamCenter(rs.getString("exam_center"));
        admitCard.setSeatNumber(rs.getString("seat_number"));
        
        Date issuedDate = rs.getDate("issued_date");
        if (issuedDate != null) {
            admitCard.setIssuedDate(issuedDate.toLocalDate());
        }
        
        admitCard.setStatus(rs.getString("status"));
        
        // Additional fields
        admitCard.setStudentName(rs.getString("full_name"));
        admitCard.setRollNumber(rs.getString("roll_number"));
        admitCard.setCourse(rs.getString("course"));
        
        return admitCard;
    }
}