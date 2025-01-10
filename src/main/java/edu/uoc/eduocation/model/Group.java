package edu.uoc.eduocation.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final String name;
    private final Teacher tutor;
    private final List<Student> students;

    public Group(String name, Teacher tutor) {
        this.name = name;
        this.tutor = tutor;
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public String getName() {
        return name;
    }

    public Teacher getTutor() {
        return tutor;
    }

    public List<Student> getStudents() {
        return students;
    }
}
