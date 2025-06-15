package com.example.iruda.tasks.dto;

import com.example.iruda.tasks.Task;

import java.util.Date;

public record TaskResponse(
        Long id,
        String title,
        String content,
        Date startDate,
        Date endDate
) {
    public static TaskResponse fromEntity(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                task.getStartDate(),
                task.getEndDate()
        );
    }
}