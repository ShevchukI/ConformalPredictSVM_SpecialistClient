package com.controllers.requests;


import com.google.gson.Gson;
import com.models.ParameterSingleObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 * Created by Admin on 22.02.2019.
 */
public class IllnessController extends MainController {

    public HttpResponse startSingleTest(int configurationId, ParameterSingleObject parameterSingleObject) throws IOException {
        String json = new Gson().toJson(parameterSingleObject);
        String url = getLocalhostUrl() + "/doctor-system/doctor/illness/result/" + configurationId + "/start";
        HttpPost request = new HttpPost(url);
        HttpResponse response = crudEntity(new StringEntity(json), request, null, null, null);
        return response;
    }

    public HttpResponse resultSingleTest(int processId) throws IOException {
        String url = getLocalhostUrl() + "/doctor-system/doctor/illness/result/" + processId + "/start";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }
}
