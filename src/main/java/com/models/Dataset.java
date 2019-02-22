package com.models;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Admin on 03.02.2019.
 */
public class Dataset {
    private int id;
    private String name;
    private String description;
    private String columns;
    private boolean active;
    private String visibleActive;
    private Specialist specialist;

    public Dataset() {
    }
    public Dataset(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Dataset(int id, String name, String description, String columns) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.columns = columns;
    }

    public Dataset(String name, String description, String columns) {
        this.name = name;
        this.description = description;
        this.columns = columns;
    }

    public Dataset(int id, String name, String description, boolean active, Specialist specialist) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.specialist = specialist;
    }

    public Dataset(int id, String name, String description, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
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

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getVisibleActive() {
        return visibleActive;
    }

    public void setVisibleActive(Boolean active) {
        if(active) {
            this.visibleActive = "Enabled";
        } else {
            this.visibleActive = "Disabled";
        }
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

    public Dataset fromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        return new Gson().fromJson(json, Dataset.class);
    }
}
