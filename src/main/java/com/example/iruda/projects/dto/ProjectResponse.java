package com.example.iruda.projects.dto;

import com.example.iruda.projects.Project;

public record ProjectResponse(
        Long id,
        String name
) {
    public static ProjectResponse fromEntity(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName()
        );
    }
}
