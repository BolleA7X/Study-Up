package com.example.alessio.tesi.Database;

public class Trophy {
    public static final String BRONZE = "1";
    public static final String SILVER = "2";
    public static final String GOLD = "3";
    public static final String PLATINUM = "4";

    private int id;
    private String name, color, description;
    int unlocked;

    public Trophy(int id, String name, String color, String description, int unlocked) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.unlocked = unlocked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(int unlocked) {
        this.unlocked = unlocked;
    }
}
