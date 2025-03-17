package com.example.iruda.projects.dto;

import com.example.iruda.projects.ProjectType;

public record ProjectRequest(
        String name,
        ProjectType projectType
) {
}
