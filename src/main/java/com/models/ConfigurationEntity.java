package com.models;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Admin on 21.02.2019.
 */
public class ConfigurationEntity {
    private int id;
    private String name;
    private double c;
    private int degree;
    private double eps;
    private double gamma;
    private double nu;
    private int probability;
    private double testPart;
    private boolean active;
    private String visibleActive;
    private SVMParameter svmParameter;
    private SVMParameter kernelParameter;
    private Dataset datasetEntity;
    private Specialist specialistEntity;

    public ConfigurationEntity() {
    }

    public ConfigurationEntity(int id, String name, boolean active, String visibleActive,
                               Dataset datasetEntity, Specialist specialistEntity) {
        this.id = id;
//        this.name = name;
        this.active = active;
        this.visibleActive = visibleActive;
        this.datasetEntity = datasetEntity;
        this.specialistEntity = specialistEntity;
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

    public double getTestPart() {
        return testPart;
    }

    public void setTestPart(double testPart) {
        this.testPart = testPart;
    }

    public SVMParameter getSvmParameter() {
        return svmParameter;
    }

    public SVMParameter getKernelParameter() {
        return kernelParameter;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getVisibleActive() {
        return visibleActive;
    }

    public void setVisibleActive(String visibleActive) {
        this.visibleActive = visibleActive;
    }
    public void setVisibleActive(Boolean active) {
        if(active) {
            this.visibleActive = "Enabled";
        } else {
            this.visibleActive = "Disabled";
        }
    }



    public void setSvmParameter(SVMParameter svmParameter) {
        this.svmParameter = svmParameter;
    }



    public void setKernelParameter(SVMParameter kernelParameter) {
        this.kernelParameter = kernelParameter;
    }

    public Dataset getDatasetEntity() {
        return datasetEntity;
    }

    public void setDatasetEntity(Dataset datasetEntity) {
        this.datasetEntity = datasetEntity;
    }

    public Specialist getSpecialistEntity() {
        return specialistEntity;
    }

    public void setSpecialistEntity(Specialist specialistEntity) {
        this.specialistEntity = specialistEntity;
    }

    public ConfigurationEntity fromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        return new Gson().fromJson(json, ConfigurationEntity.class);
    }
}
