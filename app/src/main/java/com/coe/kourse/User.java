package com.coe.kourse;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    public String id;
    public String name;
    public ArrayList<Course> courses;

    public User() {
        super();
    }

    public User(String name) {
        super();
        this.name = name;
        this.id = Long.toString(System.currentTimeMillis());
        courses = new ArrayList<>();
        courses.add(new Course("N/A", "N/A"));
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return this.id;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public ArrayList getCourses() {
        return courses;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("id", id);
        result.put("courses", courses);
        return result;
    }

    @Override
    public String toString() {
        return "[Name: " + name + ", UID: " + id + "], ";
    }
}
