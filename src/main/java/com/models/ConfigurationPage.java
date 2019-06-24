package com.models;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Admin on 21.02.2019.
 */
public class ConfigurationPage {
    private int numberOfPages;
    private List<ConfigurationEntity> datasetConfigurationEntities;

    public ConfigurationPage() {
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public List<ConfigurationEntity> getDatasetConfigurationEntities() {
        return datasetConfigurationEntities;
    }

    public void setDatasetConfigurationEntities(List<ConfigurationEntity> datasetConfigurationEntities) {
        this.datasetConfigurationEntities = datasetConfigurationEntities;
    }

    public ConfigurationPage fromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        return new Gson().fromJson(json, ConfigurationPage.class);
    }
}
