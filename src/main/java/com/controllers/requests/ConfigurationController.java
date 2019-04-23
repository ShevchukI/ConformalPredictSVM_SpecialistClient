package com.controllers.requests;

import com.google.gson.Gson;
import com.models.Configuration;
import com.tools.Constant;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 * Created by Admin on 17.02.2019.
 */
public class ConfigurationController extends MainController {

    public static HttpResponse createConfiguration(Configuration configuration, int dataSetId) throws IOException {
        String json = new Gson().toJson(configuration);
        String url = getUrl() + "/configuration/" + dataSetId;
        HttpPost request = new HttpPost(url);
        HttpResponse response = crudEntity(new StringEntity(json), request, null, null, null);

        return response;
    }

    public static HttpResponse startGenerateConfiguration(int configurationId) throws IOException {
        String url = getUrl() + "/result/general/" + configurationId + "/start";
        HttpPost request = new HttpPost(url);
        HttpResponse response = crudEntity(null, request, null, null, null);
        return response;
    }

    public static HttpResponse getProgress(int processId) throws IOException {
        String url = getUrl() + "/result/general/" + processId + "/start";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public static HttpResponse getConfusionMatrix(int configurationId) throws IOException {
        String url = getUrl() + "/result/general/" + configurationId + "/confusion_matrix";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public static HttpResponse getConfigurationAllPage(int dataSetId, int page, boolean allPage) throws IOException {
        String url;
        if(allPage){
            url = getUrl() + "/configuration/all/" + dataSetId + "/" + page + "/" + Constant.getObjectOnPage();
        } else {
            url = getUrl() + "/configuration/all/" + dataSetId + "/" + page + "/" + Constant.getObjectOnPage() + "/specialist";
        }
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public static HttpResponse activateConfiguration(int configId) throws IOException {
        String url = getUrl() + "/configuration/" + configId + "/activate";
        HttpPut request = new HttpPut(url);
        HttpResponse response = crudEntity(null, null, null, request, null);
        return response;
    }

    public static HttpResponse getConfiguration(int configId) throws IOException {
        String url = getUrl() + "/configuration/" + configId;
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public static HttpResponse deleteConfiguration(int configId) throws IOException {
        String url = getUrl() + "/configuration/" + configId;
        HttpDelete request = new HttpDelete(url);
        HttpResponse response = crudEntity(null, null, null, null, request);
        return response;
    }

    public static HttpResponse changeConfiguration(Configuration configuration, int configId) throws IOException {
        String json = new Gson().toJson(configuration);
        String url = getUrl() + "/configuration/" + configId;
        HttpPut request = new HttpPut(url);
        HttpResponse response = crudEntity(new StringEntity(json), null, null, request, null);
        return response;
    }

    public static HttpResponse getDetailedResult(int configId) throws IOException {
        String url = getUrl() + "/result/general/" + configId+"/configuration_result";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }
}
