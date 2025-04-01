package com.example.iruda.projects;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, Long> {
    void deleteByProjectId(Long projectId);
}
