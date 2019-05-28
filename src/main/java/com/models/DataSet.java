package com.models;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

/**
 * Created by Admin on 03.02.2019.
 */
public class DataSet implements Serializable{
    private int id;
    private String name;
    private String description;
    private String columns;
    private boolean active;
    private SpecialistEntity specialistEntity;

    public DataSet() {
    }

    public DataSet(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public DataSet(int id, String name, String description, String columns) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.columns = columns;
    }

    public DataSet(String name, String description, String columns) {
        this.name = name;
        this.description = description;
        this.columns = columns;
    }

    public DataSet(int id, String name, String description, boolean active, SpecialistEntity specialistEntity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.specialistEntity = specialistEntity;
    }

    public DataSet(int id, String name, String description, boolean active) {
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
        if (this.isActive()) {
            return "Enabled";
        } else {
            return "Disabled";
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public SpecialistEntity getSpecialistEntity() {
        return specialistEntity;
    }

    public void setSpecialistEntity(SpecialistEntity specialistEntity) {
        this.specialistEntity = specialistEntity;
    }

    public String getOwner() {
        return specialistEntity.getName() + " " + specialistEntity.getSurname();
    }

    public DataSet fromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        return new Gson().fromJson(json, DataSet.class);
    }
}
