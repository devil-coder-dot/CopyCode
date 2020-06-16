package com.cosafe.android.models;

public class SaveUserDetails {
    private String message;
    private SaveUserDetailsData rows;
    private String statusCode;

    public String getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(String str) {
        this.statusCode = str;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public SaveUserDetailsData getRows() {
        return this.rows;
    }

    public void setRows(SaveUserDetailsData saveUserDetailsData) {
        this.rows = saveUserDetailsData;
    }
}
