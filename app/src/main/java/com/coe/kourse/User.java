package com.coe.kourse;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String id;
    public String name;

    public User() {
        super();
    }

    public User(String name) {
        super();
        this.name = name;
        this.id = Long.toString(System.currentTimeMillis());
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("id", id);
        return result;
    }

    @Override
    public String toString() {
        return "[Name: " + name + ", UID: " + id + "], ";
    }
}
