package com.erp.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AdmitCard {
    private int admitCardId;
    private int studentId;
    private String examName;
    private LocalDate examDate;
    private int semester;
    private String examCenter;
    private String seatNumber;
    private LocalDate issuedDate;
    private String status; // Active, Expired
    private LocalDateTime createdAt;
    
    // Additional fields for display
    private String studentName;
    private String rollNumber;
    private String course;

    // Constructors
    public AdmitCard() {}

    public AdmitCard(int studentId, String examName, LocalDate examDate, int semester,
                     String examCenter, String seatNumber, LocalDate issuedDate, String status) {
        this.studentId = studentId;
        this.examName = examName;
        this.examDate = examDate;
        this.semester = semester;
        this.examCenter = examCenter;
        this.seatNumber = seatNumber;
        this.issuedDate = issuedDate;
        this.status = status;
    }

    // Getters and Setters
    public int getAdmitCardId() {
        return admitCardId;
    }

    public void setAdmitCardId(int admitCardId) {
        this.admitCardId = admitCardId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getExamCenter() {
        return examCenter;
    }

    public void setExamCenter(String examCenter) {
        this.examCenter = examCenter;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "AdmitCard{" +
                "admitCardId=" + admitCardId +
                ", studentId=" + studentId +
                ", examName='" + examName + '\'' +
                ", examDate=" + examDate +
                ", semester=" + semester +
                ", status='" + status + '\'' +
                '}';
    }
}