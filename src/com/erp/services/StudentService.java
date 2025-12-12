package com.erp.services;

import com.erp.db.StudentDAO;
import com.erp.models.Student;
import com.erp.utils.LoggerUtil;
import java.util.List;

public class StudentService {
    
    private StudentDAO studentDAO;
    
    public StudentService() {
        this.studentDAO = new StudentDAO();
    }
    
    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        try {
            LoggerUtil.info("StudentService", "Fetching all students");
            return studentDAO.getAllStudents();
        } catch (Exception e) {
            LoggerUtil.error("StudentService", "Error fetching all students", e);
            return List.of();
        }
    }
    
    /**
     * Get student by ID
     */
    public Student getStudentById(int studentId) {
        try {
            LoggerUtil.info("StudentService", "Fetching student by ID: " + studentId);
            return studentDAO.getStudentById(studentId);
        } catch (Exception e) {
            LoggerUtil.error("StudentService", "Error fetching student by ID", e);
            return null;
        }
    }
    
    /**
     * Add new student
     */
    public boolean addStudent(Student student) {
        try {
            LoggerUtil.info("StudentService", "Adding new student: " + student.getRollNumber());
            return studentDAO.addStudent(student);
        } catch (Exception e) {
            LoggerUtil.error("StudentService", "Error adding student", e);
            return false;
        }
    }
    
    /**
     * Update student
     */
    public boolean updateStudent(Student student) {
        try {
            LoggerUtil.info("StudentService", "Updating student: " + student.getStudentId());
            return studentDAO.updateStudent(student);
        } catch (Exception e) {
            LoggerUtil.error("StudentService", "Error updating student", e);
            return false;
        }
    }
    
    /**
     * Delete student
     */
    public boolean deleteStudent(int studentId) {
        try {
            LoggerUtil.info("StudentService", "Deleting student: " + studentId);
            return studentDAO.deleteStudent(studentId);
        } catch (Exception e) {
            LoggerUtil.error("StudentService", "Error deleting student", e);
            return false;
        }
    }
    
    /**
     * Search students
     */
    public List<Student> searchStudents(String keyword) {
        try {
            LoggerUtil.info("StudentService", "Searching students with keyword: " + keyword);
            return studentDAO.searchStudents(keyword);
        } catch (Exception e) {
            LoggerUtil.error("StudentService", "Error searching students", e);
            return List.of();
        }
    }
    
    /**
     * Validate student data
     */
    public String validateStudent(Student student) {
        if (student.getRollNumber() == null || student.getRollNumber().trim().isEmpty()) {
            return "Roll number is required";
        }
        if (student.getFullName() == null || student.getFullName().trim().isEmpty()) {
            return "Full name is required";
        }
        if (student.getPassword() == null || student.getPassword().trim().isEmpty()) {
            return "Password is required";
        }
        if (student.getCourse() == null || student.getCourse().trim().isEmpty()) {
            return "Course is required";
        }
        if (student.getSemester() < 1 || student.getSemester() > 8) {
            return "Semester must be between 1 and 8";
        }
        if (student.getSection() == null || student.getSection().trim().isEmpty()) {
            return "Section is required";
        }
        return null; // No validation errors
    }
}