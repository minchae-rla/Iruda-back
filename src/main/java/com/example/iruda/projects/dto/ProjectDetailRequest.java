package com.example.iruda.projects.dto;

import java.util.Date;

public record ProjectDetailRequest(
        Long projectId,
        String title,
        String content,
        Date startDate,
        Date endDate,
        String alarmSet
) {
}

