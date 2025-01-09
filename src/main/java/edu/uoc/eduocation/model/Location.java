package edu.uoc.eduocation.model;

public class Location {
    private String address;
    private String city;
    private String country;
    private String phone;

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