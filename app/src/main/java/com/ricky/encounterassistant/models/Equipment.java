package com.ricky.encounterassistant.models;

/**
 * Created by Ricky on 12/24/2014.
 */
public class Equipment {
    private String name;
    private String description;

    public Equipment(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
