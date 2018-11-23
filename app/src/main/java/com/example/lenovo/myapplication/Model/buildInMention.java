package com.example.lenovo.myapplication.Model;

import io.realm.RealmObject;

public class buildInMention extends RealmObject {

    long timestamp;

    double xAccBi;
    double yAccBi;
    double zAccBi;

    double xGyroBi;
    double yGyroBi;
    double zGyroBi;

    double xMagBi;
    double yMagBi;
    double zMagBi;

    String gestureState;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public double getxAccBi() {
        return xAccBi;
    }

    public void setxAccBi(double xAccBi) {
        this.xAccBi = xAccBi;
    }

    public double getyAccBi() {
        return yAccBi;
    }

    public void setyAccBi(double yAccBi) {
        this.yAccBi = yAccBi;
    }

    public double getzAccBi() {
        return zAccBi;
    }

    public void setzAccBi(double zAccBi) {
        this.zAccBi = zAccBi;
    }

    public double getxGyroBi() {
        return xGyroBi;
    }

    public void setxGyroBi(double xGyroBi) {
        this.xGyroBi = xGyroBi;
    }

    public double getyGyroBi() {
        return yGyroBi;
    }

    public void setyGyroBi(double yGyroBi) {
        this.yGyroBi = yGyroBi;
    }

    public double getzGyroBi() {
        return zGyroBi;
    }

    public void setzGyroBi(double zGyroBi) {
        this.zGyroBi = zGyroBi;
    }

    public double getxMagBi() {
        return xMagBi;
    }

    public void setxMagBi(double xMagBi) {
        this.xMagBi = xMagBi;
    }

    public double getyMagBi() {
        return yMagBi;
    }

    public void setyMagBi(double yMagBi) {
        this.yMagBi = yMagBi;
    }

    public double getzMagBi() {
        return zMagBi;
    }

    public void setzMagBi(double zMagBi) {
        this.zMagBi = zMagBi;
    }

    public String getGestureState() {
        return gestureState;
    }

    public void setGestureState(String gestureState) {
        this.gestureState = gestureState;
    }
}
