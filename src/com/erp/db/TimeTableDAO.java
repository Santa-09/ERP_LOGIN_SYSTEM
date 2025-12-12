package com.erp.db;

import com.erp.models.TimeTable;
import com.erp.utils.LoggerUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimeTableDAO {

    /**
     * Add timetable entry
     */
    public boolean addTimeTable(TimeTable timeTable) {
        String query = "INSERT INTO timetable (course, semester, section, day_of_week, " +
                      "time_slot, subject, faculty_name, room_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, timeTable.getCourse());
            pstmt.setInt(2, timeTable.getSemester());
            pstmt.setString(3, timeTable.getSection());
            pstmt.setString(4, timeTable.getDayOfWeek());
            pstmt.setString(5, timeTable.getTimeSlot());
            pstmt.setString(6, timeTable.getSubject());
            pstmt.setString(7, timeTable.getFacultyName());
            pstmt.setString(8, timeTable.getRoomNumber());
            
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("TimeTableDAO", "TimeTable entry added");
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("TimeTableDAO", "Error adding timetable", e);
        }
        
        return false;
    }

    /**
     * Update timetable entry
     */
    public boolean updateTimeTable(TimeTable timeTable) {
        String query = "UPDATE timetable SET course = ?, semester = ?, section = ?, " +
                      "day_of_week = ?, time_slot = ?, subject = ?, faculty_name = ?, " +
                      "room_number = ? WHERE timetable_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, timeTable.getCourse());
            pstmt.setInt(2, timeTable.getSemester());
            pstmt.setString(3, timeTable.getSection());
            pstmt.setString(4, timeTable.getDayOfWeek());
            pstmt.setString(5, timeTable.getTimeSlot());
            pstmt.setString(6, timeTable.getSubject());
            pstmt.setString(7, timeTable.getFacultyName());
            pstmt.setString(8, timeTable.getRoomNumber());
            pstmt.setInt(9, timeTable.getTimetableId());
            
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("TimeTableDAO", "TimeTable updated: " + timeTable.getTimetableId());
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("TimeTableDAO", "Error updating timetable", e);
        }
        
        return false;
    }

    /**
     * Get timetable by course, semester and section
     */
    public List<TimeTable> getTimeTableByClass(String course, int semester, String section) {
        List<TimeTable> timeTableList = new ArrayList<>();
        String query = "SELECT * FROM timetable WHERE course = ? AND semester = ? AND section = ? " +
                      "ORDER BY FIELD(day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'), time_slot";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, course);
            pstmt.setInt(2, semester);
            pstmt.setString(3, section);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                timeTableList.add(extractTimeTableFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("TimeTableDAO", "Error getting timetable by class", e);
        }
        
        return timeTableList;
    }

    /**
     * Get all timetable entries
     */
    public List<TimeTable> getAllTimeTables() {
        List<TimeTable> timeTableList = new ArrayList<>();
        String query = "SELECT * FROM timetable ORDER BY course, semester, section, " +
                      "FIELD(day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'), time_slot";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                timeTableList.add(extractTimeTableFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("TimeTableDAO", "Error getting all timetables", e);
        }
        
        return timeTableList;
    }

    /**
     * Delete timetable entry
     */
    public boolean deleteTimeTable(int timetableId) {
        String query = "DELETE FROM timetable WHERE timetable_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, timetableId);
            int rows = pstmt.executeUpdate();
            LoggerUtil.info("TimeTableDAO", "TimeTable deleted: " + timetableId);
            return rows > 0;
            
        } catch (SQLException e) {
            LoggerUtil.error("TimeTableDAO", "Error deleting timetable", e);
        }
        
        return false;
    }

    /**
     * Get distinct courses
     */
    public List<String> getDistinctCourses() {
        List<String> courses = new ArrayList<>();
        String query = "SELECT DISTINCT course FROM timetable ORDER BY course";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                courses.add(rs.getString("course"));
            }
            
        } catch (SQLException e) {
            LoggerUtil.error("TimeTableDAO", "Error getting distinct courses", e);
        }
        
        return courses;
    }

    /**
     * Extract TimeTable object from ResultSet
     */
    private TimeTable extractTimeTableFromResultSet(ResultSet rs) throws SQLException {
        TimeTable timeTable = new TimeTable();
        timeTable.setTimetableId(rs.getInt("timetable_id"));
        timeTable.setCourse(rs.getString("course"));
        timeTable.setSemester(rs.getInt("semester"));
        timeTable.setSection(rs.getString("section"));
        timeTable.setDayOfWeek(rs.getString("day_of_week"));
        timeTable.setTimeSlot(rs.getString("time_slot"));
        timeTable.setSubject(rs.getString("subject"));
        timeTable.setFacultyName(rs.getString("faculty_name"));
        timeTable.setRoomNumber(rs.getString("room_number"));
        return timeTable;
    }
}