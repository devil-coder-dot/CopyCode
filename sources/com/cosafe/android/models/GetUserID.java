package com.cosafe.android.models;

public class GetUserID {
    private String message;
    private GetUserIDByMobile rows;
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

    public GetUserIDByMobile getRows() {
        return this.rows;
    }

    public void setRows(GetUserIDByMobile getUserIDByMobile) {
        this.rows = getUserIDByMobile;
    }
}
