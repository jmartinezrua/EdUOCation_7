package edu.uoc.eduocation.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Base Class for Courses
public abstract class Course {
    private String type;
    private String name;
    private String code;
    private int credits;
    private int hours;
    private String teacherNif;

    public Course(String type, String name, String code, int credits, int hours, String teacherNif) {
        this.type = type;
        this.name = name;
        this.code = code;
        this.credits = credits;
        this.hours = hours;
        this.teacherNif = teacherNif;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getCredits() {
        return credits;
    }

    public int getHours() {
        return hours;
    }

    public String getTeacherNif() {
        return teacherNif;
    }

    public abstract String getAdditionalInfo();
}
