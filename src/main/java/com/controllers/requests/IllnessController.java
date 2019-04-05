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

    public static HttpResponse startSingleTest(int configurationId, ParameterSingleObject parameterSingleObject) throws IOException {
        String json = new Gson().toJson(parameterSingleObject);
        String url = getLocalhostUrl() + "/doctor-system/doctor/illness/result/" + configurationId + "/start";
        HttpPost request = new HttpPost(url);
        HttpResponse response = crudEntity(new StringEntity(json), request, null, null, null);
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpPost request = new HttpPost(getLocalhostUrl() + "/doctor-system/doctor/illness/result/" + configurationId + "/start");
//        request.addHeader("Authorization", basicAuthPayload);
//        request.setHeader("Content-Type", "application/json");
//        request.setEntity(new StringEntity(json));
//        HttpResponse response = null;
//        try {
//            response = client.execute(request);
//        } catch (HttpHostConnectException e) {
//            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
//            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        }
        return response;
    }

    public static HttpResponse resultSingleTest(int processId) throws IOException {
        String url = getLocalhostUrl() + "/doctor-system/doctor/illness/result/" + processId + "/start";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet(getLocalhostUrl() + "/doctor-system/doctor/illness/result/" + processId + "/start");
//        request.addHeader("Authorization", basicAuthPayload);
//        HttpResponse response = null;
//        try {
//            response = client.execute(request);
//        } catch (HttpHostConnectException e) {
//            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
//            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
//        }
        return response;
    }
}
