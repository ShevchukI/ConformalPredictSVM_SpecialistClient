package com.controllers.requests;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

/**
 * Created by Admin on 15.02.2019.
 */
public class SVMParameterController extends MainController{

    public static HttpResponse getAllKernel() throws IOException {
        String url = getUrl()+"/configuration/kernel";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

}
