package com.example.lenovo.myapplication.Model;

import io.realm.RealmObject;

public class SorDfeatureData extends RealmObject {
    long timestamp;
    double DValue_A_BI;
    double DValue_A_EX;
    double DValue_W_BI;
    double DValue_W_EX;

    String status;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getDValue_A_BI() {
        return DValue_A_BI;
    }

    public void setDValue_A_BI(double DValue_A_BI) {
        this.DValue_A_BI = DValue_A_BI;
    }

    public double getDValue_A_EX() {
        return DValue_A_EX;
    }

    public void setDValue_A_EX(double DValue_A_EX) {
        this.DValue_A_EX = DValue_A_EX;
    }

    public double getDValue_W_BI() {
        return DValue_W_BI;
    }

    public void setDValue_W_BI(double DValue_W_BI) {
        this.DValue_W_BI = DValue_W_BI;
    }

    public double getDValue_W_EX() {
        return DValue_W_EX;
    }

    public void setDValue_W_EX(double DValue_W_EX) {
        this.DValue_W_EX = DValue_W_EX;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
