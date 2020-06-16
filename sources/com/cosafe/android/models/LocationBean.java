package com.cosafe.android.models;

public class LocationBean {
    private String Accuracy;
    private String Home_location;
    private String Lat;
    private String Log;
    private String Qurentine;

    public String getLat() {
        return this.Lat;
    }

    public void setLat(String str) {
        this.Lat = str;
    }

    public String getLog() {
        return this.Log;
    }

    public void setLog(String str) {
        this.Log = str;
    }

    public String getAccuracy() {
        return this.Accuracy;
    }

    public void setAccuracy(String str) {
        this.Accuracy = str;
    }

    public String getHome_location() {
        return this.Home_location;
    }

    public void setHome_location(String str) {
        this.Home_location = str;
    }

    public String getQurentine() {
        return this.Qurentine;
    }

    public void setQurentine(String str) {
        this.Qurentine = str;
    }
}
