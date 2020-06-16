package com.cosafe.android.models;

public class SaveUserDetailsData {
    private String id;
    private String isactive;
    private String quarantineLocCngdCount;
    private String quarantine_arr_ppl_count;
    private String quarantine_curr_location;
    private String userid;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String str) {
        this.userid = str;
    }

    public String getQuarantineLocCngdCount() {
        return this.quarantineLocCngdCount;
    }

    public void setQuarantineLocCngdCount(String str) {
        this.quarantineLocCngdCount = str;
    }

    public String getQuarantine_curr_location() {
        return this.quarantine_curr_location;
    }

    public void setQuarantine_curr_location(String str) {
        this.quarantine_curr_location = str;
    }

    public String getQuarantine_arr_ppl_count() {
        return this.quarantine_arr_ppl_count;
    }

    public void setQuarantine_arr_ppl_count(String str) {
        this.quarantine_arr_ppl_count = str;
    }

    public String getIsactive() {
        return this.isactive;
    }

    public void setIsactive(String str) {
        this.isactive = str;
    }
}
