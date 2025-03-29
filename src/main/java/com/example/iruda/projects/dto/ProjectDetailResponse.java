package com.example.iruda.projects.dto;

import com.example.iruda.projects.ProjectDetail;
import java.util.Date;

public record ProjectDetailResponse(
        Long id,
        String title,
        String content,
        Date startDate,
        Date endDate
) {
    public static ProjectDetailResponse fromEntity(ProjectDetail projectDetail) {
        return new ProjectDetailResponse(
                projectDetail.getId(),
                projectDetail.getTitle(),
                projectDetail.getContent(),
                projectDetail.getStartDate(),
                projectDetail.getEndDate()
        );
    }
}
