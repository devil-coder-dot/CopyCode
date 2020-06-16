package com.cosafe.android.models;

public class GetAllDownLoadFiles {
    private String filelocation;
    private String filelocationBluetooth;
    private String flag;
    private String phone;

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String str) {
        this.flag = str;
    }

    public String getFilelocation() {
        return this.filelocation;
    }

    public void setFilelocation(String str) {
        this.filelocation = str;
    }

    public String getFilelocationBluetooth() {
        return this.filelocationBluetooth;
    }

    public void setFilelocationBluetooth(String str) {
        this.filelocationBluetooth = str;
    }
}
