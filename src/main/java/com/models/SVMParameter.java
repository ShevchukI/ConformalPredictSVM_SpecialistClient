package com.models;

/**
 * Created by Admin on 15.02.2019.
 */
public class SVMParameter {
    private int id;
    private String name;

    public SVMParameter() {
    }

    public SVMParameter(int id, String name) {
        this.id = id;
        this.name = name;
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


}
