package edu.uoc.eduocation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

public class Group {
    private final String name;
    private final Teacher tutor;
    private final List<Student> students;
    private int studentsCount;

    public Group(String name, Teacher tutor) {
        this.name = name;
        this.tutor = tutor;
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        this.students.add(student);
        this.studentsCount = this.students.size();
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

    @Override
    public String toString() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", name);
        jsonMap.put("tutor", tutor.getName()); // Assuming the Teacher class has a getName() method
        jsonMap.put("studentsCount", students.size());

        Gson gson = new Gson();
        return gson.toJson(jsonMap);
    }
}