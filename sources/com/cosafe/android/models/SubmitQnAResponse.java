package com.cosafe.android.models;

public class SubmitQnAResponse {
    private String answer;
    private int id;
    private String questionseq;
    private String userid;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String str) {
        this.userid = str;
    }

    public String getQuestionseq() {
        return this.questionseq;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String str) {
        this.answer = str;
    }
}
