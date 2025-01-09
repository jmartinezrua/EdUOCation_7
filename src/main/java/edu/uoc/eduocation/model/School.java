package edu.uoc.eduocation.model;

import java.util.ArrayList;
import java.util.List;

public class School {
    private String name;
    private List<Location> locations;
    private List<Group> groups;

    public School(String name) {
        this.name = name;
        this.locations = new ArrayList<>();
        this.groups = new ArrayList<>();
    }

    public void addLocation(Location location) {
        this.locations.add(location);
    }

    public void addGroup(Group group) {
        this.groups.add(group);
    }

    public String getName() {
        return name;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<Group> getGroups() {
        return groups;
    }
}