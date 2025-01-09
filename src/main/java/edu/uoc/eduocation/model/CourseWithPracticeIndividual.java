package edu.uoc.eduocation.model;

public class CourseWithPracticeIndividual extends Course {
    private String practiceType;

    public CourseWithPracticeIndividual(String type, String name, String code, int credits, int hours, String teacherNif, String practiceType) {
        super(type, name, code, credits, hours, teacherNif);
        this.practiceType = practiceType;
    }

    @Override
    public String getAdditionalInfo() {
        return "Practice type: " + practiceType;
    }
}