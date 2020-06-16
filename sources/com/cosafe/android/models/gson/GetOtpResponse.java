package com.cosafe.android.models.gson;

public class GetOtpResponse {
    private String message;
    private String type;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }
}
