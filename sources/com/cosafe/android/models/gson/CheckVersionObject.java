package com.cosafe.android.models.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckVersionObject {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("rows")
    @Expose
    private CheckVersionResponse rows;
    @SerializedName("statusCode")
    @Expose
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

    public CheckVersionResponse getRows() {
        return this.rows;
    }

    public void setRows(CheckVersionResponse checkVersionResponse) {
        this.rows = checkVersionResponse;
    }
}
