package com.erp.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Fee {
    private int feeId;
    private int studentId;
    private int semester;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal dueAmount;
    private LocalDate dueDate;
    private String paymentStatus; // Paid, Pending, Partial, Overdue
    private LocalDate lastPaymentDate;
    private LocalDateTime createdAt;
    
    // Additional fields for display
    private String studentName;
    private String rollNumber;

    // Constructors
    public Fee() {}

    public Fee(int studentId, int semester, BigDecimal totalAmount, BigDecimal paidAmount, 
               BigDecimal dueAmount, LocalDate dueDate, String paymentStatus) {
        this.studentId = studentId;
        this.semester = semester;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.dueAmount = dueAmount;
        this.dueDate = dueDate;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public int getFeeId() {
        return feeId;
    }

    public void setFeeId(int feeId) {
        this.feeId = feeId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(LocalDate lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
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
        return "Fee{" +
                "feeId=" + feeId +
                ", studentId=" + studentId +
                ", semester=" + semester +
                ", totalAmount=" + totalAmount +
                ", paidAmount=" + paidAmount +
                ", dueAmount=" + dueAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}