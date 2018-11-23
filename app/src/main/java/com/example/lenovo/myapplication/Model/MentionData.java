package com.example.lenovo.myapplication.Model;

import io.realm.RealmObject;

public class MentionData extends RealmObject {

    long timestamp;

    double xAccBI;
    double yAccBI;
    double zAccBI;
    double xGyroBI;
    double yGyroBI;
    double zGyroBI;
    double xMagBI;
    double yMagBI;
    double zMagBI;

    double xAccEx;
    double yAccEX;
    double zAccEX;
    double xGyroEX;
    double yGyroEX;
    double zGyroEX;
    double xMagEx;
    double yMagEX;
    double zMagEX;

    String gestureState;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getxAccBI() {
        return xAccBI;
    }

    public void setxAccBI(double xAccBI) {
        this.xAccBI = xAccBI;
    }

    public double getyAccBI() {
        return yAccBI;
    }

    public void setyAccBI(double yAccBI) {
        this.yAccBI = yAccBI;
    }

    public double getzAccBI() {
        return zAccBI;
    }

    public void setzAccBI(double zAccBI) {
        this.zAccBI = zAccBI;
    }

    public double getxGyroBI() {
        return xGyroBI;
    }

    public void setxGyroBI(double xGyroBI) {
        this.xGyroBI = xGyroBI;
    }

    public double getyGyroBI() {
        return yGyroBI;
    }

    public void setyGyroBI(double yGyroBI) {
        this.yGyroBI = yGyroBI;
    }

    public double getzGyroBI() {
        return zGyroBI;
    }

    public void setzGyroBI(double zGyroBI) {
        this.zGyroBI = zGyroBI;
    }

    public double getxMagBI() {
        return xMagBI;
    }

    public void setxMagBI(double xMagBI) {
        this.xMagBI = xMagBI;
    }

    public double getyMagBI() {
        return yMagBI;
    }

    public void setyMagBI(double yMagBI) {
        this.yMagBI = yMagBI;
    }

    public double getzMagBI() {
        return zMagBI;
    }

    public void setzMagBI(double zMagBI) {
        this.zMagBI = zMagBI;
    }

    public double getxAccEx() {
        return xAccEx;
    }

    public void setxAccEx(double xAccEx) {
        this.xAccEx = xAccEx;
    }

    public double getyAccEX() {
        return yAccEX;
    }

    public void setyAccEX(double yAccEX) {
        this.yAccEX = yAccEX;
    }

    public double getzAccEX() {
        return zAccEX;
    }

    public void setzAccEX(double zAccEX) {
        this.zAccEX = zAccEX;
    }

    public double getxGyroEX() {
        return xGyroEX;
    }

    public void setxGyroEX(double xGyroEX) {
        this.xGyroEX = xGyroEX;
    }

    public double getyGyroEX() {
        return yGyroEX;
    }

    public void setyGyroEX(double yGyroEX) {
        this.yGyroEX = yGyroEX;
    }

    public double getzGyroEX() {
        return zGyroEX;
    }

    public void setzGyroEX(double zGyroEX) {
        this.zGyroEX = zGyroEX;
    }

    public double getxMagEx() {
        return xMagEx;
    }

    public void setxMagEx(double xMagEx) {
        this.xMagEx = xMagEx;
    }

    public double getyMagEX() {
        return yMagEX;
    }

    public void setyMagEX(double yMagEX) {
        this.yMagEX = yMagEX;
    }

    public double getzMagEX() {
        return zMagEX;
    }

    public void setzMagEX(double zMagEX) {
        this.zMagEX = zMagEX;
    }

    public String getGestureState() {
        return gestureState;
    }

    public void setGestureState(String gestureState) {
        this.gestureState = gestureState;
    }
}
