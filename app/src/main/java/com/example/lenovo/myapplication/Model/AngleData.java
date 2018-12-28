package com.example.lenovo.myapplication.Model;

import io.realm.RealmObject;

public class AngleData extends RealmObject {
    long timestamp;

    double angle_BI;
    double angle_EX;

    String status;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAngle_BI() {
        return angle_BI;
    }

    public void setAngle_BI(double angle_BI) {
        this.angle_BI = angle_BI;
    }

    public double getAngle_EX() {
        return angle_EX;
    }

    public void setAngle_EX(double angle_EX) {
        this.angle_EX = angle_EX;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
