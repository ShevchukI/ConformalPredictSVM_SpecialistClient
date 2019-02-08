package com.controllers.requests;

import com.google.gson.Gson;
import com.models.Specialist;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.util.Base64;

/**
 * Created by Admin on 03.02.2019.
 */
public class SpecialistController {
    private final static String URL = "http://localhost:8888";

    public HttpResponse specialistAuthorization(String name, String password) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL + "/doctor-system/specialist/authorization");
        request.addHeader("Authorization", basicAuthPayload);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        }catch (HttpHostConnectException e){
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }
        return response;
    }


    public int specialistRegistration(Specialist specialist) throws IOException {
        String json = new Gson().toJson(specialist);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(URL + "/doctor-system/specialist/registration");
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(json));
        HttpResponse response = null;
        try {
            response = client.execute(request);
        }catch (HttpHostConnectException e){
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }
        return response.getStatusLine().getStatusCode();
    }

    public HttpResponse changePassword(String name, String password, String newPassword) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(URL + "/doctor-system/specialist/psw");
        request.setHeader("Authorization", basicAuthPayload);
        request.setEntity(new StringEntity(newPassword));
        HttpResponse response = null;
        try {
            response = client.execute(request);
        }catch (HttpHostConnectException e){
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }
        return response;
    }

    public HttpResponse changeName(String name, String password, Specialist specialist) throws IOException {
        String json = new Gson().toJson(specialist);

        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(URL + "/doctor-system/specialist");
        request.setHeader("Authorization", basicAuthPayload);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(json));
        HttpResponse response = null;
        try {
            response = client.execute(request);
        }catch (HttpHostConnectException e){
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }
        return response;
    }
}
