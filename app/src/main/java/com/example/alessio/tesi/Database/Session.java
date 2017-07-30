package com.example.alessio.tesi.Database;

public class Session {
    private int id;
    private int year, month, day, duration;
    private int location_name, course_id;

    public Session(int id, int year, int month, int day, int duration, int location_name, int course_id) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.duration = duration;
        this.location_name = location_name;
        this.course_id = course_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getLocation_name() {
        return location_name;
    }

    public void setLocation_name(int location_name) {
        this.location_name = location_name;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }
}
