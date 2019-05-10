package com.models;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Admin on 22.02.2019.
 */
public class Predict {
    private int id;
    private int realClass;
    private double pPositive;
    private double pNegative;
    private int predictClass;
    private double confidence;
    private double credibility;
    private double alphaPositive;
    private double alphaNegative;
    private DataSetObject datasetObjectsEntity;
    private String visibleClass;
    private String visibleCredibility;
    private String visibleConfidence;
    private String visibleParameters;


    public Predict() {
    }

    public Predict(int id, int realClass,
                   double pPositive, double pNegative,
                   int predictClass, double confidence, double credibility,
                   double alphaPositive, double alphaNegative) {
        this.id = id;
        this.realClass = realClass;
        this.pPositive = pPositive;
        this.pNegative = pNegative;
        this.predictClass = predictClass;
        this.confidence = confidence;
        this.credibility = credibility;
        this.alphaPositive = alphaPositive;
        this.alphaNegative = alphaNegative;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRealClass() {
        return realClass;
    }

    public void setRealClass(int realClass) {
        this.realClass = realClass;
    }

    public double getPPositive() {
        return pPositive;
    }

    public void setPPositive(double pPositive) {
        this.pPositive = pPositive;
    }

    public double getPNegative() {
        return pNegative;
    }

    public void setPNegative(double pNegative) {
        this.pNegative = pNegative;
    }

    public int getPredictClass() {
        return predictClass;
    }

    public void setPredictClass(int predictClass) {
        this.predictClass = predictClass;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getCredibility() {
        return credibility;
    }

    public void setCredibility(double credibility) {
        this.credibility = credibility;
    }

    public void setVisibleConfidence(String visibleConfidence) {
        this.visibleConfidence = visibleConfidence;
    }

    public double getAlphaPositive() {
        return alphaPositive;
    }

    public void setAlphaPositive(double alphaPositive) {
        this.alphaPositive = alphaPositive;
    }

    public double getAlphaNegative() {
        return alphaNegative;
    }

    public void setAlphaNegative(double alphaNegative) {
        this.alphaNegative = alphaNegative;
    }

    public String getVisibleClass() {
        if (getRealClass() == getPredictClass()) {
            switch (getPredictClass()) {
                case 1:
                    return "Positive";
                case -1:
                    return "Negative";
                default:
                    break;
            }
        }
        return "Uncertain";
    }

    public String getVisibleCredibility() {
        return visibleCredibility;
    }

    public void setVisibleCredibility(String visibleCredibility) {
        this.visibleCredibility = visibleCredibility;
    }

    public DataSetObject getDataSetObjectsEntity() {
        return datasetObjectsEntity;
    }

    public void setDataSetObjectsEntity(DataSetObject dataSetObjectsEntity) {
        this.datasetObjectsEntity = dataSetObjectsEntity;
    }

    public String getVisibleParameters() {
        return visibleParameters;
    }

    public void setVisibleParameters(String visibleParameters) {
        this.visibleParameters = visibleParameters;
    }

    public Predict fromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        return new Gson().fromJson(json, Predict.class);
    }

    public String getVisibleConfidence() {
        return visibleConfidence;
    }

//    public String getVisibleConfidence() {
//        NumberFormat formatter = new DecimalFormat("#00.00");
//        return String.valueOf(formatter.format(getConfidence())) + "%";
//    }

    public Integer getObjectId(){
        return datasetObjectsEntity.getUserObjectId();
    }


}
