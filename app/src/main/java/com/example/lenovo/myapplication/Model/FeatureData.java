package com.example.lenovo.myapplication.Model;

import io.realm.RealmObject;

public class FeatureData extends RealmObject {

    long timestamp;

    double R_A_BI;
    double R_A_EX;
    double R_W_BI;
    double R_W_EX;

    String status;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getR_A_BI() {
        return R_A_BI;
    }

    public void setR_A_BI(double r_A_BI) {
        R_A_BI = r_A_BI;
    }

    public double getR_A_EX() {
        return R_A_EX;
    }

    public void setR_A_EX(double r_A_EX) {
        R_A_EX = r_A_EX;
    }

    public double getR_W_BI() {
        return R_W_BI;
    }

    public void setR_W_BI(double r_W_BI) {
        R_W_BI = r_W_BI;
    }

    public double getR_W_EX() {
        return R_W_EX;
    }

    public void setR_W_EX(double r_W_EX) {
        R_W_EX = r_W_EX;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
