package edu.uoc.eduocation.model;

import java.time.LocalDateTime;

public class CourseWithExam extends Course {
    private final LocalDateTime examDate;
    private final String location;

    public CourseWithExam(String type, String name, String code, int credits, int hours, String teacherNif, LocalDateTime examDate, String location) {
        super(type, name, code, credits, hours, teacherNif);
        this.examDate = examDate;
        this.location = location;
    }



    @Override
    public String getAdditionalInfo() {
        return "Exam on: " + examDate + " at " + location;
    }
}