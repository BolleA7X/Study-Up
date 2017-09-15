package com.example.alessio.tesi.Database;

public class Session {
    public static final int TRUE = 1;
    public static final int FALSE = 0;

    private int id;
    private int year, month, day, duration;
    private int theory, exercise, project;
    private String location_name, course_name;

    public Session() {}

    public Session(int year, int month, int day, int duration, int theory, int exercise, int project,
                   String location_name, String course_name) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.duration = duration;
        this.theory = theory;
        this.exercise = exercise;
        this.project = project;
        this.location_name = location_name;
        this.course_name = course_name;
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

    public int getTheory() {
        return theory;
    }

    public void setTheory(int theory) {
        this.theory = theory;
    }

    public int getExercise() {
        return exercise;
    }

    public void setExercise(int exercise) {
        this.exercise = exercise;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public static int stringToInt(String value) {
        if(value.equals("true"))
            return TRUE;
        else if(value.equals("false"))
            return FALSE;
        else
            return -1;
    }
}
