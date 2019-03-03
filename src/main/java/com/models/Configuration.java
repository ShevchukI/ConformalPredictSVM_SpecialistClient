package com.models;

/**
 * Created by Admin on 17.02.2019.
 */
public class Configuration {
    protected String name;
    protected double c;
    protected int degree;
    protected double eps;
    protected double gamma;
    private int kernelParameter;
    protected double nu;
    protected int probability;
    private int svmParameter;
    protected double testPart;

    public Configuration() {
    }

//    public Configuration(String name, double c, int degree, double eps, double gamma, int kernelParameter,
//                         double nu, int probability, int svmParameter, double testPart) {
//        this.name = name;
//        this.c = c;
//        this.degree = degree;
//        this.eps = eps;
//        this.gamma = gamma;
//        this.kernelParameter = kernelParameter;
//        this.nu = nu;
//        this.probability = probability;
//        this.svmParameter = svmParameter;
//        this.testPart = testPart;
//    }

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
        return "Configuration{" +
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
