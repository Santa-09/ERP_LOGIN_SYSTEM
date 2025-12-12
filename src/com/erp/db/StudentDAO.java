package com.erp.db;

import com.erp.models.Student;
import com.erp.utils.LoggerUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    /**
     * Authenticate student user
     */
    public Student authenticate(String rollNumber, String password) {
        String query = "SELECT * FROM student WHERE roll_number = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, rollNumber);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Student student = extractStudentFromResultSet(rs);
                LoggerUtil.info("StudentDAO", "Student authenticated successfully: " + rollNumber);
                return student;
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("StudentDAO", "Error authenticating student", e);
        }
        
        return null;
    }

    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM student ORDER BY roll_number";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
            
            LoggerUtil.info("StudentDAO", "Retrieved " + students.size() + " students");
            
        } catch (SQLException e) {
            LoggerUtil.error("StudentDAO", "Error getting all students", e);
        }
        
        return students;
    }

    /**
     * Get student by ID
     */
    public Student getStudentById(int studentId) {
        String query = "SELECT * FROM student WHERE student_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("StudentDAO", "Error getting student by ID", e);
        }
        
        return null;
    }

    /**
     * Add new student
     */
    public boolean addStudent(Student student) {
        String query = "INSERT INTO student (roll_number, password, full_name, email, phone, " +
                      "course, semester, section, admission_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, student.getRollNumber());
            pstmt.setString(2, student.getPassword());
            pstmt.setString(3, student.getFullName());
            pstmt.setString(4, student.getEmail());
            pstmt.setString(5, student.getPhone());
            pstmt.setString(6, student.getCourse());
            pstmt.setInt(7, student.getSemester());
            pstmt.setString(8, student.getSection());
            pstmt.setDate(9, Date.valueOf(student.getAdmissionDate()));
            
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("StudentDAO", "Student added: " + student.getRollNumber());
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("StudentDAO", "Error adding student", e);
        }
        
        return false;
    }

    /**
     * Update student
     */
    public boolean updateStudent(Student student) {
        String query = "UPDATE student SET full_name = ?, email = ?, phone = ?, " +
                      "course = ?, semester = ?, section = ? WHERE student_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getPhone());
            pstmt.setString(4, student.getCourse());
            pstmt.setInt(5, student.getSemester());
            pstmt.setString(6, student.getSection());
            pstmt.setInt(7, student.getStudentId());
            
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("StudentDAO", "Student updated: " + student.getStudentId());
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("StudentDAO", "Error updating student", e);
        }
        
        return false;
    }

    /**
     * Delete student
     */
    public boolean deleteStudent(int studentId) {
        String query = "DELETE FROM student WHERE student_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, studentId);
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("StudentDAO", "Student deleted: " + studentId);
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("StudentDAO", "Error deleting student", e);
        }
        
        return false;
    }

    /**
     * Search students by name or roll number
     */
    public List<Student> searchStudents(String keyword) {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM student WHERE full_name LIKE ? OR roll_number LIKE ? " +
                      "ORDER BY roll_number";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("StudentDAO", "Error searching students", e);
        }
        
        return students;
    }

    /**
     * Extract Student object from ResultSet
     */
    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setRollNumber(rs.getString("roll_number"));
        student.setFullName(rs.getString("full_name"));
        student.setEmail(rs.getString("email"));
        student.setPhone(rs.getString("phone"));
        student.setCourse(rs.getString("course"));
        student.setSemester(rs.getInt("semester"));
        student.setSection(rs.getString("section"));
        
        Date admissionDate = rs.getDate("admission_date");
        if (admissionDate != null) {
            student.setAdmissionDate(admissionDate.toLocalDate());
        }
        
        return student;
    }
}