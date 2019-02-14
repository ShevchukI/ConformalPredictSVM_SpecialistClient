package com.models;

/**
 * Created by Admin on 08.02.2019.
 */
public class DatasetObject {
    private int id;
    private String content;

    public DatasetObject() {
    }

    public DatasetObject(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
