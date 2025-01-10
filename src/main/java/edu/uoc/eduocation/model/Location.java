package edu.uoc.eduocation.model;

public class Location {
    private final String address;
    private final String city;
    private final String country;
    private final String phone;

    public Location(String address, String city, String country, String phone) {
        this.address = address;
        this.city = city;
        this.country = country;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }
}