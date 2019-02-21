package com.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 17.02.2019.
 */
public class ConfusionMatrixRow {
    private int id;
    private double epsilon;
    private int activePredictedInactive;
    private int inactivePredictedActive;
    private int inactivePredictedInactive;
    private int activePredictedActive;
    private int emptyPredictions;
    private int uncertainPredictions;

    public ConfusionMatrixRow() {
    }

    public ConfusionMatrixRow(int id, double epsilon, int activePredictedInactive, int inactivePredictedActive,
                              int inactivePredictedInactive, int activePredictedActive, int emptyPredictions,
                              int uncertainPredictions) {
        this.id = id;
        this.epsilon = epsilon;
        this.activePredictedInactive = activePredictedInactive;
        this.inactivePredictedActive = inactivePredictedActive;
        this.inactivePredictedInactive = inactivePredictedInactive;
        this.activePredictedActive = activePredictedActive;
        this.emptyPredictions = emptyPredictions;
        this.uncertainPredictions = uncertainPredictions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public int getActivePredictedInactive() {
        return activePredictedInactive;
    }

    public void setActivePredictedInactive(int activePredictedInactive) {
        this.activePredictedInactive = activePredictedInactive;
    }

    public int getInactivePredictedActive() {
        return inactivePredictedActive;
    }

    public void setInactivePredictedActive(int inactivePredictedActive) {
        this.inactivePredictedActive = inactivePredictedActive;
    }

    public int getInactivePredictedInactive() {
        return inactivePredictedInactive;
    }

    public void setInactivePredictedInactive(int inactivePredictedInactive) {
        this.inactivePredictedInactive = inactivePredictedInactive;
    }

    public int getActivePredictedActive() {
        return activePredictedActive;
    }

    public void setActivePredictedActive(int activePredictedActive) {
        this.activePredictedActive = activePredictedActive;
    }

    public int getEmptyPredictions() {
        return emptyPredictions;
    }

    public void setEmptyPredictions(int emptyPredictions) {
        this.emptyPredictions = emptyPredictions;
    }

    public int getUncertainPredictions() {
        return uncertainPredictions;
    }

    public void setUncertainPredictions(int uncertainPredictions) {
        this.uncertainPredictions = uncertainPredictions;
    }

    public List<ConfusionMatrixRow> listFromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        Type patientListType = new TypeToken<ArrayList<ConfusionMatrixRow>>(){}.getType();
        Gson gson = new Gson();
        List<ConfusionMatrixRow> list = gson.fromJson(json, patientListType);
        return list;
    }
}
