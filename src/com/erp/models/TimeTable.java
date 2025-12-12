package com.erp.models;

import java.time.LocalDateTime;

public class TimeTable {
    private int timetableId;
    private String course;
    private int semester;
    private String section;
    private String dayOfWeek;
    private String timeSlot;
    private String subject;
    private String facultyName;
    private String roomNumber;
    private LocalDateTime createdAt;

    // Constructors
    public TimeTable() {}

    public TimeTable(String course, int semester, String section, String dayOfWeek,
                     String timeSlot, String subject, String facultyName, String roomNumber) {
        this.course = course;
        this.semester = semester;
        this.section = section;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
        this.subject = subject;
        this.facultyName = facultyName;
        this.roomNumber = roomNumber;
    }

    // Getters and Setters
    public int getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(int timetableId) {
        this.timetableId = timetableId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TimeTable{" +
                "timetableId=" + timetableId +
                ", course='" + course + '\'' +
                ", semester=" + semester +
                ", section='" + section + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", timeSlot='" + timeSlot + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}