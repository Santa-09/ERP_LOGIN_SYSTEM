package com.erp.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Attendance {
    private int attendanceId;
    private int studentId;
    private LocalDate attendanceDate;
    private String subject;
    private String status; // Present, Absent, Late
    private int markedBy;
    private LocalDateTime createdAt;
    
    // Additional fields for display
    private String studentName;
    private String rollNumber;

    // Constructors
    public Attendance() {}

    public Attendance(int studentId, LocalDate attendanceDate, String subject, String status) {
        this.studentId = studentId;
        this.attendanceDate = attendanceDate;
        this.subject = subject;
        this.status = status;
    }

    // Getters and Setters
    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMarkedBy() {
        return markedBy;
    }

    public void setMarkedBy(int markedBy) {
        this.markedBy = markedBy;
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

    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId=" + attendanceId +
                ", studentId=" + studentId +
                ", attendanceDate=" + attendanceDate +
                ", subject='" + subject + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}