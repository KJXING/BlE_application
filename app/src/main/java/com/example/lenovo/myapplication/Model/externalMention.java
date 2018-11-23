package com.example.lenovo.myapplication.Model;

import io.realm.RealmObject;

public class externalMention extends RealmObject {

    long timestamp;

    double xAccEx;
    double yAccEx;
    double zAccEx;

    double xGyroEx;
    double yGyroEx;
    double zGyroEx;

    double xMagEx;
    double yMagEx;
    double zMagEx;

    String gestureState;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getGestureState() {
        return gestureState;
    }

    public void setGestureState(String gestureState) {
        this.gestureState = gestureState;
    }

    public double getxAccEx() {
        return xAccEx;
    }

    public void setxAccEx(double xAccEx) {
        this.xAccEx = xAccEx;
    }

    public double getyAccEx() {
        return yAccEx;
    }

    public void setyAccEx(double yAccEx) {
        this.yAccEx = yAccEx;
    }

    public double getzAccEx() {
        return zAccEx;
    }

    public void setzAccEx(double zAccEx) {
        this.zAccEx = zAccEx;
    }

    public double getxGyroEx() {
        return xGyroEx;
    }

    public void setxGyroEx(double xGyroEx) {
        this.xGyroEx = xGyroEx;
    }

    public double getyGyroEx() {
        return yGyroEx;
    }

    public void setyGyroEx(double yGyroEx) {
        this.yGyroEx = yGyroEx;
    }

    public double getzGyroEx() {
        return zGyroEx;
    }

    public void setzGyroEx(double zGyroEx) {
        this.zGyroEx = zGyroEx;
    }

    public double getxMagEx() {
        return xMagEx;
    }

    public void setxMagEx(double xMagEx) {
        this.xMagEx = xMagEx;
    }

    public double getyMagEx() {
        return yMagEx;
    }

    public void setyMagEx(double yMagEx) {
        this.yMagEx = yMagEx;
    }

    public double getzMagEx() {
        return zMagEx;
    }

    public void setzMagEx(double zMagEx) {
        this.zMagEx = zMagEx;
    }
}
