package com.models;

/**
 * Created by Admin on 22.02.2019.
 */
public class ParameterSingleObject {
    private String params;
    private Double significance;

    public ParameterSingleObject() {
    }

    public ParameterSingleObject(String params, double significance) {
        this.params = params;
        this.significance = significance;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Double getSignificance() {
        return significance;
    }

    public void setSignificance(Double significance) {
        this.significance = significance;
    }

    @Override
    public String toString() {
        return "ParameterSingleObject{" +
                "params='" + params + '\'' +
                ", significance=" + significance +
                '}';
    }
}
