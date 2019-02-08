package com.models;

/**
 * Created by Admin on 03.02.2019.
 */
public class Dataset {
    private int id;
    private String name;
    private String description;
    private boolean active;
    private Specialist specialist;

    public Dataset() {
    }

    public Dataset(int id, String name, String description, boolean active, Specialist specialist) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.specialist = specialist;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }
}
