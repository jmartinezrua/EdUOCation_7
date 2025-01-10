package edu.uoc.eduocation.model;

import java.time.LocalDate;

public class Teacher {
    private final String nif;
    private final String name;
    private final String surname;
    private final LocalDate birthdate;
    private final String department;

    public Teacher(String nif, String name, String surname, LocalDate birthdate, String department) {
        this.nif = nif;
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.department = department;
    }

    public String getNif() {
        return nif;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getDepartment() {
        return department;
    }
}