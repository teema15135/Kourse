package com.coe.kourse;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Course {

    public String id;
    public String name;
    public String color;

    public int total;
    public int attend;

    public int type; // 0 for Non-fix time type, 1 for Fix time type

    public Course() {
        super();
    }

    public Course(String courseName, String courseColor, int total, int type) {
        this.id = Long.toString(System.currentTimeMillis());
        this.name = courseName;
        this.color = courseColor;
        this.total = total;
        this.attend = 0;
        this.type = type;
    }

    public void attend() {
        if (total > attend) attend++;
    }

    public int getAttend() {
        return this.attend;
    }

    public int getTotal() {
        return this.total;
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
        result.put("total", total);
        result.put("attend", attend);
        return result;
    }

}
