package com.models;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Admin on 08.02.2019.
 */
public class DataSetPage {
    private int numberOfPages;
    private List<DataSet> datasetEntities;

    public DataSetPage() {
    }

    public DataSetPage(int numberOfPages, List<DataSet> dataSetEntities) {
        this.numberOfPages = numberOfPages;
        this.datasetEntities = dataSetEntities;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public List<DataSet> getDataSetEntities() {
        return datasetEntities;
    }

    public void setDataSetEntities(List<DataSet> dataSetEntities) {
        this.datasetEntities = dataSetEntities;
    }

    public DataSetPage fromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        return new Gson().fromJson(json, DataSetPage.class);
    }

}
