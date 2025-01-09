package edu.uoc.eduocation.model;

public class CourseWithoutExam extends Course {
    public CourseWithoutExam(String type, String name, String code, int credits, int hours, String teacherNif) {
        super(type, name, code, credits, hours, teacherNif);
    }

    @Override
    public String getAdditionalInfo() {
        return "No additional info.";
    }
}
