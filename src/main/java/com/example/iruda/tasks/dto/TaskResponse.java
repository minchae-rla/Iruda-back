package com.example.iruda.tasks.dto;

import com.example.iruda.tasks.Task;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record TaskResponse(
        Long id,
        String title,
        String content,
        String startDate,
        String endDate,
        String alarmSet,
        String color,
        boolean readAlarm
) {
    public static TaskResponse fromEntity(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ZoneId zone = ZoneId.of("Asia/Seoul");

        String start = task.getStartDate().toInstant().atZone(zone).toLocalDate().format(formatter);
        String end = task.getEndDate().toInstant().atZone(zone).toLocalDate().format(formatter);

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                start,
                end,
                task.getAlarmSet(),
                task.getColor(),
                task.isReadAlarm()
        );
    }
}
