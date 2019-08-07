package com.dcd.recipes.stemapp;

public class Recipe {
    private String name;
    private int difficulty;
    private String desc;
    public Recipe (String name, int difficulty, String desc) {
        this.name = name;
        this.difficulty = difficulty;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getDesc() {
        return desc;
    }
}
