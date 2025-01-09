package edu.uoc.eduocation.controller;

import edu.uoc.eduocation.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EdUOCationController {

    private final List<School> schools;
    private final List<Teacher> teachers;
    private final List<Course> courses;
    private final List<Student> students;
    private final List<Enrollment> enrollments;

    private static final Logger logger = Logger.getLogger(EdUOCationController.class.getName());

    public EdUOCationController(String schoolsFile, String teachersFile, String coursesFile, String studentsFile, String enrollmentsFile) {
        schools = new ArrayList<>();
        teachers = new ArrayList<>();
        courses = new ArrayList<>();
        students = new ArrayList<>();
        enrollments = new ArrayList<>();

        loadSchools(schoolsFile);
        loadTeachers(teachersFile);
        loadCourses(coursesFile);
        loadStudents(studentsFile);
        loadEnrollments(enrollmentsFile);
    }

    private void loadSchools(String filename) {
        InputStream inputStream = getClass().getResourceAsStream("/data/" + filename);
        if (inputStream == null) {
            logger.log(Level.SEVERE, "File not found: " + filename);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                School school = new School(parts[0].trim());

                for (int i = 1; i < parts.length; i++) {
                    String[] locationData = parts[i].split(",");
                    Location location = new Location(locationData[0].trim(), locationData[1].trim(), locationData[2].trim(), locationData[3].trim());
                    school.addLocation(location);
                }
                schools.add(school);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading schools from file: " + filename, e);
        }
    }

    private void loadTeachers(String filename) {
        InputStream inputStream = getClass().getResourceAsStream("/data/" + filename);
        if (inputStream == null) {
            logger.log(Level.SEVERE, "File not found: " + filename);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                Teacher teacher = new Teacher(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        LocalDate.parse(parts[3].trim()),
                        parts[4].trim()
                );
                teachers.add(teacher);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading teachers from file: " + filename, e);
        }
    }

    private void loadCourses(String filename) {
        InputStream inputStream = getClass().getResourceAsStream("/data/" + filename);
        if (inputStream == null) {
            logger.log(Level.SEVERE, "File not found: " + filename);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                String type = parts[0].trim();
                String name = parts[1].trim();
                String code = parts[2].trim();
                int credits = Integer.parseInt(parts[3].trim());
                int hours = Integer.parseInt(parts[4].trim());
                String teacherNif = parts[5].trim();

                Course course;
                switch (type) {
                    case "CourseWithExam" -> {
                        String[] additionalInfo = parts[6].split(",");
                        LocalDateTime examDate = LocalDateTime.parse(additionalInfo[0].trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        String location = additionalInfo[1].trim();
                        course = new CourseWithExam(type, name, code, credits, hours, teacherNif, examDate, location);
                    }
                    case "CourseWithPracticeGroup" -> {
                        String[] additionalInfo = parts[6].split(",");
                        String practiceType = additionalInfo[0].trim();
                        int groupSize = Integer.parseInt(additionalInfo[1].trim());
                        course = new CourseWithPracticeGroup(type, name, code, credits, hours, teacherNif, practiceType, groupSize);
                    }
                    case "CourseWithPracticeIndividual" -> {
                        String practiceType = parts[6].trim();
                        course = new CourseWithPracticeIndividual(type, name, code, credits, hours, teacherNif, practiceType);
                    }
                    default -> course = new CourseWithoutExam(type, name, code, credits, hours, teacherNif);
                }

                courses.add(course);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading courses from file: " + filename, e);
        }
    }

    private void loadStudents(String filename) {
        InputStream inputStream = getClass().getResourceAsStream("/data/" + filename);
        if (inputStream == null) {
            logger.log(Level.SEVERE, "File not found: " + filename);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                String schoolName = parts[0].trim();
                String groupName = parts[1].trim();
                String tutorNif = parts[2].trim();
                String[] studentData = parts[3].split(",");

                School school = findSchool(schoolName);
                Teacher tutor = findTeacher(tutorNif);
                Group group = new Group(groupName, tutor);

                for (String studentInfo : studentData) {
                    String[] studentParts = studentInfo.split(":");
                    Student student = new Student(
                            studentParts[0].trim(),
                            studentParts[1].trim(),
                            studentParts[2].trim(),
                            LocalDate.parse(studentParts[3].trim())
                    );
                    group.addStudent(student);
                    students.add(student);
                }
                school.addGroup(group);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading students from file: " + filename, e);
        }
    }

    private void loadEnrollments(String filename) {
        InputStream inputStream = getClass().getResourceAsStream("/data/" + filename);
        if (inputStream == null) {
            logger.log(Level.SEVERE, "File not found: " + filename);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length < 4) {
                    logger.log(Level.WARNING, "Invalid enrollment data: " + line);
                    continue;
                }

                String studentNif = parts[0].trim();
                String courseCode = parts[1].trim();
                String semester = parts[2].trim();
                String enrollmentType = parts[3].trim();
                String additionalInfo = parts.length > 4 ? parts[4].trim() : "";

                Student student = findStudent(studentNif);
                Course course = findCourse(courseCode);

                Enrollment enrollment = new Enrollment(student, course, semester, enrollmentType);

                if (enrollmentType.equals("MULTIPLE") && !additionalInfo.isEmpty()) {
                    String[] groupMembers = additionalInfo.split(",");
                    for (String groupMember : groupMembers) {
                        enrollment.addGroupMember(groupMember.trim());
                    }
                }

                enrollments.add(enrollment);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading enrollments from file: " + filename, e);
        }
    }

    private School findSchool(String name) {
        return schools.stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
    }

    private Teacher findTeacher(String nif) {
        return teachers.stream().filter(t -> t.getNif().equals(nif)).findFirst().orElse(null);
    }

    private Student findStudent(String nif) {
        return students.stream().filter(s -> s.getNif().equals(nif)).findFirst().orElse(null);
    }

    private Course findCourse(String code) {
        return courses.stream().filter(c -> c.getCode().equals(code)).findFirst().orElse(null);
    }

    public List<String> getSchools() {
        List<String> schoolNames = new ArrayList<>();
        for (School school : schools) {
            schoolNames.add(school.getName());
        }
        return schoolNames;
    }

    public List<String> getTeachers() {
        List<String> teacherNames = new ArrayList<>();
        for (Teacher teacher : teachers) {
            teacherNames.add(teacher.getName() + " " + teacher.getSurname());
        }
        return teacherNames;
    }

    public List<String> getCourses() {
        return courses.stream()
                .map(course -> course.getName() + " (" + course.getCode() + ")")
                .collect(Collectors.toList());
    }

    public List<String> getGroups(String schoolName) {
        School school = findSchool(schoolName);
        List<String> groupNames = new ArrayList<>();
        if (school != null) {
            for (Group group : school.getGroups()) {
                groupNames.add(group.getName());
            }
        }
        return groupNames;
    }

    public List<String> getStudents(String schoolName, String groupName) {
        School school = findSchool(schoolName);
        List<String> studentNames = new ArrayList<>();
        if (school != null) {
            for (Group group : school.getGroups()) {
                if (group.getName().equals(groupName)) {
                    for (Student student : group.getStudents()) {
                        studentNames.add(student.getName() + " " + student.getSurname());
                    }
                }
            }
        }
        return studentNames;
    }

    public List<String> getEnrollments(String studentNif) {
        List<String> enrollmentDescriptions = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent().getNif().equals(studentNif)) {
                enrollmentDescriptions.add(enrollment.getCourse().getName() + ", Semester: " + enrollment.getSemester());
            }
        }
        return enrollmentDescriptions;
    }

    public boolean updateEnrollmentMark(String courseName, String semester, String studentNif, double mark) {
        if (mark < 0 || mark > 10) {
            return false;
        }
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getCourse().getName().equals(courseName)
                    && enrollment.getSemester().equals(semester)
                    && enrollment.getStudent().getNif().equals(studentNif)) {
                enrollment.updateMark(mark);
                return true;
            }
        }
        return false;
    }

    public void addSchool(String name, String locationData) {
        School school = new School(name);
        String[] locationParts = locationData.split(",");
        Location location = new Location(locationParts[0].trim(), locationParts[1].trim(), locationParts[2].trim(), locationParts[3].trim());
        school.addLocation(location);
        schools.add(school);
    }

    public void addTeacher(String nif, String name, String surname, LocalDate birthdate, String department) {
        Teacher teacher = new Teacher(nif, name, surname, birthdate, department);
        teachers.add(teacher);
    }

    public void addCourse(String type, String name, String code, int credits, int hours, String teacherNif, String additionalInfo) {
        Course course;
        switch (type) {
            case "CourseWithExam" -> {
                String[] infoParts = additionalInfo.split(",");
                LocalDateTime examDate = LocalDateTime.parse(infoParts[0].trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                String location = infoParts[1].trim();
                course = new CourseWithExam(type, name, code, credits, hours, teacherNif, examDate, location);
            }
            case "CourseWithPracticeGroup" -> {
                String[] infoParts = additionalInfo.split(",");
                String practiceType = infoParts[0].trim();
                int groupSize = Integer.parseInt(infoParts[1].trim());
                course = new CourseWithPracticeGroup(type, name, code, credits, hours, teacherNif, practiceType, groupSize);
            }
            case "CourseWithPracticeIndividual" -> {
                String practiceType = additionalInfo.trim();
                course = new CourseWithPracticeIndividual(type, name, code, credits, hours, teacherNif, practiceType);
            }
            default -> course = new CourseWithoutExam(type, name, code, credits, hours, teacherNif);
        }
        courses.add(course);
    }

    public void addStudentGroup(String schoolName, String groupName, String tutorNif, String[] studentData) {
        School school = findSchool(schoolName);
        Teacher tutor = findTeacher(tutorNif);
        Group group = new Group(groupName, tutor);

        for (String studentInfo : studentData) {
            String[] studentParts = studentInfo.split(":");
            Student student = new Student(
                    studentParts[0].trim(),
                    studentParts[1].trim(),
                    studentParts[2].trim(),
                    LocalDate.parse(studentParts[3].trim())
            );
            group.addStudent(student);
            students.add(student);
        }
        school.addGroup(group);
    }

    public void addEnrollment(String studentNif, String courseCode, String semester, String enrollmentType, String additionalInfo) {
        Student student = findStudent(studentNif);
        Course course = findCourse(courseCode);

        Enrollment enrollment = new Enrollment(student, course, semester, enrollmentType);

        if (enrollmentType.equals("MULTIPLE")) {
            String[] groupMembers = additionalInfo.split(",");
            for (String groupMember : groupMembers) {
                enrollment.addGroupMember(groupMember.trim());
            }
        }

        enrollments.add(enrollment);
    }
}