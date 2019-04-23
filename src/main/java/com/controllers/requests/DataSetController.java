package com.controllers.requests;

import com.google.gson.Gson;
import com.models.DataSet;
import com.tools.Constant;
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

    public static HttpResponse getDataSetAllPage(int page) throws IOException {
        String url = getUrl() + "/dataset/all/" + page + "/" + Constant.getObjectOnPage();
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public static HttpResponse getSpecialistDataSetAllPage(int page) throws IOException {
        String url = getUrl() + "/dataset/all/" + page + "/" + Constant.getObjectOnPage() + "/specialist";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public static HttpResponse changeActive(int dataSetId, boolean active) throws IOException {
        String url = getUrl() + "/dataset/" + dataSetId + "/activate";
        String json;
        if(active){
            json = "true";
        } else {
            json = "false";
        }
        HttpPut request = new HttpPut(url);
        HttpResponse response = crudEntity(new StringEntity(json), null, null, request, null);
        return response;
    }

    public static HttpResponse createDataSet(DataSet dataSet) throws IOException {
        String json = new Gson().toJson(dataSet);
        String url = getUrl() + "/dataset/";
        HttpPost request = new HttpPost(url);
        HttpResponse response = crudEntity(new StringEntity(json), request, null, null, null);
        return response;
    }

    public static HttpResponse addObjectsToDataSet(File file, int dataSetId) throws IOException {
        String url = getUrl() + "/dataset/" + dataSetId + "/objects";
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((Constant.getAuth()[0] + ":" + Constant.getAuth()[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
//        HttpPost request = new HttpPost(getUrl() + "/dataset/" + dataSetId + "/objects");
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

    public static HttpResponse getDataSetById(int dataSetId) throws IOException {
        String url = getUrl() + "/dataset/" + dataSetId;
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public static HttpResponse changeDataSet(DataSet dataSet) throws IOException {
        String json = new Gson().toJson(dataSet);
        String url = getUrl() + "/dataset/" + dataSet.getId();
        HttpPut request = new HttpPut(url);
        HttpResponse response = crudEntity(new StringEntity(json),null, null, request, null);
        return response;
    }

    public static HttpResponse getDataSetObjects(int dataSetId) throws IOException {
        String url = getUrl() + "/dataset/" + dataSetId + "/objects";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public static HttpResponse deleteDataSet(int dataSetId) throws IOException {
        String url = getUrl() + "/dataset/" + dataSetId;
        HttpDelete request = new HttpDelete(url);
        HttpResponse response = crudEntity(null, null, null, null , request);
        return response;
    }
}
