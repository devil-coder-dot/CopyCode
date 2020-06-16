package com.cosafe.android.models;

public class BreathingBean {
    private String exhale;
    private String exhaleHold;
    private String inhale;
    private String inhaleHold;
    private String logTime;

    public String getLogTime() {
        return this.logTime;
    }

    public void setLogTime(String str) {
        this.logTime = str;
    }

    public String getInhale() {
        return this.inhale;
    }

    public void setInhale(String str) {
        this.inhale = str;
    }

    public String getInhaleHold() {
        return this.inhaleHold;
    }

    public void setInhaleHold(String str) {
        this.inhaleHold = str;
    }

    public String getExhale() {
        return this.exhale;
    }

    public void setExhale(String str) {
        this.exhale = str;
    }

    public String getExhaleHold() {
        return this.exhaleHold;
    }

    public void setExhaleHold(String str) {
        this.exhaleHold = str;
    }
}
