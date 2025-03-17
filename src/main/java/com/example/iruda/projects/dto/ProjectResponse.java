package com.example.iruda.projects.dto;

import com.example.iruda.projects.Project;
import com.example.iruda.projects.ProjectType;

public record ProjectResponse(
        Long id,
        String name,
        ProjectType projectType
) {
    public static ProjectResponse fromEntity(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getProjectType()
        );
    }
}
