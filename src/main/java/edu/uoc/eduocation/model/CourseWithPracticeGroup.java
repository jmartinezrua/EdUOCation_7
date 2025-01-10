package edu.uoc.eduocation.model;

public class CourseWithPracticeGroup extends Course {
    private final String practiceType;
    private final int groupSize;

    public CourseWithPracticeGroup(String type, String name, String code, int credits, int hours, String teacherNif, String practiceType, int groupSize) {
        super(type, name, code, credits, hours, teacherNif);
        this.practiceType = practiceType;
        this.groupSize = groupSize;
    }

    @Override
    public String getAdditionalInfo() {
        return "Practice type: " + practiceType + ", Group size: " + groupSize;
    }
}