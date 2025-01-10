package edu.uoc.eduocation.model;

import com.google.gson.Gson;

import java.util.List;
import java.util.ArrayList;

public class Enrollment {
    private final Student student;
    private final Course course;
    private final String semester;
    private final String enrollmentType;
    private double mark;
    private final List<String> groupMembers;
    private String status;

    public Enrollment(Student student, Course course, String semester, String enrollmentType) {
        this.student = student;
        this.course = course;
        this.semester = semester;
        this.enrollmentType = enrollmentType;
        this.mark = 0.0;
        this.groupMembers = new ArrayList<>();

    }

    public void addGroupMember(String nif) {
        this.groupMembers.add(nif);
    }

    public void updateMark(double mark) {
        this.mark = mark;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public String getSemester() {
        return semester;
    }

    public String getEnrollmentType() {
        return enrollmentType;
    }

    public double getMark() {
        return mark;
    }

    public List<String> getGroupMembers() {
        return groupMembers;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}