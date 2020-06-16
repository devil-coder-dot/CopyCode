package com.cosafe.android.models;

import java.util.List;

public class GetAllDownloadData {
    private List<GetAllDownLoadFiles> rows;
    private String statusCode;
    private String totalRecords;

    public String getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(String str) {
        this.statusCode = str;
    }

    public String getTotalRecords() {
        return this.totalRecords;
    }

    public void setTotalRecords(String str) {
        this.totalRecords = str;
    }

    public List<GetAllDownLoadFiles> getGetAllDownLoadFilesList() {
        return this.rows;
    }

    public void setGetAllDownLoadFilesList(List<GetAllDownLoadFiles> list) {
        this.rows = list;
    }
}
