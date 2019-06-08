package com.models;

import com.google.gson.Gson;
import com.tools.Constant;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

import static com.tools.Constant.crudEntity;
import static com.tools.Constant.getUrl;

/**
 * Created by Admin on 17.02.2019.
 */
public class Model {
    private String name;
    private double c;
    private int degree;
    private double eps;
    private double gamma;
    private int kernelParameter;
    private double nu;
    private int probability;
    private int svmParameter;
    private double testPart;

    public Model() {
    }

    public HttpResponse createConfiguration(Model model, int dataSetId) throws IOException {
        String json = new Gson().toJson(model);
        String url = getUrl() + "/model/" + dataSetId;
        HttpPost request = new HttpPost(url);
        HttpResponse response = crudEntity(new StringEntity(json), request, null, null, null);

        return response;
    }

    public HttpResponse startGenerateConfiguration(int configurationId) throws IOException {
        String url = getUrl() + "/result/general/" + configurationId + "/start";
        HttpPost request = new HttpPost(url);
        HttpResponse response = crudEntity(null, request, null, null, null);
        return response;
    }

    public HttpResponse getProgress(int processId) throws IOException {
        String url = getUrl() + "/result/general/" + processId + "/start";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public HttpResponse getConfusionMatrix(int configurationId) throws IOException {
        String url = getUrl() + "/result/general/" + configurationId + "/confusion_matrix";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public static HttpResponse getModelAllPage(int dataSetId, int page, boolean allPage) throws IOException {
        String url;
        if (allPage) {
            url = getUrl() + "/configuration/all/" + dataSetId + "/" + page + "/" + Constant.getObjectOnPage();
        } else {
            url = getUrl() + "/configuration/all/" + dataSetId + "/" + page + "/" + Constant.getObjectOnPage() + "/specialist";
        }
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public static HttpResponse activateModel(int configId) throws IOException {
        String url = getUrl() + "/configuration/" + configId + "/activate";
        HttpPut request = new HttpPut(url);
        HttpResponse response = crudEntity(null, null, null, request, null);
        return response;
    }

    public HttpResponse getModel(int configId) throws IOException {
        String url = getUrl() + "/configuration/" + configId;
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public static HttpResponse deleteModel(int configId) throws IOException {
        String url = getUrl() + "/configuration/" + configId;
        HttpDelete request = new HttpDelete(url);
        HttpResponse response = crudEntity(null, null, null, null, request);
        return response;
    }

    public static HttpResponse changeModel(Model model, int configId) throws IOException {
        String json = new Gson().toJson(model);
        String url = getUrl() + "/model/" + configId;
        HttpPut request = new HttpPut(url);
        HttpResponse response = crudEntity(new StringEntity(json), null, null, request, null);
        return response;
    }

    public static HttpResponse getDetailedResult(int configId) throws IOException {
        String url = getUrl() + "/result/general/" + configId + "/configuration_result";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public int getKernelParameter() {
        return kernelParameter;
    }

    public void setKernelParameter(int kernelParameter) {
        this.kernelParameter = kernelParameter;
    }

    public double getNu() {
        return nu;
    }

    public void setNu(double nu) {
        this.nu = nu;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public int getSvmParameter() {
        return svmParameter;
    }

    public void setSvmParameter(int svmParameter) {
        this.svmParameter = svmParameter;
    }

    public double getTestPart() {
        return testPart;
    }

    public void setTestPart(double testPart) {
        this.testPart = testPart;
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", c=" + c +
                ", degree=" + degree +
                ", eps=" + eps +
                ", gamma=" + gamma +
                ", kernelParameter=" + kernelParameter +
                ", nu=" + nu +
                ", probability=" + probability +
                ", svmParameter=" + svmParameter +
                ", testPart=" + testPart +
                '}';
    }


}
