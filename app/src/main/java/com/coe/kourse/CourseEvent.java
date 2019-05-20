package com.coe.kourse;

import java.util.Date;

public class CourseEvent {

    Date eventDate;
    String courseName;
    String eventName;
    String user;

    public CourseEvent() {
        super();
    }

    /*
    For create event with no course involve
     */
    public CourseEvent(String eventName, String user, Date eventDate) {
        super();
        this.eventName = eventName;
        this.user = user;
        this.eventDate = eventDate;
    }

    /*
    For create event when add course with course payment date
     */
    public CourseEvent(String eventName, String courseName, String user, Date eventDate) {
        super();
        this.eventName = eventName;
        this.user = user;
        this.eventDate = eventDate;
        this.courseName = courseName;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public String getUser() {
        return this.user;
    }

    public Date getEventDate() {
        return this.eventDate;
    }

}
