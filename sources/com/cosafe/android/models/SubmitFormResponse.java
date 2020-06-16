package com.cosafe.android.models;

import java.util.List;

public class SubmitFormResponse {
    private String message;
    private List<SubmitQnAResponse> rows;
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

    public List<SubmitQnAResponse> getQnARespponse() {
        return this.rows;
    }

    public void setQnAresponse(List<SubmitQnAResponse> list) {
        this.rows = list;
    }
}
