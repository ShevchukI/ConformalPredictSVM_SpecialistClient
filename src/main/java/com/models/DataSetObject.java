package com.models;

/**
 * Created by Admin on 08.02.2019.
 */
public class DataSetObject {
    private int id;
    private int userObjectId;
    private String params;
    private int objectClass;

    public DataSetObject() {
    }

    public DataSetObject(int id, String content) {
        this.id = id;
        this.params = content;
    }

    public int getUserObjectId() {
        return userObjectId;
    }

    public void setUserObjectId(int userObjectId) {
        this.userObjectId = userObjectId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String content) {
        this.params = content;
    }

    public int getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(int objectClass) {
        this.objectClass = objectClass;
    }
}
