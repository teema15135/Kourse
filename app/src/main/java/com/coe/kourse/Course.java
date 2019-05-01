package com.coe.kourse;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Course {

    public String id;
    public String name;
    public String color;

    public Course() {
        super();
    }

    public Course(String courseName, String courseColor) {
        this.id = Long.toString(System.currentTimeMillis());
        this.name = courseName;
        this.color = courseColor;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("id", id);
        result.put("color", color);
        return result;
    }

}
