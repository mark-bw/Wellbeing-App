package com.sociallaboursupply.sls_wellbeing_app.Model;

import java.util.Arrays;
import java.util.Date;

public class MoodModel {
    public static final String HAPPY = "happy";
    public static final String SAD = "sad";
    public static final String NEUTRAL = "neutral";
    public static final String[] STATUS_OPTIONS = new String[] {HAPPY, SAD, NEUTRAL, null};

    private Integer id, userId;
    private String status;
    private Date date;
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        assert Arrays.asList(STATUS_OPTIONS).contains(status);
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

}
