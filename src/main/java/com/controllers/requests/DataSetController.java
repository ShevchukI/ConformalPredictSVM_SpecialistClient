package com.controllers.requests;

import com.google.gson.Gson;
import com.models.DataSet;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

/**
 * Created by Admin on 08.02.2019.
 */
public class DataSetController extends MainController {

    public HttpResponse getDataSetAllPage(String[] authorization, int page, int objectOnPage) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl() + "/dataset/all/" + page + "/" + objectOnPage);
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

    public HttpResponse getSpecialistDataSetAllPage(String[] authorization, int page, int objectOnPage) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl() + "/dataset/all/" + page + "/" + objectOnPage + "/specialist");
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

    public HttpResponse changeActive(String[] authorization, int datasetId, boolean active) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(getUrl() + "/dataset/" + datasetId + "/activate");
        request.addHeader("Authorization", basicAuthPayload);
        request.setHeader("Content-Type", "application/json");
        if (active) {
            request.setEntity(new StringEntity("true"));
        } else {
            request.setEntity(new StringEntity("false"));
        }
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (HttpHostConnectException e) {
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }
        return response;
    }

    public HttpResponse createDataSet(String[] authorization, DataSet dataSet) throws IOException {
        String json = new Gson().toJson(dataSet);
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(getUrl() + "/dataset/");
        request.addHeader("Authorization", basicAuthPayload);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(json));
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (HttpHostConnectException e) {
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }
        return response;
    }

    public HttpResponse addObjectsToDataSet(String[] authorization, File file, int datasetId) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(getUrl() + "/dataset/" + datasetId + "/objects");
        request.addHeader("Authorization", basicAuthPayload);
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("file", new FileBody(file));
        request.setEntity(entity);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (HttpHostConnectException e) {
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }
        return response;
    }

    public HttpResponse getDataSetById(String[] authorization, int datasetId) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl() + "/dataset/" + datasetId);
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

    public HttpResponse changeDataSet(String[] authorization, DataSet dataSet) throws IOException {
        String json = new Gson().toJson(dataSet);
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(getUrl() + "/dataset/" + dataSet.getId());
        request.addHeader("Authorization", basicAuthPayload);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(json));
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (HttpHostConnectException e) {
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }
        return response;
    }

    public HttpResponse getDataSetObjects(String[] authorization, int datasetId) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl() + "/dataset/" + datasetId + "/objects");
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

    public HttpResponse deleteDataSet(String[] authorization, int datasetId) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete request = new HttpDelete(getUrl() + "/dataset/" + datasetId);
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
