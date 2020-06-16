package com.cosafe.android.models;

public class SubmitFormRequest {
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String answer6;
    private int userid;

    public int getUserid() {
        return this.userid;
    }

    public void setUserid(int i) {
        this.userid = i;
    }

    public String getAnswer1() {
        return this.answer1;
    }

    public void setAnswer1(String str) {
        this.answer1 = str;
    }

    public String getAnswer2() {
        return this.answer2;
    }

    public void setAnswer2(String str) {
        this.answer2 = str;
    }

    public String getAnswer3() {
        return this.answer3;
    }

    public void setAnswer3(String str) {
        this.answer3 = this.answer3;
    }

    public String getAnswer4() {
        return this.answer4;
    }

    public void setAnswer4(String str) {
        this.answer4 = str;
    }

    public String getAnswer5() {
        return this.answer5;
    }

    public void setAnswer5(String str) {
        this.answer5 = str;
    }

    public String getAnswer6() {
        return this.answer6;
    }

    public void setAnswer6(String str) {
        this.answer6 = str;
    }

    public SubmitFormRequest(int i, String str, String str2, String str3, String str4, String str5, String str6) {
        this.userid = i;
        this.answer1 = str;
        this.answer2 = str2;
        this.answer3 = str3;
        this.answer4 = str4;
        this.answer5 = str5;
        this.answer6 = str6;
    }
}
