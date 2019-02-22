package com.controllers.requests;


import com.google.gson.Gson;
import com.models.ParameterSingleObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.util.Base64;

/**
 * Created by Admin on 22.02.2019.
 */
public class IllnessController extends MainController {

    public HttpResponse startSingleTest(String[] authorization, int configurationId, ParameterSingleObject parameterSingleObject) throws IOException {
        String json = new Gson().toJson(parameterSingleObject);
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(getLocalhostUrl() + "/doctor-system/doctor/illness/result/" + configurationId + "/start");
        request.addHeader("Authorization", basicAuthPayload);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(json));
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (HttpHostConnectException e) {
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        }
        return response;
    }

    public HttpResponse resultSingleTest(String[] authorization, int processId) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getLocalhostUrl() + "/doctor-system/doctor/illness/result/" + processId + "/start");
        request.addHeader("Authorization", basicAuthPayload);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (HttpHostConnectException e) {
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }
        return response;
    }
}
