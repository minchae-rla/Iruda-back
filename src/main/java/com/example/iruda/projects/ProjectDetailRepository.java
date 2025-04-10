package com.example.iruda.projects;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, Long> {

    List<ProjectDetail> findAllByProjectId(Long projectId);
}
