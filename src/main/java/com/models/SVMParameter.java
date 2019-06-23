package com.models;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

import static com.tools.Constant.crudEntity;
import static com.tools.Constant.getUrl;

/**
 * Created by Admin on 15.02.2019.
 */
public class SVMParameter {
    private int id;
    private String name;
    public static HttpResponse getAllKernel() throws IOException {
        String url = getUrl()+"/configuration/kernel";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
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
