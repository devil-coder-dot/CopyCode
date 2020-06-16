package com.cosafe.android.models;

public class getAllupdateflag {
    private String message;
    private UpdatedFlag rows;
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

    public UpdatedFlag getRows() {
        return this.rows;
    }

    public void setRows(UpdatedFlag updatedFlag) {
        this.rows = updatedFlag;
    }
}
