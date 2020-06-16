package com.cosafe.android.models.gson;

public class VerifyOtpResponse {
    private String key;
    private Integer ok;
    private String result;

    public Integer getOk() {
        return this.ok;
    }

    public void setOk(Integer num) {
        this.ok = num;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String str) {
        this.result = str;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }
}
