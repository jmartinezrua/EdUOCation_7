package edu.uoc.eduocation.model;

import com.google.gson.Gson;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public String toString() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("nif", nif);
        jsonMap.put("name", name);
        jsonMap.put("surname", surname);
        jsonMap.put("birthdate", birthdate.toString());

        Gson gson = new Gson();
        return gson.toJson(jsonMap);
    }
}