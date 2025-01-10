package edu.uoc.eduocation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class School {
    private final String name;
    private final List<Location> locations;
    private final List<Group> groups;
    private int locationsCount;
    private int groupsCount;

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

    @Override
    public String toString() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", name);
        jsonMap.put("locationsCount", locations.size());
        jsonMap.put("groupsCount", groups.size());

        Gson gson = new Gson();
        return gson.toJson(jsonMap);
    }
}

/*"{" +

        "\"name\": \"" + name + "\"," +

        "\"locationsCount\": " + locationCount + "," +

        "\"groupsCount\": " + groupCount +

        "}"

 */