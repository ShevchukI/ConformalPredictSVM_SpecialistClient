package com.models;

import com.google.gson.Gson;
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
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.Base64;

import static com.tools.Constant.*;

/**
 * Created by Admin on 03.02.2019.
 */
public class DataSet implements Serializable {
    private int id;
    private String name;
    private String description;
    private String columns;
    private boolean active;
    private SpecialistEntity specialistEntity;

    public DataSet() {
    }

    public DataSet(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public DataSet(int id, String name, String description, String columns) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.columns = columns;
    }

    public DataSet(String name, String description, String columns) {
        this.name = name;
        this.description = description;
        this.columns = columns;
    }

    public DataSet(int id, String name, String description, boolean active, SpecialistEntity specialistEntity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.specialistEntity = specialistEntity;
    }

    public DataSet(int id, String name, String description, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
    }

    public int addNew(String name, String description, String columns, File fileBuf) {
        int statusCode = 0;
        try {
            this.name = name;
            this.description = description;
            this.columns = columns;
            HttpResponse response = createDataSet(this);
            statusCode = response.getStatusLine().getStatusCode();
            if (checkStatusCode(statusCode)) {
                this.id = Integer.parseInt(EntityUtils.toString(response.getEntity(), "UTF-8"));
                response = addObjectsToDataSet(fileBuf, this.id);
                statusCode = response.getStatusLine().getStatusCode();
                return statusCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statusCode;
    }

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
        if (active) {
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

    public HttpResponse addObjectsToDataSet(File file, int dataSetId) throws IOException {
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
        HttpResponse response = crudEntity(new StringEntity(json), null, null, request, null);
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
        HttpResponse response = crudEntity(null, null, null, null, request);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getVisibleActive() {
        if (this.isActive()) {
            return "Enabled";
        } else {
            return "Disabled";
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public SpecialistEntity getSpecialistEntity() {
        return specialistEntity;
    }

    public void setSpecialistEntity(SpecialistEntity specialistEntity) {
        this.specialistEntity = specialistEntity;
    }

    public String getOwner() {
        return specialistEntity.getName() + " " + specialistEntity.getSurname();
    }

    public DataSet fromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        return new Gson().fromJson(json, DataSet.class);
    }


}
