package edu.uoc.eduocation.model;

import java.time.LocalDate;

public class Student {
    private final String nif;
    private final String name;
    private final String surname;
    private final LocalDate birthdate;

    public Student(String nif, String name, String surname, LocalDate birthdate) {
        this.nif = nif;
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
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
}