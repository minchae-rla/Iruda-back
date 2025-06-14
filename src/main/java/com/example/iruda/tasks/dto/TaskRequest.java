package com.example.iruda.tasks.dto;

import java.util.Date;

public record TaskRequest(
        Long projectId,
        String title,
        String content,
        Date startDate,
        Date endDate,
        String alarmSet
) {
}