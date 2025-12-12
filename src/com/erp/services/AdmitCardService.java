package com.erp.services;

import com.erp.db.AdmitCardDAO;
import com.erp.models.AdmitCard;
import com.erp.utils.LoggerUtil;
import java.util.List;

public class AdmitCardService {
    
    private AdmitCardDAO admitCardDAO;
    
    public AdmitCardService() {
        this.admitCardDAO = new AdmitCardDAO();
    }
    
    /**
     * Add admit card
     */
    public boolean addAdmitCard(AdmitCard admitCard) {
        try {
            LoggerUtil.info("AdmitCardService", "Adding admit card for student: " + admitCard.getStudentId());
            return admitCardDAO.addAdmitCard(admitCard);
        } catch (Exception e) {
            LoggerUtil.error("AdmitCardService", "Error adding admit card", e);
            return false;
        }
    }
    
    /**
     * Update admit card
     */
    public boolean updateAdmitCard(AdmitCard admitCard) {
        try {
            LoggerUtil.info("AdmitCardService", "Updating admit card: " + admitCard.getAdmitCardId());
            return admitCardDAO.updateAdmitCard(admitCard);
        } catch (Exception e) {
            LoggerUtil.error("AdmitCardService", "Error updating admit card", e);
            return false;
        }
    }
    
    /**
     * Get admit cards by student ID
     */
    public List<AdmitCard> getAdmitCardsByStudent(int studentId) {
        try {
            LoggerUtil.info("AdmitCardService", "Fetching admit cards for student: " + studentId);
            return admitCardDAO.getAdmitCardsByStudent(studentId);
        } catch (Exception e) {
            LoggerUtil.error("AdmitCardService", "Error fetching admit cards by student", e);
            return List.of();
        }
    }
    
    /**
     * Get all admit cards
     */
    public List<AdmitCard> getAllAdmitCards() {
        try {
            LoggerUtil.info("AdmitCardService", "Fetching all admit cards");
            return admitCardDAO.getAllAdmitCards();
        } catch (Exception e) {
            LoggerUtil.error("AdmitCardService", "Error fetching all admit cards", e);
            return List.of();
        }
    }
    
    /**
     * Get active admit cards
     */
    public List<AdmitCard> getActiveAdmitCards() {
        try {
            LoggerUtil.info("AdmitCardService", "Fetching active admit cards");
            return admitCardDAO.getActiveAdmitCards();
        } catch (Exception e) {
            LoggerUtil.error("AdmitCardService", "Error fetching active admit cards", e);
            return List.of();
        }
    }
    
    /**
     * Delete admit card
     */
    public boolean deleteAdmitCard(int admitCardId) {
        try {
            LoggerUtil.info("AdmitCardService", "Deleting admit card: " + admitCardId);
            return admitCardDAO.deleteAdmitCard(admitCardId);
        } catch (Exception e) {
            LoggerUtil.error("AdmitCardService", "Error deleting admit card", e);
            return false;
        }
    }
    
    /**
     * Validate admit card data
     */
    public String validateAdmitCard(AdmitCard admitCard) {
        if (admitCard.getStudentId() <= 0) {
            return "Invalid student ID";
        }
        if (admitCard.getExamName() == null || admitCard.getExamName().trim().isEmpty()) {
            return "Exam name is required";
        }
        if (admitCard.getExamDate() == null) {
            return "Exam date is required";
        }
        if (admitCard.getSemester() < 1 || admitCard.getSemester() > 8) {
            return "Semester must be between 1 and 8";
        }
        if (admitCard.getIssuedDate() == null) {
            return "Issued date is required";
        }
        return null; // No validation errors
    }
    
    /**
     * Update admit card status to expired for past exams
     */
    public boolean updateExpiredAdmitCards() {
        try {
            LoggerUtil.info("AdmitCardService", "Updating expired admit cards");
            List<AdmitCard> activeCards = getActiveAdmitCards();
            java.time.LocalDate today = java.time.LocalDate.now();
            int updatedCount = 0;
            
            for (AdmitCard card : activeCards) {
                if (card.getExamDate().isBefore(today)) {
                    card.setStatus("Expired");
                    if (updateAdmitCard(card)) {
                        updatedCount++;
                    }
                }
            }
            
            LoggerUtil.info("AdmitCardService", "Updated " + updatedCount + " expired admit cards");
            return updatedCount > 0;
        } catch (Exception e) {
            LoggerUtil.error("AdmitCardService", "Error updating expired admit cards", e);
            return false;
        }
    }
}