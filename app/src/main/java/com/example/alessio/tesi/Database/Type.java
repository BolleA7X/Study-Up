package com.example.alessio.tesi.Database;

public class Type {
    private int id;
    private int theory, exercise, project;

    public Type(int id, int theory, int exercise, int project) {
        this.id = id;
        this.theory = theory;
        this.exercise = exercise;
        this.project = project;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
