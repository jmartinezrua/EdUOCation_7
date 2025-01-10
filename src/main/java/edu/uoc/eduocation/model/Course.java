package edu.uoc.eduocation.model;

// Base Class for Courses
public abstract class Course {
    private final String type;
    private final String name;
    private final String code;
    private final int credits;
    private final int hours;
    private final String teacherNif;

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
