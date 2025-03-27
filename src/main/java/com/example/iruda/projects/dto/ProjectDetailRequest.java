package com.example.iruda.projects.dto;

import java.util.Date;

public class ProjectDetailRequest {
    private String title;
    private String content;
    private Date startDate;
    private Date endDate;
    private String alarmSet;

    public String title() {
        return title;
    }

    public String content() {
        return content;
    }

    public Date startDate() {
        return startDate;
    }

    public Date endDate() {
        return endDate;
    }

    public String alarmSet() {
        return alarmSet;
    }
}

