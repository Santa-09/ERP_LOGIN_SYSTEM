package com.erp.services;

import com.erp.db.TimeTableDAO;
import com.erp.models.TimeTable;
import com.erp.utils.LoggerUtil;
import java.util.List;

public class TimeTableService {
    
    private TimeTableDAO timeTableDAO;
    
    public TimeTableService() {
        this.timeTableDAO = new TimeTableDAO();
    }
    
    /**
     * Add timetable entry
     */
    public boolean addTimeTable(TimeTable timeTable) {
        try {
            LoggerUtil.info("TimeTableService", "Adding timetable entry");
            return timeTableDAO.addTimeTable(timeTable);
        } catch (Exception e) {
            LoggerUtil.error("TimeTableService", "Error adding timetable", e);
            return false;
        }
    }
    
    /**
     * Update timetable entry
     */
    public boolean updateTimeTable(TimeTable timeTable) {
        try {
            LoggerUtil.info("TimeTableService", "Updating timetable: " + timeTable.getTimetableId());
            return timeTableDAO.updateTimeTable(timeTable);
        } catch (Exception e) {
            LoggerUtil.error("TimeTableService", "Error updating timetable", e);
            return false;
        }
    }
    
    /**
     * Get timetable by course, semester and section
     */
    public List<TimeTable> getTimeTableByClass(String course, int semester, String section) {
        try {
            LoggerUtil.info("TimeTableService", 
                "Fetching timetable for " + course + " Sem " + semester + " Sec " + section);
            return timeTableDAO.getTimeTableByClass(course, semester, section);
        } catch (Exception e) {
            LoggerUtil.error("TimeTableService", "Error fetching timetable by class", e);
            return List.of();
        }
    }
    
    /**
     * Get all timetable entries
     */
    public List<TimeTable> getAllTimeTables() {
        try {
            LoggerUtil.info("TimeTableService", "Fetching all timetable entries");
            return timeTableDAO.getAllTimeTables();
        } catch (Exception e) {
            LoggerUtil.error("TimeTableService", "Error fetching all timetables", e);
            return List.of();
        }
    }
    
    /**
     * Delete timetable entry
     */
    public boolean deleteTimeTable(int timetableId) {
        try {
            LoggerUtil.info("TimeTableService", "Deleting timetable: " + timetableId);
            return timeTableDAO.deleteTimeTable(timetableId);
        } catch (Exception e) {
            LoggerUtil.error("TimeTableService", "Error deleting timetable", e);
            return false;
        }
    }
    
    /**
     * Get distinct courses
     */
    public List<String> getDistinctCourses() {
        try {
            LoggerUtil.info("TimeTableService", "Fetching distinct courses");
            return timeTableDAO.getDistinctCourses();
        } catch (Exception e) {
            LoggerUtil.error("TimeTableService", "Error fetching distinct courses", e);
            return List.of();
        }
    }
    
    /**
     * Validate timetable data
     */
    public String validateTimeTable(TimeTable timeTable) {
        if (timeTable.getCourse() == null || timeTable.getCourse().trim().isEmpty()) {
            return "Course is required";
        }
        if (timeTable.getSemester() < 1 || timeTable.getSemester() > 8) {
            return "Semester must be between 1 and 8";
        }
        if (timeTable.getSection() == null || timeTable.getSection().trim().isEmpty()) {
            return "Section is required";
        }
        if (timeTable.getDayOfWeek() == null || timeTable.getDayOfWeek().trim().isEmpty()) {
            return "Day of week is required";
        }
        if (timeTable.getTimeSlot() == null || timeTable.getTimeSlot().trim().isEmpty()) {
            return "Time slot is required";
        }
        if (timeTable.getSubject() == null || timeTable.getSubject().trim().isEmpty()) {
            return "Subject is required";
        }
        return null; // No validation errors
    }
}